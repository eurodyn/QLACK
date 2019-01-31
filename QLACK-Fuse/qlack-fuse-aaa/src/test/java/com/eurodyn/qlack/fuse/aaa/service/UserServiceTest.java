package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.UserMapper;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
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
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Spy
    private UserMapper userMapper;

    @Spy
    private UserRepository userRepository;

    @Spy
    private UserAttributeRepository userAttributeRepository;

    private InitTestValues initTestValues;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCreateUser() {
        UserDTO userDTO = initTestValues.createUserDTO();
        User user = initTestValues.createUser();
        when(userMapper.mapToEntity(userDTO)).thenReturn(user);

        String userId = userService.createUser(userDTO);
        assertEquals(user.getId(), userId);
    }

}
