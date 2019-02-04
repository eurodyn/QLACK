package com.eurodyn.qlack.fuse.audit.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class AuditLevelMapperTest {

    @InjectMocks
    private AuditLevelMapperImpl auditLevelMapperImpl;

    private InitTestValues initTestValues;
    private AuditLevel auditLevel;
    private AuditLevelDTO auditLevelDTO;
    private List<AuditLevel> auditLevels;
    private List<AuditLevelDTO> auditLevelDTOs;

    @Before
    public void init() {
        initTestValues = new InitTestValues();

        auditLevel = initTestValues.createAuditLevel();
        auditLevelDTO = initTestValues.createAuditLevelDTO();
        auditLevels = initTestValues.createAuditLevels();
        auditLevelDTOs = initTestValues.createAuditLevelDTOs();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapToDTOIdTest() {
        auditLevelDTO =  auditLevelMapperImpl.mapToDTO(auditLevel);
        assertEquals(auditLevel.getId(), auditLevelDTO.getId());
    }

    @Test
    public void mapToEntityIdTest() {
        auditLevelMapperImpl.mapToEntity(auditLevelDTO);
        assertEquals(auditLevelDTO.getId(), auditLevel.getId());
    }

    @Test
    public void mapToDTONameTest() {
        auditLevelDTO =  auditLevelMapperImpl.mapToDTO(auditLevel);
        assertEquals(auditLevel.getName(), auditLevelDTO.getName());
    }

    @Test
    public void mapToEntityNameTest() {
        auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
        assertEquals(auditLevelDTO.getName(), auditLevel.getName());
    }

    @Test
    public void mapToDTODescriptionTest() {
        auditLevelDTO =  auditLevelMapperImpl.mapToDTO(auditLevel);
        assertEquals(auditLevel.getDescription(), auditLevelDTO.getDescription());
    }

    @Test
    public void mapToEntityDescriptionTest() {
        auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
        assertEquals(auditLevelDTO.getDescription(), auditLevel.getDescription());
    }

    @Test
    public void mapToDTOPrinSessionIdTest() {
        auditLevelDTO =  auditLevelMapperImpl.mapToDTO(auditLevel);
        assertEquals(auditLevel.getPrinSessionId(), auditLevelDTO.getPrinSessionId());
    }

    @Test
    public void mapToEntityPrinSessionIdTest() {
        auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
        assertEquals(auditLevelDTO.getPrinSessionId(), auditLevel.getPrinSessionId());
    }

    @Test
    public void mapToDTOCreatedOnTest() {
        auditLevelDTO =  auditLevelMapperImpl.mapToDTO(auditLevel);
        assertEquals(auditLevel.getCreatedOn(), auditLevelDTO.getCreatedOn());
    }

    @Test
    public void mapToEntityCreatedOnTest() {
        auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
        assertEquals(auditLevelDTO.getCreatedOn(), auditLevel.getCreatedOn());
    }

    @Test
    public void mapToDTOListTest() {
        auditLevelDTOs = auditLevelMapperImpl.mapToDTO(auditLevels);
        assertEquals(auditLevelDTOs.size(), auditLevels.size());
    }

    @Test
    public void mapToEntityListTest() {
        auditLevels = auditLevelMapperImpl.mapToEntity(auditLevelDTOs);
        assertEquals(auditLevels.size(), auditLevelDTOs.size());
    }

    @Test
    public void mapToExistingEntityTest() {
        auditLevelDTO.setName("New name");
        auditLevelMapperImpl.mapToExistingEntity(auditLevelDTO, auditLevel);
        assertEquals(auditLevelDTO.getName(), auditLevel.getName());
    }
}