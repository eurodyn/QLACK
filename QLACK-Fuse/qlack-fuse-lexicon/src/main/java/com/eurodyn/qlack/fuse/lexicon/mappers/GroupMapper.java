package com.eurodyn.qlack.fuse.lexicon.mappers;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import org.mapstruct.Mapper;

//import java.util.Set;
//
//@Mapper(componentModel = "spring")
//public interface GroupMapper {
//  GroupDTO toGroupDTO(Group g);
//  Set<GroupDTO> toGroupDTOSet(Iterable<Group> g);
//}

@Mapper(componentModel = "spring")
public interface GroupMapper extends LexiconMapper<Group, GroupDTO> {

}
