package com.eurodyn.qlack.fuse.lexicon.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Template;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TemplateMapper extends LexiconMapper<Template, TemplateDTO> {

}
