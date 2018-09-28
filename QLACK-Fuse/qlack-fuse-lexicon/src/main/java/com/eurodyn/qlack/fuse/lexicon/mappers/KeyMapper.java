package com.eurodyn.qlack.fuse.lexicon.mappers;

import java.util.HashMap;
import java.util.Map;

import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Key;

public interface KeyMapper extends LexiconMapper<Key, KeyDTO> {
	
	default KeyDTO mapToDTOwithTranslations(Key key) {

		KeyDTO dto = mapToDTO(key);
		Map<String, String> translations = new HashMap<>();
		if (key.getData() != null) {
			for (Data data : key.getData()) {
				translations.put(data.getLanguage().getId(), data.getValue());
			}
		}
		dto.setTranslations(translations);
		return dto;
	}
	

}
