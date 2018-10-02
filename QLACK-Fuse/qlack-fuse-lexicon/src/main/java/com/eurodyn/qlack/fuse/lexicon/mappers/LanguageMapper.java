package com.eurodyn.qlack.fuse.lexicon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Language;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LanguageMapper extends LexiconMapper<Language, LanguageDTO> {

}
