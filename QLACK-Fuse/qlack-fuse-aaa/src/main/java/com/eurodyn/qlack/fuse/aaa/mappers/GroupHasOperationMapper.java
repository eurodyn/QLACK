package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.GroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.GroupHasOperation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {GroupMapper.class, OperationMapper.class, ResourceMapper.class})
public interface GroupHasOperationMapper extends AAAMapper<GroupHasOperation, GroupHasOperationDTO> {

}
