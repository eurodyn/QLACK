package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.model.Email;

@Mapper(componentModel = "spring")
public interface EmailMapper extends MailingMapper<Email, EmailDTO> {

}
