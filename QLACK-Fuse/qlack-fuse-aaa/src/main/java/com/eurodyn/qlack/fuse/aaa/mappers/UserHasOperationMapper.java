package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {OperationMapper.class, ResourceMapper.class})
public interface UserHasOperationMapper extends AAAMapper<UserHasOperation, UserHasOperationDTO> {

}
