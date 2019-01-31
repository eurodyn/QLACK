package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapperImpl;

    @Spy
    private UserAttributeMapper userAttributeMapper;

    private InitTestValues initTestValues;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapToDTOIdTest(){
        User user = initTestValues.createUser();
        UserDTO userDTO  = userMapperImpl.mapToDTO(user);
        assertEquals(user.getId(), userDTO.getId());
    }

    @Test
    public void mapToDTOUsernameTest(){
        User user = initTestValues.createUser();
        UserDTO userDTO  = userMapperImpl.mapToDTO(user);
        assertEquals(user.getUsername(), userDTO.getUsername());
    }

    @Test
    public void mapToDTOPasswordTest(){
        User user = initTestValues.createUser();
        UserDTO userDTO  = userMapperImpl.mapToDTO(user);
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    @Test
    public void mapToDTOStatusTest(){
        User user = initTestValues.createUser();
        UserDTO userDTO  = userMapperImpl.mapToDTO(user);
        assertEquals(user.getStatus(), userDTO.getStatus());
    }

    @Test
    public void mapToDTOSuperAdminTest(){
        User user = initTestValues.createUser();
        UserDTO userDTO  = userMapperImpl.mapToDTO(user);
        assertEquals(user.isSuperadmin(), userDTO.isSuperadmin());
    }

    @Test
    public void mapToDTOExternalTest(){
        User user = initTestValues.createUser();
        UserDTO userDTO  = userMapperImpl.mapToDTO(user);
        assertEquals(user.getExternal(), userDTO.isExternal());
    }

    @Test
    public void mapToDTOUserAttributesTest(){
        User user = initTestValues.createUser();
        for (UserAttribute a: user.getUserAttributes()){
            when(userAttributeMapper.mapToDTO(a)).thenReturn(new UserAttributeDTO(a.getName(), a.getData()));
        }
        UserDTO userDTO  = userMapperImpl.mapToDTO(user);
        assertEquals(user.getUserAttributes().size(), userDTO.getUserAttributes().size());
    }

    @Test
    public void mapToEntityIdTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user  = userMapperImpl.mapToEntity(userDTO);
        assertEquals(userDTO.getId(), user.getId());
    }

    @Test
    public void mapToEntityUsernameTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user  = userMapperImpl.mapToEntity(userDTO);
        assertEquals(userDTO.getUsername(), user.getUsername());
    }

    @Test
    public void mapToEntityPasswordTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user  = userMapperImpl.mapToEntity(userDTO);
        assertEquals(userDTO.getPassword(), user.getPassword());
    }

    @Test
    public void mapToEntityStatusTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user  = userMapperImpl.mapToEntity(userDTO);
        assertEquals(userDTO.getStatus(), user.getStatus());
    }

    @Test
    public void mapToEntitySuperAdminTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user  = userMapperImpl.mapToEntity(userDTO);
        assertEquals(userDTO.isSuperadmin(), user.isSuperadmin());
    }

    @Test
    public void mapToEntityExternalTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user  = userMapperImpl.mapToEntity(userDTO);
        assertEquals(userDTO.isExternal(), user.getExternal());
    }

    @Test
    public void mapToEntityUserAttributesTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        for (UserAttributeDTO a: userDTO.getUserAttributes()){
            when(userAttributeMapper.mapToEntity(a)).thenReturn(new UserAttribute());
        }
        User user  = userMapperImpl.mapToEntity(userDTO);
        assertEquals(userDTO.getUserAttributes().size(), user.getUserAttributes().size());
    }
}