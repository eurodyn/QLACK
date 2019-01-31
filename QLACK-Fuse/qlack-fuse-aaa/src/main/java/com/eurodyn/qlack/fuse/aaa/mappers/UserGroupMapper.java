package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGroupMapper extends AAAMapper<UserGroup, UserGroupDTO> {

  @Override
  @Mapping(source = "parent.id", target = "parentId")
  UserGroupDTO mapToDTO(UserGroup userGroup);
}
