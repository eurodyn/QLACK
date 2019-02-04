package com.eurodyn.qlack.fuse.audit.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.mappers.AuditMapper;
import com.eurodyn.qlack.fuse.audit.mappers.AuditTraceMapper;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.QAudit;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditTraceRepository;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class AuditServiceTest {

    @InjectMocks
    AuditService auditService;

    AuditRepository auditRepository = mock(AuditRepository.class);;
    AuditLevelRepository auditLevelRepository = mock(AuditLevelRepository.class);
    AuditTraceRepository auditTraceRepository = mock(AuditTraceRepository.class);

    @Spy
    private AuditMapper auditMapper;

    @Spy
    private AuditTraceMapper auditTraceMapper;

    private InitTestValues initTestValues;
    private QAudit qAudit;

    @Before
    public void init(){
        auditService = new AuditService(null,auditRepository, auditMapper, auditLevelRepository, auditTraceRepository );
        initTestValues = new InitTestValues();
        qAudit = new QAudit("audit");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void auditFromDTOTest() {
        AuditDTO auditDTO = initTestValues.createAuditDTO();
        Audit audit = initTestValues.createAudit();
        when(auditMapper.mapToEntity(auditDTO)).thenReturn(audit);
        String auditId = auditService.audit(auditDTO);

        assertEquals(auditDTO.getId(), auditId);
        verify(auditRepository, times(1)).save(audit);
    }

    @Test
    public void auditCorellatedDTOListTest() {
        List<AuditDTO> auditDTOs = initTestValues.createAuditDTOs();
        List<Audit> audits = initTestValues.createAudits();
        Collection<String> auditIds = new ArrayList<>();

        for (int i = 0; i < auditDTOs.size(); i++) {
            when(auditMapper.mapToEntity(auditDTOs.get(i))).thenReturn(audits.get(i));
            auditIds.add(auditDTOs.get(i).getId());
        }

        List<String> createdAuditIDs = auditService.audits(auditDTOs, initTestValues.getCorrelationId());
        assertEquals(auditIds, createdAuditIDs);
    }

    @Test
    public void deleteAuditTest() {
        Audit audit = initTestValues.createAudit();
        Audit audit2 = initTestValues.createAudit();
        when(auditRepository.fetchById(audit.getId())).thenReturn(audit2);
        auditService.deleteAudit(audit.getId());

        verify(auditRepository, times(1)).delete(audit2);
    }

    @Test
    public void truncateAuditsTest() {
        auditService.truncateAudits();
        verify(auditRepository, times(1)).deleteAll();
    }

    @Test
    public void truncateAuditsCreatedBeforeDateTest() {
        Date date = Calendar.getInstance().getTime();
        auditService.truncateAudits(date);
        verify(auditRepository, times(1)).deleteByCreatedOnBefore(date.toInstant().toEpochMilli());
    }

    @Test
    public void truncateAuditsRetentionPeriodTest() {
        Long currentDateInMilli = Calendar.getInstance().getTimeInMillis();
        Long retentionPeriod = 604800000L;
        Long dateLimit = currentDateInMilli - retentionPeriod;
        auditService.truncateAudits(retentionPeriod);

        verify(auditRepository, times(1)).deleteByCreatedOnBefore(dateLimit);
    }

    @Test
    public void getAuditByIdTest() {
        Audit audit = initTestValues.createAudit();
        AuditDTO auditDTO = initTestValues.createAuditDTO();
        when(auditRepository.fetchById(auditDTO.getId())).thenReturn(audit);
        when(auditMapper.mapToDTO(audit)).thenReturn(auditDTO);
        AuditDTO foundAudit = auditService.getAuditById(auditDTO.getId());

        assertEquals(auditDTO.getId(), foundAudit.getId());
    }

    @Test
    public void getAuditLogsTest() {
        Page<AuditDTO> auditDTOPage = new PageImpl<AuditDTO>(initTestValues.createAuditDTOs());
        Page<Audit> auditsPage = new PageImpl<Audit>(initTestValues.createAudits());

        when(auditRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(auditsPage);
        when(auditMapper.toAuditDTO(auditsPage)).thenReturn(auditDTOPage);

        String expression = "%Params%";
        Predicate event = qAudit.event.like(expression);
        Page<AuditDTO> foundAudits = auditService.getAuditLogs(PageRequest.of(0, 10), event);

        assertEquals(auditDTOPage, foundAudits);
    }

    @Test
    public void getDistinctEventsForReferenceIdTest() {
        Audit audit = initTestValues.createAudit();
        List<String> expectedEvents = new ArrayList<>();
        expectedEvents.add("Front End Event");
        expectedEvents.add("Back End Event");

        when(auditRepository.findDistinctEventsByReferenceId(audit.getReferenceId())).thenReturn(expectedEvents);

        List<String> actualEvents = auditService.getDistinctEventsForReferenceId(audit.getReferenceId());
        assertEquals(expectedEvents, actualEvents);
    }
}
