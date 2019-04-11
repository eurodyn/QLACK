package com.eurodyn.qlack.fuse.audit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "al_audit_trace")
@Getter
@Setter
public class AuditTrace extends AuditBaseEntity {

    /**
     * the data of the trace
     */
    @Column(name = "trace_data")
    private String traceData;

    /**
     * the Audit, which is referenced by this trace
     */
    @OneToOne(mappedBy = "trace")
    private Audit audit;

    public AuditTrace() {
        setId(java.util.UUID.randomUUID().toString());
    }
}