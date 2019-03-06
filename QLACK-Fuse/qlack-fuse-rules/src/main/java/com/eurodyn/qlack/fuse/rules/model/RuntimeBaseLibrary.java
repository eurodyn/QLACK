package com.eurodyn.qlack.fuse.rules.model;
import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rul_runtime_kbase_library")
public class RuntimeBaseLibrary implements Serializable {
    private static final long serialVersionUID = -1428178339286367026L;

    @Id
    private String id;

    @Lob
    private byte[] library;

    @ManyToOne
    @JoinColumn(name = "kbase_state_id")
    private RuntimeBaseState base;

    // -- Constructors

    public RuntimeBaseLibrary() {
        id = UUID.randomUUID().toString();
    }

}
