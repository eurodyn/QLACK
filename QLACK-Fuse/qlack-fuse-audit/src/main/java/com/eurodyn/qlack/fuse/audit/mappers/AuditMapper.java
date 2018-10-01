package com.eurodyn.qlack.fuse.audit.mappers;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {
    AuditLevelMapper.class
})
public interface AuditMapper extends AuditBaseMapper<Audit, AuditDTO> {

//  @Mapping(expression = "java(new java.util.Date(audit.getCreatedOn()))", target = "createdOn")
//  @Mapping(expression = "java(audit.getLevelId().getName())", target = "level")
//  AuditLogDTO toAuditLogDTO(Audit audit);

  @Override
  @Mapping(target = "traceId.traceData", source = "traceData")
  Audit mapToEntity(AuditDTO dto);

  @Override
  @Mapping(source = "traceId.traceData", target = "traceData")
  AuditDTO mapToDTO(Audit audit);

  default Page<AuditDTO> toAuditDTO(Page<Audit> audits) {
    return audits.map(this::mapToDTO);
  }



}
