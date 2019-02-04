package com.eurodyn.qlack.fuse.audit.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
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
public class AuditTraceMapperTest {

    @InjectMocks
    private AuditTraceMapperImpl auditTraceMapperImpl;

    private InitTestValues initTestValues;
    private AuditTrace auditTrace;
    private AuditTraceDTO auditTraceDTO;
    private List<AuditTrace> auditTraces;
    private List<AuditTraceDTO> auditTraceDTOs;

    @Before
    public void init() {
        initTestValues = new InitTestValues();

        auditTrace = initTestValues.createAuditTrace();
        auditTraceDTO = initTestValues.createAuditTraceDTO();
        auditTraces = initTestValues.createAuditTraces();
        auditTraceDTOs = initTestValues.createAuditTraceDTOs();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapToDTOIdTest() {
        auditTraceDTO = auditTraceMapperImpl.mapToDTO(auditTrace);
        assertEquals(auditTraceDTO.getId(), auditTrace.getId());
    }

    @Test
    public void mapToEntityIdTest() {
        auditTrace = auditTraceMapperImpl.mapToEntity(auditTraceDTO);
        assertEquals(auditTrace.getId(), auditTraceDTO.getId());
    }

    @Test
    public void mapToDTOTraceDataTest() {
        auditTraceDTO = auditTraceMapperImpl.mapToDTO(auditTrace);
        assertEquals(auditTraceDTO.getTraceData(), auditTrace.getTraceData());
    }

    @Test
    public void mapToEntityTraceDataTest() {
        auditTrace = auditTraceMapperImpl.mapToEntity(auditTraceDTO);
        assertEquals(auditTrace.getTraceData(), auditTraceDTO.getTraceData());
    }

    @Test
    public void mapToDTOListTest() {
        auditTraceDTOs = auditTraceMapperImpl.mapToDTO(auditTraces);
        assertEquals(auditTraces.size(), auditTraceDTOs.size());
    }

    @Test
    public void mapToEntityListTest() {
        auditTraces = auditTraceMapperImpl.mapToEntity(auditTraceDTOs);
        assertEquals(auditTraceDTOs.size(), auditTraceDTOs.size());
    }

    @Test
    public void mapToExistingEntity() {
        auditTraceDTO.setTraceData("New mock data");
        auditTraceMapperImpl.mapToExistingEntity(auditTraceDTO, auditTrace);
        assertEquals(auditTraceDTO.getTraceData(), auditTrace.getTraceData());
    }
}