package com.eurodyn.qlack.fuse.mailing.mappers;/**
 * @author European Dynamics
 */

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessagesDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InternalMessagesTest {

    @InjectMocks
    private InternalMessagesMapperImpl internalMessagesMapperImpl;

    private InitTestValues initTestValues;
    private com.eurodyn.qlack.fuse.mailing.model.InternalMessages internalMessages;
    private com.eurodyn.qlack.fuse.mailing.dto.InternalMessagesDTO internalMessagesDTO;

    @Before
    public void init() {
        initTestValues = new InitTestValues();

        internalMessages = initTestValues.createInternalMessages();
        internalMessagesDTO = initTestValues.createInternalMessagesDTO();
    }

    @Test
    public void testMapToDTOId() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getId(), internalMessagesDTO.getId());
    }

    @Test
    public void testMapToDTOSubject() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getSubject(), internalMessagesDTO.getSubject());
    }

    @Test
    public void testMapToDTOMessage() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getMessage(), internalMessagesDTO.getMessage());
    }

    @Test
    public void testMapToDTOMailFrom() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getMailFrom(), internalMessagesDTO.getMailFrom());
    }

    @Test
    public void testMapToDTOMailTo() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getMailTo(), internalMessagesDTO.getMailTo());
    }

    @Test
    public void testMapToDTODateSent() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getDateSent().longValue(), internalMessagesDTO.getDateSent().getTime());
    }

    @Test
    public void testMapToDTODateReceived() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getDateReceived().longValue(), internalMessagesDTO.getDateReceived().getTime());
    }

    @Test
    public void testMapToDTOStatus() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getStatus(), internalMessagesDTO.getStatus());
    }

    @Test
    public void testMapToDTODeleteType() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getDeleteType(), internalMessagesDTO.getDeleteType());
    }

    @Test
    public void testMapToDTOAttachments() {
        InternalMessagesDTO internalMessagesDTO = internalMessagesMapperImpl.mapToDTO(internalMessages);
        assertEquals(internalMessages.getAttachments().size(), internalMessagesDTO.getAttachments().size());
    }

    @Test
    public void testMapToEntityId() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getId(), internalMessages.getId());
    }

    @Test
    public void testMapToEntitySubject() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getSubject(), internalMessages.getSubject());
    }

    @Test
    public void testMapToEntityMessage() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getMessage(), internalMessages.getMessage());
    }

    @Test
    public void testMapToEntityMailFrom() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getMailFrom(), internalMessages.getMailFrom());
    }

    @Test
    public void testMapToEntityMailTo() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getMailTo(), internalMessages.getMailTo());
    }

    @Test
    public void testMapToEntityDateSent() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getDateSent().getTime(), internalMessages.getDateSent().longValue());
    }

    @Test
    public void testMapToEntityDateReceived() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getDateReceived().getTime(), internalMessages.getDateReceived().longValue());
    }

    @Test
    public void testMapToEntityStatus() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getStatus(), internalMessages.getStatus());
    }

    @Test
    public void testMapToEntityDeleteType() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getDeleteType(), internalMessages.getDeleteType());
    }

    @Test
    public void testMapToEntityAttachments() {
        InternalMessages internalMessages = internalMessagesMapperImpl.mapToEntity(internalMessagesDTO);
        assertEquals(internalMessagesDTO.getAttachments().size(), internalMessages.getAttachments().size());
    }

}
