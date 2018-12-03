package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {UserHasOperationMapper.class, GroupHasOperationMapper.class})
@DecoratedWith(UserDetailsMapperDecorator.class)
public interface UserDetailsMapper extends AAAMapper<User, UserDetailsDTO> {

    @Mapping(target = "userHasOperations", source = "userHasOperations")
    UserDetailsDTO mapToDTO(User user);

}
