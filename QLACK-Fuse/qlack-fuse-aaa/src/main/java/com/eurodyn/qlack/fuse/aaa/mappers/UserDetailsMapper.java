package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.decorators.UserDetailsMapperDecorator;
import com.eurodyn.qlack.fuse.aaa.model.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserGroupHasOperationMapper.class})
@DecoratedWith(UserDetailsMapperDecorator.class)
public interface UserDetailsMapper {

    UserDetailsDTO mapToDTO(User user);

}
