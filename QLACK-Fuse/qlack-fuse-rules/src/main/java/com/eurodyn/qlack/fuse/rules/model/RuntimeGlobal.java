package com.eurodyn.qlack.fuse.rules.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "rul_runtime_kglobal")
public class RuntimeGlobal implements Serializable {
    private static final long serialVersionUID = -4425563178825097556L;

    @Id
    private String id;

    @Column(name = "global_id")
    private String globalId;

    @Lob
    private byte[] state;

    @Transient
    private Object object;

    @ManyToOne
    @JoinColumn(name = "ksession_id")
    private RuntimeSession session;

    // -- Constructors

    public RuntimeGlobal() {
        id = UUID.randomUUID().toString();
    }

    // -- Queries

    public static RuntimeGlobal findByGlobalId(EntityManager em, String sessionId, String globalId) {
        String jpql =
            "SELECT g " +
                "FROM RuntimeGlobal g " +
                "WHERE g.session.id = :sessionId AND g.globalId = :globalId";

        try {
            return em.createQuery(jpql, RuntimeGlobal.class)
                .setParameter("sessionId", sessionId)
                .setParameter("globalId", globalId)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
