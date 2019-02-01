package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserAttributesMapperTest {

    @InjectMocks
    private UserAttributeMapperImpl userAttributeMapperImpl;

    private InitTestValues initTestValues;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapToDTOIdTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getId(), userAttributeDTO.getId());
    }

    @Test
    public void mapToDTONameTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getName(), userAttributeDTO.getName());
    }

    @Test
    public void mapToDTODataTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getData(), userAttributeDTO.getData());
    }

    @Test
    public void mapToDTOContentTypeTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getContentType(), userAttributeDTO.getContentType());
    }

    @Test
    public void mapToDTOUserTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getUser().getId(), userAttributeDTO.getUserId());
    }

    @Test
    public void mapToEntityIdTest(){
        UserAttributeDTO userAttributeDTO = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getId(), userAttribute.getId());
    }

    @Test
    public void mapToEntityNameTest(){
        UserAttributeDTO userAttributeDTO = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getName(), userAttribute.getName());
    }

    @Test
    public void mapToEntityDataTest(){
        UserAttributeDTO userAttributeDTO = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getData(), userAttribute.getData());
    }

    @Test
    public void mapToEntityContentTypeTest(){
        UserAttributeDTO userAttributeDTO = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getContentType(), userAttribute.getContentType());
    }

    @Test
    public void mapToDTOListSizeTest(){
        List<UserAttribute> userAttributes = initTestValues.createUserAttributes(null);
        List<UserAttributeDTO> userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttributes);
        assertEquals(userAttributes.size(), userAttributeDTO.size());
    }

    @Test
    public void mapToEntityListSizeTest(){
        List<UserAttributeDTO> userAttributesDTO = initTestValues.createUserAttributesDTO(null);
        List<UserAttribute> userAttributes  = userAttributeMapperImpl.mapToEntity(userAttributesDTO);
        assertEquals(userAttributesDTO.size(), userAttributes.size());
    }

    @Test
    public void mapToExistingEntity(){
        UserAttributeDTO userAttributeDTO = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        userAttributeMapperImpl.mapToExistingEntity(userAttributeDTO, userAttribute);
    }
}
