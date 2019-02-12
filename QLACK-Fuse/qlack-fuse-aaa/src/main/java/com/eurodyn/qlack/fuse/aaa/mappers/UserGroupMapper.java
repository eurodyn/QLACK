package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGroupMapper {

  @Mapping(source = "parent.id", target = "parentId")
  UserGroupDTO mapToDTO(UserGroup userGroup, @Context boolean lazyRelatives);

  List<UserGroupDTO> mapToDTO(List<UserGroup> userGroups, @Context boolean lazyRelatives);

  UserGroup mapToEntity(UserGroupDTO userGroupDTO);

  List<UserGroup> mapToEntity(List<UserGroupDTO> userGroupsDTO);

  void mapToExistingEntity(UserGroupDTO userGroupDTO, @MappingTarget UserGroup userGroup);
}
