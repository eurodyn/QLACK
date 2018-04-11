/*
 * Copyright 2014 EUROPEAN DYNAMICS SA <info@eurodyn.com>
 *
 * Licensed under the EUPL, Version 1.1 only (the "License").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.eurodyn.qlack.fuse.aaa.model;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The persistent class for the aaa_op_template database table.
 */
@Entity
@Table(name = "aaa_op_template")
public class QFAOpTemplate implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Version
  private long dbversion;

  private String description;

  private String name;

  // bi-directional many-to-one association to QFAOpTemplateHasOperation
  @OneToMany(mappedBy = "template")
  private List<QFAOpTemplateHasOperation> opTemplateHasOperations;

  public QFAOpTemplate() {
    id = UUID.randomUUID().toString();
  }

  public static QFAOpTemplate find(String opTemplateID, EntityManager em) {
    return em.find(QFAOpTemplate.class, opTemplateID);
  }

  public static QFAOpTemplate findByName(final String opTemplateName,
      final EntityManager em) {
    QFAOpTemplate retVal = null;

    Query q = em
        .createQuery("select ot from QFAOpTemplate ot where ot.name = :opTemplateName");
    q.setParameter("opTemplateName", opTemplateName);
    List<QFAOpTemplate> l = q.getResultList();
    if (!l.isEmpty()) {
      retVal = l.get(0);
    }

    return retVal;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<QFAOpTemplateHasOperation> getOpTemplateHasOperations() {
    return this.opTemplateHasOperations;
  }

  public void setOpTemplateHasOperations(
      List<QFAOpTemplateHasOperation> opTemplateHasOperations) {
    this.opTemplateHasOperations = opTemplateHasOperations;
  }

  public QFAOpTemplateHasOperation addOpTemplateHasOperation(
      QFAOpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setTemplate(this);

    return opTemplateHasOperation;
  }

  public QFAOpTemplateHasOperation removeOpTemplateHasOperation(
      QFAOpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setTemplate(null);

    return opTemplateHasOperation;
  }

}