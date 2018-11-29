package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {UserHasOperationMapper.class, GroupHasOperationMapper.class})
public interface UserDetailsMapper extends AAAMapper<User, UserDetailsDTO> {

    @Mappings({
        @Mapping(target = "userHasOperations", source = "userHasOperations"),
        // @Mapping(target = "groupHasOperations", source = "groups.groupHasOperations")
    })
    void mapToDTO(UserDetailsDTO dto, @MappingTarget User user);

}
