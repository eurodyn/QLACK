package com.eurodyn.qlack.fuse.lexicon.service;


import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.TemplateProcessingException;
import com.eurodyn.qlack.fuse.lexicon.mappers.TemplateMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.TemplateRepository;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@Service
@Validated
public class TemplateService {


    private static final Logger LOGGER = Logger.getLogger(TemplateService.class.getName());

    private final TemplateRepository templateRepository;
    private final LanguageRepository languageRepository;

    private TemplateMapper templateMapper;


    public TemplateService(TemplateRepository templateRepository, LanguageRepository languageRepository, TemplateMapper templateMapper) {
        this.templateRepository = templateRepository;
        this.languageRepository = languageRepository;
        this.templateMapper = templateMapper;
    }


    public String createTemplate(TemplateDTO template) {
        Template entity = new Template();
        saveEntity(template, entity);
        return entity.getId();
    }

    public void updateTemplate(TemplateDTO template) {
        Template entity = templateRepository.fetchById(template.getId());
        saveEntity(template, entity);
    }

    private void saveEntity(TemplateDTO template, Template entity) {
        entity.setName(template.getName());
        entity.setContent(template.getContent());
        entity.setLanguage(languageRepository.fetchById(template.getLanguageId()));
        templateRepository.save(entity);
    }

    public void deleteTemplate(String templateId) {
        templateRepository.deleteById(templateId);
    }

    public TemplateDTO getTemplate(String templateId) {
        return templateMapper.mapToDTO(templateRepository.findById(templateId).orElseThrow(() -> new QDoesNotExistException()));
    }

    public Map<String, String> getTemplateContentByName(String templateName) {
        List<Template> templates = templateRepository.findByName(templateName);

        if (templates.isEmpty()) {
            return null;
        }
        Map<String, String> contents = new HashMap<>();
        for (Template template : templates) {
            contents.put(template.getLanguage().getId(), template.getContent());
        }
        return contents;
    }

    public String getTemplateContentByName(String templateName, String languageId) {
        Template template = templateRepository.findByNameAndLanguageId(templateName, languageId);
        return template != null ? template.getContent() : null;
    }

    public String processTemplateByName(String templateName, String languageId, Map<String, Object> templateData) {
        Template template = templateRepository.findByNameAndLanguageId(templateName, languageId);

        return processTemplate(template, templateData);
    }

    public String processTemplateByNameAndLocale(String templateName, String locale, Map<String, Object> templateData) {
        Template template = templateRepository.findByNameAndLanguageLocale(templateName, locale);

        return processTemplate(template, templateData);
    }

    public String processTemplate(String templateBody, Map<String, Object> templateData) {
        Template template = new Template();
        template.setContent(templateBody);
        template.setName(UUID.randomUUID().toString());

        return processTemplate(template, templateData);
    }

    /**
     * Helper method to process a template as string.
     */
    private String _processTemplate(String content, String templateName, Map<String, Object> templateData) {
        StringWriter retVal = new StringWriter();
        try {
            freemarker.template.Template fTemplate = new freemarker.template.Template(templateName, new StringReader(content), null);
            fTemplate.process(templateData, retVal);
            retVal.flush();
        } catch (TemplateException | IOException ex) {
            // Catch exception and throw RuntimeException instead in order to
            // also roll back the transaction.
            LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            throw new TemplateProcessingException(MessageFormat.format("Error processing template {0}.", templateName));
        }
        return retVal.toString();
    }

    /**
     * Helper method to process a template. It performs a 2-passes variable replacement to support variables resolution within variables. We
     * have opted for a non-recursive calling approach to keep the code simple as only 2 levels of nesting is supported.
     */
    private String processTemplate(Template template, Map<String, Object> templateData) {
        String retVal = template.getContent();
        // 1st pass.
        if (retVal.contains("${")) {
            retVal = _processTemplate(template.getContent(), template.getName(), templateData);
            // 2nd pass (to support variables in variables).
            if (retVal.contains("${")) {
                retVal = _processTemplate(retVal, template.getName(), templateData);
            }
        }

        return retVal;
    }

}