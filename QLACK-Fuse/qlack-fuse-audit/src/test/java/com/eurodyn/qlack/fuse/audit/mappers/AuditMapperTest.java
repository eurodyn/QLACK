package com.eurodyn.qlack.fuse.audit.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
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
public class AuditMapperTest {

    @InjectMocks
    private AuditMapperImpl auditMapperImpl;

    @Spy
    private AuditTraceMapperImpl auditTraceMapperImpl;

    private InitTestValues initTestValues;

    private Audit audit;
    private AuditDTO auditDTO;
    private AuditTrace auditTrace;
    private AuditTraceDTO auditTraceDTO;
    private List<Audit> audits;
    private List<AuditDTO> auditDTOs;

    @Before
    public void init() {
        initTestValues = new InitTestValues();

        audit = initTestValues.createAudit();
        auditDTO = initTestValues.createAuditDTO();
        auditTrace = initTestValues.createAuditTrace();
        auditTraceDTO = initTestValues.createAuditTraceDTO();
        audits = initTestValues.createAudits();
        auditDTOs = initTestValues.createAuditDTOs();


        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapToDTOIdTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getId(), auditDTO.getId());
    }

    @Test
    public void mapToEntityIdTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getId(), audit.getId());
    }

    @Test
    public void mapToDTOCreatedOnTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getCreatedOn(), auditDTO.getCreatedOn());
    }

    @Test
    public void mapToEntityCreatedOnTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getCreatedOn(), audit.getCreatedOn());
    }

    @Test
    public void mapToDTOPrinSessionTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getPrinSessionId(), auditDTO.getPrinSessionId());
    }

    @Test
    public void mapToEntityPrinSessionTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getPrinSessionId(), audit.getPrinSessionId());
    }

    @Test
    public void mapToDTOShortDescriptionTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getShortDescription(), auditDTO.getShortDescription());
    }

    @Test
    public void mapToEntityShortDescriptionTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getShortDescription(), audit.getShortDescription());
    }

    @Test
    public void mapToDTOEventTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getEvent(), auditDTO.getEvent());
    }

    @Test
    public void mapToEntityEventTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getEvent(), audit.getEvent());
    }

    @Test
    public void mapToDTOGroupNameTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getGroupName(), auditDTO.getGroupName());
    }

    @Test
    public void mapToEntityGroupNameTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getGroupName(), audit.getGroupName());
    }

    @Test
    public void mapToDTOCorrelationIdTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getCorrelationId(), auditDTO.getCorrelationId());
    }

    @Test
    public void mapToEntityCorrelationIdTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getCorrelationId(), audit.getCorrelationId());
    }

    @Test
    public void mapToDTOReferenceIdTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getReferenceId(), auditDTO.getReferenceId());
    }

    @Test
    public void mapToEntityReferenceIdTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getReferenceId(), audit.getReferenceId());
    }

    @Test
    public void mapToDTOpt1Test() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getOpt1(), auditDTO.getOpt1());
    }

    @Test
    public void mapToEntityOpt1Test() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getOpt1(), audit.getOpt1());
    }

    @Test
    public void mapToDTOpt2Test() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getOpt2(), auditDTO.getOpt2());
    }

    @Test
    public void mapToEntityOpt2Test() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getOpt2(), audit.getOpt2());
    }

    @Test
    public void mapToDTOpt3Test() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getOpt3(), auditDTO.getOpt3());
    }

    @Test
    public void mapToEntityOpt3Test() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getOpt3(), audit.getOpt3());
    }

    @Test
    public void mapToDTOTraceTest() {
        auditDTO =  auditMapperImpl.mapToDTO(audit);
        assertEquals(audit.getTrace().getTraceData(), auditDTO.getTrace().getTraceData());
    }

    @Test
    public void mapToEntityTraceTest() {
        audit =  auditMapperImpl.mapToEntity(auditDTO);
        assertEquals(auditDTO.getTrace().getTraceData(), audit.getTrace().getTraceData());
    }

    @Test
    public void mapToExistingEntityTest() {
        auditDTO.setEvent("New event");
        auditMapperImpl.mapToExistingEntity(auditDTO, audit);
        assertEquals(auditDTO.getEvent(), audit.getEvent());
    }
}