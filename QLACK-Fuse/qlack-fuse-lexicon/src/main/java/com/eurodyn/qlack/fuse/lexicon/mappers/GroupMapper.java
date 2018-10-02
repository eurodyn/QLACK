package com.eurodyn.qlack.fuse.lexicon.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper extends LexiconMapper<Group, GroupDTO> {

}
