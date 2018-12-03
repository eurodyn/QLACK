package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
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
    private GroupHasOperationMapper groupHasOperationMapper;

    @Override
    public UserDetailsDTO mapToDTO(User user) {
        UserDetailsDTO dto = delegate.mapToDTO(user);

        dto.setGroupHasOperations(user.getGroups().stream()
            .map(g -> groupHasOperationMapper.mapToDTO(g.getGroupHasOperations()))
            .flatMap(List::stream)
            .collect(Collectors.toList()));

        return dto;
    }

}
