package com.eurodyn.qlack.fuse.rules.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rul_runtime_ksession")
public class RuntimeSession implements Serializable {
    private static final long serialVersionUID = -3899085624479625816L;

    @Id
    private String id;

    @Column(name = "session_id")
    private int sessionId;

    @ManyToOne
    @JoinColumn(name = "kbase_state_id")
    private RuntimeBaseState base;

    @OneToMany(mappedBy = "session", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<RuntimeGlobal> globals;

    // -- Constructors

    public RuntimeSession() {
        id = UUID.randomUUID().toString();
    }

}
