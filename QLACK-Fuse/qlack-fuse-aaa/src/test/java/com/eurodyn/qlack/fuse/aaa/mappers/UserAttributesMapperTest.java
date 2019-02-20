package com.eurodyn.qlack.fuse.aaa.mappers;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserAttributesMapperTest {
    private String id;
    private String name;
    private String data;
    private String userId;
    private byte[] binData;
    private String contentType;

    // @InjectMocks
    // private UserAttributeMapperImpl userAttributeMapperImpl;
    // private UserAttributeMapperImpl userAttributesMapperImpl;
    //
    // private InitTestValues initTestValues;
    //
    // @Before
    // public void init(){
    //     initTestValues = new InitTestValues();
    //     MockitoAnnotations.initMocks(this);
    // }
    //
    // @Test
    // public void mapToDTOIdTest() {
    //     UserAttribute userAttribute = initTestValues.createUserAttribute();
    //     UserAttributeDTO userAttributeDto = userAttributeMapperImpl.mapToDTO(userAttribute);
    //     assertEquals(userAttribute.getId(), userAttributeDto.getName());
    // }
    //
    // // @Test
    // // public void mapToDTOIdTest(){
    // //     UserAttribute userAttribute = initTestValues.createUserAttribute(null);
    // //     UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
    // //     assertEquals(userAttribute.getId(), userAttributeDto.getId());
    // // }
    //
    //
    //
    // @Test
    // public void mapToDTONameTest(){
    //     UserAttribute userAttribute = initTestValues.createUserAttribute(null);
    //     UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
    //     assertEquals(userAttribute.getName(), userAttributeDto.getName());
    // }
    //
    // @Test
    // public void mapToDTODataTest(){
    //     UserAttribute userAttribute = initTestValues.createUserAttribute(null);
    //     UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
    //     assertEquals(userAttribute.getData(), userAttributeDto.getData());
    // }
    //
    // @Test
    // public void mapToDTOContentTypeTest(){
    //     UserAttribute userAttribute = initTestValues.createUserAttribute(null);
    //     UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
    //     assertEquals(userAttribute.getContentType(), userAttributeDto.getContentType());
    // }
    //
    // @Test
    // public void mapToDTOUserTest(){
    //     UserAttribute userAttribute = initTestValues.createUserAttribute(null);
    //     UserAttributeDTO userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttribute);
    //     assertEquals(userAttribute.getUser().getId(), userAttributeDto.getUserId());
    // }
    //
    // @Test
    // public void mapToEntityIdTest(){
    //     UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
    //     UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
    //     assertEquals(userAttributeDto.getId(), userAttribute.getId());
    // }
    //
    // @Test
    // public void mapToEntityNameTest(){
    //     UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
    //     UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
    //     assertEquals(userAttributeDto.getName(), userAttribute.getName());
    // }
    //
    // @Test
    // public void mapToEntityDataTest(){
    //     UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
    //     UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
    //     assertEquals(userAttributeDto.getData(), userAttribute.getData());
    // }
    //
    // @Test
    // public void mapToEntityContentTypeTest(){
    //     UserAttributeDTO userAttributeDto = initTestValues.createUserAttributeDTO(null);
    //     UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDto);
    //     assertEquals(userAttributeDto.getContentType(), userAttribute.getContentType());
    // }
    //
    // @Test
    // public void mapToDTOListSizeTest(){
    //     List<UserAttribute> userAttributes = initTestValues.createUserAttributes(null);
    //     List<UserAttributeDTO> userAttributeDto  = userAttributeMapperImpl.mapToDTO(userAttributes);
    //     assertEquals(userAttributes.size(), userAttributeDto.size());
    // }
    //
    // @Test
    // public void mapToEntityListSizeTest(){
    //     List<UserAttributeDTO> userAttributesDto = initTestValues.createUserAttributesDTO(null);
    //     List<UserAttribute> userAttributes  = userAttributeMapperImpl.mapToEntity(userAttributesDto);
    //     assertEquals(userAttributesDto.size(), userAttributes.size());
    // }
}
