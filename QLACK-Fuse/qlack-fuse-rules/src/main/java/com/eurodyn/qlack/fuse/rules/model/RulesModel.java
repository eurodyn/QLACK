package com.eurodyn.qlack.fuse.rules.model;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Superclass that contains fields used by the same entities.
 *
 * @author European Dynamics SA
 */
@MappedSuperclass
@Getter
@Setter
public class RulesModel implements Serializable {

    /**
     * the auto-generated uuid of the entity
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
}
