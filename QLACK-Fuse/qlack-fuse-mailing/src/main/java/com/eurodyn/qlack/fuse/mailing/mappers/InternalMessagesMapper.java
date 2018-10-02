package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.InternalMessagesDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessages;

@Mapper(componentModel = "spring")
public interface InternalMessagesMapper extends MailingMapper<InternalMessages, InternalMessagesDTO> {

}
