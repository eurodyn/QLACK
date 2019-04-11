package com.eurodyn.qlack.fuse.audit.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Audit Base entity, that holds the id of all Audit based entities.
 *
 * @author European Dynamics SA
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AuditBaseEntity {

    /**
     * the id of all Audit based entities (Audit, Audit level and Audit trace)
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
}
