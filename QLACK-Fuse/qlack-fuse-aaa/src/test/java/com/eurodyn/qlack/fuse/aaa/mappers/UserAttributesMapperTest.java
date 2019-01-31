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
        UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getId(), userAttributeDto.getId());
    }

    @Test
    public void mapToDTONameTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getName(), userAttributeDto.getName());
    }

    @Test
    public void mapToDTODataTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getData(), userAttributeDto.getData());
    }

    @Test
    public void mapToDTOContentTypeTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getContentType(), userAttributeDto.getContentType());
    }

    @Test
    public void mapToDTOUserTest(){
        UserAttribute userAttribute = initTestValues.createUserAttribute(null);
        UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getUser().getId(), userAttributeDto.getUserId());
    }

    @Test
    public void mapToEntityIdTest(){
        UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
        assertEquals(userAttributeDto.getId(), userAttribute.getId());
    }

    @Test
    public void mapToEntityNameTest(){
        UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
        assertEquals(userAttributeDto.getName(), userAttribute.getName());
    }

    @Test
    public void mapToEntityDataTest(){
        UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
        assertEquals(userAttributeDto.getData(), userAttribute.getData());
    }

    @Test
    public void mapToEntityContentTypeTest(){
        UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
        assertEquals(userAttributeDto.getContentType(), userAttribute.getContentType());
    }

    @Test
    public void mapToDTOListSizeTest(){
        List<UserAttribute> userAttributes = initTestValues.createUserAttributes(null);
        List<UserAttributeDTO> userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttributes);
        assertEquals(userAttributes.size(), userAttributeDto.size());
    }

    @Test
    public void mapToEntityListSizeTest(){
        List<UserAttributeDTO> userAttributesDto = initTestValues.createUserAttributesDTO(null);
        List<UserAttribute> userAttributes  = userAttributeMapperImpl.mapToEntity(userAttributesDto);
        assertEquals(userAttributesDto.size(), userAttributes.size());
    }
}
