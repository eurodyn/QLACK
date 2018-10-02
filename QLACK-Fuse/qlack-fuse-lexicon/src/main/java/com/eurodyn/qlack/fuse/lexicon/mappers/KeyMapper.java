package com.eurodyn.qlack.fuse.lexicon.mappers;

import java.util.HashMap;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Key;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeyMapper extends LexiconMapper<Key, KeyDTO> {
	
//	@Override
//  @Mapping(source = "parent.id", target = "parentId")
	default KeyDTO mapToDTOwithTranslations(Key entity, boolean addtranslations) {

		KeyDTO dto = mapToDTO(entity);
		Map<String, String> translations = new HashMap<>();
		
		if (addtranslations && (entity.getData() != null)) {
			for (Data data : entity.getData()) {
				translations.put(data.getLanguage().getId(), data.getValue());
			}
		}
		dto.setTranslations(translations);
		return dto;
	}
	

}
