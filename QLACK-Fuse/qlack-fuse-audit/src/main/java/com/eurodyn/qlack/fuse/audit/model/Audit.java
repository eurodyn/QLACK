package com.eurodyn.qlack.fuse.audit.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "al_audit")
@Getter
@Setter
public class Audit extends AuditBaseEntity {

    /**
     * The id of the Audit level, this Audit belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private AuditLevel levelId;

    /**
     * The id of the Audit trace of this Audit
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "trace_id")
    private AuditTrace trace;

    /**
     * The id of the web session, the Audit occurred
     */
    @Column(name = "prin_session_id")
    private String prinSessionId;

    /**
     * A short description of the Audit event
     */
    @Column(name = "short_description")
    private String shortDescription;

    /**
     * The actual Audit event
     */
    @Column(name = "event")
    private String event;

    /**
     * A number representing the date the Audit was created
     */
    @Column(name = "created_on")
    private Long createdOn;

    /**
     * The reference id of the Audit
     */
    @Column(name = "reference_id")
    private String referenceId;

    /**
     * The name of the group the Audit belongs to
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * The id used to correlate the Audit with other Audits
     */
    @Column(name = "correlation_id")
    private String correlationId;

    /**
     * Optional field to store app-specific info
     */
    @Column(name = "opt1")
    private String opt1;
    /**
     * Optional field to store app-specific info
     */
    @Column(name = "opt2")
    private String opt2;
    /**
     * Optional field to store app-specific info
     */
    @Column(name = "opt3")
    private String opt3;

    public Audit() {
        setId(java.util.UUID.randomUUID().toString());
    }
}