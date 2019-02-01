package com.eurodyn.qlack.fuse.aaa;

import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitTestValues {

    public UserAttribute createUserAttribute(User user){
        UserAttribute userAttribute = new UserAttribute();
        userAttribute.setId("dca76ec3-0423-4a17-8287-afd311697dbf");
        userAttribute.setName("fullName");
        userAttribute.setData("FirstName LastName");
        userAttribute.setContentType("text");
        if (user == null){
            userAttribute.setUser(this.createUser());
        } else{
            userAttribute.setUser(user);
        }

        return userAttribute;
    }

    public UserAttributeDTO createUserAttributeDTO(String userId){
        UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
        userAttributeDTO.setId("dca76ec3-0423-4a17-8287-afd311697dbf");
        userAttributeDTO.setName("fullName");
        userAttributeDTO.setData("FirstName LastName");
        userAttributeDTO.setContentType("text");
        if (userId == null){
            userAttributeDTO.setUserId(this.createUser().getId());
        } else{
            userAttributeDTO.setUserId(userId);
        }

        return userAttributeDTO;
    }

    public List<UserAttribute> createUserAttributes(User user){
        List<UserAttribute> userAttributes = new ArrayList<>();
        userAttributes.add(this.createUserAttribute(user));

        UserAttribute userAttribute = new UserAttribute();
        userAttribute.setId("ef682d4c-be43-4a33-8262-8af497816277");
        userAttribute.setName("company");
        userAttribute.setData("European Dynamics");
        userAttribute.setContentType("text");
        if (user == null){
            userAttribute.setUser(this.createUser());
        } else{
            userAttribute.setUser(user);
        }
        userAttributes.add(userAttribute);

        return userAttributes;
    }

    public List<UserAttributeDTO> createUserAttributesDTO(String userId){
        List<UserAttributeDTO> userAttributesDTO = new ArrayList<>();
        userAttributesDTO.add(this.createUserAttributeDTO(userId));

        UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
        userAttributeDTO.setId("ef682d4c-be43-4a33-8262-8af497816277");
        userAttributeDTO.setName("company");
        userAttributeDTO.setData("European Dynamics");
        userAttributeDTO.setContentType("text");
        if (userId == null){
            userAttributeDTO.setUserId(this.createUser().getId());
        } else{
            userAttributeDTO.setUserId(userId);
        }
        userAttributesDTO.add(userAttributeDTO);

        return userAttributesDTO;
    }

    public User createUser(){
        User user = new User();
        user.setId("57d30f8d-cf0c-4742-9893-09e2aa08c255");
        user.setUsername("AAA Default User");
        user.setPassword("thisisaverysecurepassword");
        user.setStatus((byte)1);
        user.setSuperadmin(true);
        user.setExternal(false);
        user.setUserAttributes(this.createUserAttributes(user));

        return user;
    }

    public UserDTO createUserDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId("57d30f8d-cf0c-4742-9893-09e2aa08c255");
        userDTO.setUsername("AAA Default User");
        userDTO.setPassword("thisisaverysecurepassword");
        userDTO.setStatus((byte)1);
        userDTO.setSuperadmin(true);
        userDTO.setExternal(false);
        userDTO.setUserAttributes(new HashSet<>(this.createUserAttributesDTO(userDTO.getId())));

        return userDTO;
    }

    public List<User> createUsers(){
        List<User> users = new ArrayList<>();
        users.add(this.createUser());

        User user = new User();
        user.setId("0b422f60-a66b-4526-937d-26802cd9c8a1");
        user.setUsername("AAA Additional User");
        user.setPassword("thisisanextremelysecurepassword");
        user.setStatus((byte)1);
        user.setSuperadmin(true);
        user.setExternal(false);
        users.add(user);

        return users;
    }

    public List<UserDTO> createUsersDTO(){
        List<UserDTO> usersDTO = new ArrayList<>();
        usersDTO.add(this.createUserDTO());

        UserDTO userDTO = new UserDTO();
        userDTO.setId("0b422f60-a66b-4526-937d-26802cd9c8a1");
        userDTO.setUsername("AAA Additional User");
        userDTO.setPassword("thisisanextremelysecurepassword");
        userDTO.setStatus((byte)1);
        userDTO.setSuperadmin(true);
        userDTO.setExternal(false);
        usersDTO.add(userDTO);

        return usersDTO;
    }
}
