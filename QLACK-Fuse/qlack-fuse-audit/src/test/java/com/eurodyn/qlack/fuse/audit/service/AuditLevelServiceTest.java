package com.eurodyn.qlack.fuse.audit.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.exceptions.AlreadyExistsException;
import com.eurodyn.qlack.fuse.audit.mappers.AuditLevelMapper;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class AuditLevelServiceTest {

    @InjectMocks
    private AuditLevelService auditLevelService;

    @Spy
    private AuditLevelMapper auditLevelMapper;

    private AuditLevelRepository auditLevelRepository = mock(AuditLevelRepository.class);

    private InitTestValues initTestValues;
    private AuditLevelDTO auditLevelDTO;
    private AuditLevel auditLevel;

    @Before
    public void init(){
        auditLevelService = new AuditLevelService(auditLevelRepository, auditLevelMapper);
        initTestValues = new InitTestValues();
        auditLevelDTO = initTestValues.createAuditLevelDTO();
        auditLevel = initTestValues.createAuditLevel();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addLevelTest() {
        when(auditLevelMapper.mapToEntity(auditLevelDTO)).thenReturn(auditLevel);
        String auditLevelId = auditLevelService.addLevel(auditLevelDTO);

        assertEquals(auditLevel.getId(), auditLevelId);
        verify(auditLevelRepository, times(1)).save(auditLevel);

    }

    public void addNotExistingLevelTest(){
        when(auditLevelService.listAuditLevels()).thenReturn(null);
        auditLevelService.addLevelIfNotExists(auditLevelDTO);
        verify(auditLevelService, times(1)).addLevel(auditLevelDTO);
    }

    @Test(expected = AlreadyExistsException.class)
    public void addExistingLevelTest(){
        List<AuditLevelDTO> auditLevelDTOs = initTestValues.createAuditLevelDTOs();
        when(auditLevelService.listAuditLevels()).thenReturn(auditLevelDTOs);
        auditLevelService.addLevelIfNotExists(auditLevelDTO);
    }

    @Test
    public void deleteLevelByIdTest() {
        AuditLevel auditLevel2 = initTestValues.createAuditLevel();
        when(auditLevelRepository.fetchById(auditLevel.getId())).thenReturn(auditLevel2);
        auditLevelService.deleteLevelById(auditLevel.getId());

        verify(auditLevelRepository, times(1)).delete(auditLevel2);
    }

    @Test
    public void deleteLevelByNameTest() {
        AuditLevel auditLevel2 = initTestValues.createAuditLevel();
        when(auditLevelRepository.findByName(auditLevel.getName())).thenReturn(auditLevel2);
        auditLevelService.deleteLevelByName(auditLevel.getName());

        verify(auditLevelRepository, times(1)).delete(auditLevel2);
    }

    @Test
    public void  updateLevelTest() {
        when(auditLevelMapper.mapToEntity(auditLevelDTO)).thenReturn(auditLevel);
        auditLevelService.updateLevel(auditLevelDTO);

        verify(auditLevelMapper, times(1)).mapToEntity(auditLevelDTO);
    }

    @Test
    public void getAuditLevelByNameTest() {
        when(auditLevelRepository.findByName(auditLevel.getName())).thenReturn(auditLevel);
        when(auditLevelMapper.mapToDTO(auditLevel)).thenReturn(auditLevelDTO);
        AuditLevelDTO foundAudit = auditLevelService.getAuditLevelByName(auditLevel.getName());

        assertEquals(auditLevelDTO.getId(), foundAudit.getId());
    }

    @Test
    public void listAuditLevelsTest() {
        List<AuditLevel> auditLevels = initTestValues.createAuditLevels();
        List<AuditLevelDTO> auditLevelDTOS = initTestValues.createAuditLevelDTOs();
        when(auditLevelRepository.findAll()).thenReturn(auditLevels);
        when(auditLevelMapper.mapToDTO(auditLevels)).thenReturn(auditLevelDTOS);
        List<AuditLevelDTO> allAuditLevels = auditLevelService.listAuditLevels();

        assertEquals(allAuditLevels, auditLevelDTOS);
    }
}