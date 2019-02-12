package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class UserGroupMapperTest {

    @InjectMocks
    private UserGroupMapperImpl userGroupMapperImpl;

    @Test
    public void testMapToEntityId(){
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId("id");
        UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
        assertEquals(userGroup.getId(), userGroupDTO.getId());
    }
}
