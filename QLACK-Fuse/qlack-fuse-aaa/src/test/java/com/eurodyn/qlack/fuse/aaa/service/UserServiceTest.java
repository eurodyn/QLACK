package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.UserAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.UserMapper;
import com.eurodyn.qlack.fuse.aaa.model.QUser;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.querydsl.core.types.OrderSpecifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    private UserRepository userRepository = mock(UserRepository.class);

    private UserAttributeRepository userAttributeRepository = mock(UserAttributeRepository.class);

    @Spy
    private UserAttributeMapper userAttributeMapper;

    @Spy
    private UserMapper userMapper;

    private InitTestValues initTestValues;

    private QUser qUser;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        userService = new UserService(null, null,
                userRepository, userAttributeRepository,
                null, null, userMapper,
                null, userAttributeMapper);
        qUser = new QUser("user");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUserWithoutUserAttributes(){
        UserDTO userDTO = initTestValues.createUserDTO();
        userDTO.setUserAttributes(new HashSet<>());
        User user = initTestValues.createUser();
        user.setUserAttributes(new ArrayList<>());
        when(userMapper.mapToEntity(userDTO)).thenReturn(user);

        String userId = userService.createUser(userDTO);
        assertEquals(user.getId(), userId);
        verify(userRepository, times(1)).save(user);
        verify(userAttributeRepository, never()).save(any());
    }

    @Test
    public void testCreateUser(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user = initTestValues.createUser();
        when(userMapper.mapToEntity(userDTO)).thenReturn(user);

        String userId = userService.createUser(userDTO);
        assertEquals(user.getId(), userId);
        verify(userRepository, times(1)).save(user);
        for(UserAttribute a: user.getUserAttributes()){
            verify(userAttributeRepository, times(1)).save(a);
        }
    }

    @Test
    public void testUpdateUserWithoutUserAttibutes(){
        UserDTO userDTO = initTestValues.createUserDTO();
        userDTO.setUserAttributes(new HashSet<>());
        User user = initTestValues.createUser();
        user.setUserAttributes(new ArrayList<>());

        when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
        userService.updateUser(userDTO, true, false);

        verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
        verify(userAttributeRepository, never()).findByUserIdAndName(any(), any());
    }

    private void testUpdateUserWithUserAttributes(boolean createIfMissing){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user = initTestValues.createUser();

        userDTO.setUsername("updated username");

        int index = 0;
        for (Iterator<UserAttributeDTO> iter = userDTO.getUserAttributes().iterator(); iter.hasNext();) {
            UserAttributeDTO u = iter.next();
            u.setData("updated " + u.getData());

            when(userAttributeRepository.findByUserIdAndName(user.getId(), u.getName())).thenReturn(user.getUserAttributes().get(index));
            index++;
        }

        UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
        userAttributeDTO.setId("b69381e6-a465-4137-8c82-6f54f07b0a7f");
        userAttributeDTO.setName("email");
        userAttributeDTO.setData("user@qlack.eurodyn.com");
        userAttributeDTO.setContentType("text");
        userDTO.getUserAttributes().add(userAttributeDTO);

        when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
        userService.updateUser(userDTO, true, createIfMissing);

        verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
    }

    @Test
    public void testUpdateUserWithoutNewUserAttributes(){
        testUpdateUserWithUserAttributes(false);
        verify(userAttributeRepository, times(2)).save(any());
    }

    @Test
    public void testUpdateUserWithNewUserAttributes(){
        testUpdateUserWithUserAttributes(true);
        verify(userAttributeRepository, times(3)).save(any());
    }

    @Test
    public void testDeleteUser(){
        User user = initTestValues.createUser();
        User user2 = initTestValues.createUser();

        when(userRepository.fetchById(user.getId())).thenReturn(user2);
        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).delete(user2);
    }

    @Test
    public void testGetUserById(){
        UserDTO userDTO = initTestValues.createUserDTO();
        User user = initTestValues.createUser();

        when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
        when(userMapper.mapToDTO(user)).thenReturn(userDTO);
        UserDTO foundUser = userService.getUserById(userDTO.getId());
        assertEquals(userDTO, foundUser);
    }

    @Test
    public void testGetUsersById(){
        List<User> users = initTestValues.createUsers();
        List<UserDTO> usersDTO = initTestValues.createUsersDTO();

        Collection<String> userIds = new ArrayList<>();
        for(int i=0; i<users.size(); i++){
            userIds.add(users.get(i).getId());
            when(userMapper.mapToDTO(users.get(i))).thenReturn(usersDTO.get(i));
        }

        when(userRepository.findAll(qUser.id.in(userIds))).thenReturn(users);
        Set<UserDTO> foundUsers = userService.getUsersById(userIds);

        assertEquals(new HashSet<>(usersDTO), foundUsers);
    }
}
