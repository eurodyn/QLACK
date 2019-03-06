package com.eurodyn.qlack.fuse.lexicon.service;

import com.eurodyn.qlack.common.exceptions.QAlreadyExistsException;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.LanguageProcessingException;
import com.eurodyn.qlack.fuse.lexicon.mappers.LanguageMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.repository.KeyRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@Service
@Validated
public class LanguageService {

	private static final Logger LOGGER = Logger.getLogger(LanguageService.class.getName());
	// A pattern for RTL languages (from Google Closure Templates).
	private static final Pattern RtlLocalesRe = Pattern
			.compile("^(ar|dv|he|iw|fa|nqo|ps|sd|ug|ur|yi|.*[-_](Arab|Hebr|Thaa|Nkoo|Tfng))"
					+ "(?!.*[-_](Latn|Cyrl)($|-|_))($|-|_)");

	private final KeyRepository keyRepository;
	private final LanguageRepository languageRepository;

	private KeyService keyService;
	private GroupService groupService;
	
	private LanguageMapper languageMapper; 

	@Autowired
	public LanguageService(KeyService keyService, GroupService groupService, LanguageRepository languageRepository,
			KeyRepository keyRepository, LanguageMapper languageMapper) {
		this.keyService = keyService;
		this.groupService = groupService;
		this.languageRepository = languageRepository;
		this.keyRepository = keyRepository;
		this.languageMapper = languageMapper;
	}

	public String createLanguage(LanguageDTO language) {
		Language entity = languageMapper.mapToEntity(language);
		languageRepository.save(entity);
		return entity.getId();
	}


    public String createLanguageIfNotExists(LanguageDTO language) throws QAlreadyExistsException {
        if (languageRepository.findByLocale(language.getLocale()) == null) {
            return createLanguage(language);
        } else {
            throw new QAlreadyExistsException("Language: " + language.getName() + " already exists and will not be created.");
        }
    }

	public String createLanguage(LanguageDTO language, String translationPrefix) {
		Language entity = languageMapper.mapToEntity(language); 
		languageRepository.save(entity);
		Map<String, String> translations = new HashMap<>();
		for (Key key : keyRepository.findAll()) {
			translations.put(key.getId(), (translationPrefix != null ? (translationPrefix + key.getName()) : key.getName()));
		}
		keyService.updateTranslationsForLanguage(entity.getId(), translations);

		return entity.getId();
	}

	public String createLanguage(LanguageDTO language, String sourceLanguageId, String translationPrefix) {
		Language entity = languageMapper.mapToEntity(language);
		entity.setId(language.getId());
		languageRepository.save(entity);

		Map<String, String> translations = keyService.getTranslationsForLocale((languageRepository.fetchById(sourceLanguageId)).getLocale());

		if (translationPrefix != null) {
			for (String keyId : translations.keySet()) {
				translations.put(keyId, translationPrefix + translations.get(keyId));
			}
		}
		keyService.updateTranslationsForLanguage(entity.getId(), translations);

		return entity.getId();
	}

	public void updateLanguage(LanguageDTO language) {
		Language entity = languageRepository.fetchById(language.getId()); 
		entity.setName(language.getName());
		entity.setLocale(language.getLocale());
	}

	public void deleteLanguage(String languageId) {
		languageRepository.deleteById(languageId);
	}

	public void activateLanguage(String languageId) {
		Language language = languageRepository.fetchById(languageId);
		language.setActive(true);
	}

	public void deactivateLanguage(String languageId) {
		Language language = languageRepository.fetchById(languageId);
		language.setActive(false);
	}

	public LanguageDTO getLanguage(String languageId) {
		return languageMapper.mapToDTO(languageRepository.fetchById(languageId));
	}

	public LanguageDTO getLanguageByLocale(String locale) {
		return getLanguageByLocale(locale, false);
	}

	public LanguageDTO getLanguageByLocale(String locale, boolean fallback) {
		Language language = languageRepository.findByLocale(locale); 
		if (fallback && language == null) {
			language = languageRepository.findByLocale(getEffectiveLanguage(locale, null));
		}
		return languageMapper.mapToDTO(language);
	}

	public List<LanguageDTO> getLanguages(boolean includeInactive) {
		List<Language> languages = includeInactive ? languageRepository.findAll() : languageRepository.findByActiveTrueOrderByNameAsc();
		return  languageMapper.mapToDTO(languages);
	}

	public String getEffectiveLanguage(String locale, String defaultLocale) {
		Language language = languageRepository.findByLocale(locale);
		if ((language != null) && (language.isActive())) {
			return locale;
		}

		// If no active language was found and the user-locale can be further
		// reduced, try again,
		int index = StringUtils.indexOfAny(locale, "_-");
		if (index > 0) {
			String reducedLocale = locale.substring(0, index);
			language = languageRepository.findByLocale(reducedLocale);
			if ((language != null) && (language.isActive())) {
				return reducedLocale;
			}
		}

		// If nothing worked, return the default locale after checking
		// that it corresponds to an existing active language
		Language defaultLanguage = languageRepository.findByLocale(defaultLocale); 
		if ((defaultLanguage != null) && (defaultLanguage.isActive())) {
			return defaultLocale;
		}
		return null;
	}

	public byte[] downloadLanguage(String languageId) {

		// Check that the language exists and get its translations
		Language language = languageRepository.fetchById(languageId);

		// Create an Excel workbook. The workbook will contain a sheet for each
		// group.
		Workbook wb = new HSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();

		// Iterate over all existing groups and create a sheet for each one.
		// Creating a new list below and not using the one retrieved from
		// Group.findAll since result lists are read only and
		// we need to add the empty group below to the list.
		List<Group> groups = groupService.findAll();
		// Add an dummy entry to the list to also check for translations without
		// a group.
		Group emptyGroup = new Group();
		emptyGroup.setId(null);
		emptyGroup.setTitle("<No group>");
		groups.add(0, emptyGroup);
		for (Group group : groups) {
			Map<String, String> translations = keyService.getTranslationsForGroupAndLocale(group.getId(), language.getLocale());
			if (!translations.isEmpty()) {
				Sheet sheet = wb.createSheet(group.getTitle());

				// Add the header.
				Row headerRow = sheet.createRow(0);
				headerRow.createCell(0).setCellValue(createHelper.createRichTextString("Key"));
				headerRow.createCell(1).setCellValue(createHelper.createRichTextString("Translation"));

				// Add the data.
				int rowCounter = 1;
				for (String key : translations.keySet()) {
					Row row = sheet.createRow(rowCounter++);
					row.createCell(0).setCellValue(createHelper.createRichTextString(key));
					row.createCell(1).setCellValue(createHelper.createRichTextString(translations.get(key)));
				}
			}
		}

		// Create the byte[] holding the Excel data.
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			wb.write(bos);
			return bos.toByteArray();
		} catch (IOException ex) {
			// Convert to a runtime exception in order to roll back transaction
			LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw new LanguageProcessingException("Error creating Excel file for language " + languageId);
		}
	}

	public void uploadLanguage(String languageId, byte[] lgXL) {
		try {
			Workbook wb = WorkbookFactory.create(new BufferedInputStream(new ByteArrayInputStream(lgXL)));
			for (int si = 0; si < wb.getNumberOfSheets(); si++) {
				Map<String, String> translations = new HashMap<>();
				Sheet sheet = wb.getSheetAt(si);
				String groupName = sheet.getSheetName();
				String groupId = null;
				if (StringUtils.isNotBlank(groupName)) {
					groupId = groupService.findByTitle(groupName).getId();
				}
				// Skip first row (the header of the Excel file) and start
				// parsing translations.
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					String keyName = sheet.getRow(i).getCell(0).getStringCellValue();
					String keyValue = sheet.getRow(i).getCell(1).getStringCellValue();
					translations.put(keyName, keyValue);
				}
				keyService.updateTranslationsForLanguageByKeyName(languageId, groupId, translations);
			}
		} catch (IOException | InvalidFormatException ex) {
			// Convert to a runtime exception in order to roll back transaction
			LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw new LanguageProcessingException("Error reading Excel file for language " + languageId);
		}

	}

	public boolean isLocaleRTL(String locale) {
		return RtlLocalesRe.matcher(locale).find();
	}

}
