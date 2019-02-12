package com.eurodyn.qlack.fuse.aaa.mappers.decorators;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.UserGroupMapper;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author EUROPEAN DYNAMICS SA
 */
public abstract class UserGroupMapperDecorator implements UserGroupMapper {

    @Autowired
    @Qualifier("delegate")
    private UserGroupMapper delegate;

    @Override
    public UserGroupDTO mapToDTO(UserGroup userGroup, @Context boolean lazyRelatives){
        if (!lazyRelatives){
            return delegate.mapToDTO(userGroup, false);
        } else{
            UserGroupDTO userGroupDTO = delegate.mapToDTO(userGroup, false);
            userGroupDTO.setChildren(new HashSet(userGroup.getChildren()));
            return userGroupDTO;
        }
    }

    @Override
    public List<UserGroupDTO> mapToDTO(List<UserGroup> userGroups, boolean lazyRelatives) {
        List<UserGroupDTO> userGroupsDTO = new ArrayList<>();
        userGroups.stream().forEach(userGroup -> userGroupsDTO.add(mapToDTO(userGroup, lazyRelatives)));
        return userGroupsDTO;
    }
}
