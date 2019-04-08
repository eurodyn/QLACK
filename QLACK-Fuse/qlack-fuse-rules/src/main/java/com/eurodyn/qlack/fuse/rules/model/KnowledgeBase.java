package com.eurodyn.qlack.fuse.rules.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Knowledge Base entity, that holds the data of a Drools Knowledge Base.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "rul_kbase")
@Getter
@Setter
@NoArgsConstructor
public class KnowledgeBase extends RulesModel {

    /**
     * the serialized KieBase
     */
    @Lob
    private byte[] state;

    /**
     * the libraries of the Knowledge Base
     */
    @OneToMany(mappedBy = "base", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<KnowledgeBaseLibrary> libraries;

    /**
     * the rules of the Knowledge Base
     */
    @OneToMany(mappedBy = "base", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<KnowledgeBaseRule> rules;

}
