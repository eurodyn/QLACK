package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = SessionAttributeDTOMapper.class)
public interface SessionDTOMapper extends AAAMapper<Session, SessionDTO> {

  @Override
  @Mapping(source = "sessionAttributes", target = "attributes")
//  SessionDTO fromSession(Session session);
  SessionDTO mapToDTO(Session session);

  default Page<SessionDTO> fromSessions(Page<Session> sessions) {
    return sessions.map(this::mapToDTO);
  }

}