package com.eurodyn.qlack.fuse.audit.mappers;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = AuditTraceMapper.class)
public interface AuditMapper extends AuditBaseMapper<Audit, AuditDTO> {

//  @Mapping(expression = "java(new java.util.Date(audit.getCreatedOn()))", target = "createdOn")
//  @Mapping(expression = "java(audit.getLevelId().getName())", target = "level")
//  AuditLogDTO toAuditLogDTO(Audit audit);

  default Page<AuditDTO> toAuditDTO(Page<Audit> audits) {
    return audits.map(this::mapToDTO);
  }

}
