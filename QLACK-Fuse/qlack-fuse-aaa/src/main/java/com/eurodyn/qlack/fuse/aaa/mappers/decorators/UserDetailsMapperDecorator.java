package com.eurodyn.qlack.fuse.aaa.mappers.decorators;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.UserDetailsMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.UserGroupHasOperationMapper;
import com.eurodyn.qlack.fuse.aaa.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author EUROPEAN DYNAMICS SA
 */
public abstract class UserDetailsMapperDecorator implements UserDetailsMapper {

    @Autowired
    @Qualifier("delegate")
    private UserDetailsMapper delegate;

    @Autowired
    private UserGroupHasOperationMapper userGroupHasOperationMapper;

    @Override
    public UserDetailsDTO mapToDTO(User user) {
        UserDetailsDTO dto = delegate.mapToDTO(user);

        dto.setUserGroupHasOperations(user.getUserGroups().stream()
                .map(g -> userGroupHasOperationMapper.mapToDTO(g.getUserGroupHasOperations()))
                .flatMap(List::stream)
                .collect(Collectors.toList()));

        return dto;
    }

}
