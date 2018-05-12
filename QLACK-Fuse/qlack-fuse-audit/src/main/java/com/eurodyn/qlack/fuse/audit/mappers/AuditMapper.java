package com.eurodyn.qlack.fuse.audit.mappers;

import com.eurodyn.qlack.fuse.audit.dto.AuditLogDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditMapper {

  @Mapping(expression = "java(new java.util.Date(audit.getCreatedOn()))", target = "createdOn")
  AuditLogDTO toAuditLogDTO(Audit audit);

  default Page<AuditLogDTO> toAuditLogDTO(Page<Audit> audits) {
    return audits.map(this::toAuditLogDTO);
  }

}
