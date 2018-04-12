package com.eurodyn.qlack.fuse.mailing.service;

import com.eurodyn.qlack.fuse.mailing.dto.QFMContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.QFMDistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.model.QFMContact;
import com.eurodyn.qlack.fuse.mailing.model.QFMDistributionList;
import com.eurodyn.qlack.fuse.mailing.util.QFMConverterUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide distribution list related services. For details regarding the functionality offered see
 * the respective interfaces.
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
@Transactional
public class QFMDistributionListService {

  @PersistenceContext
  private EntityManager em;

  /**
   * Create a new distribution list.
   */
  public void createDistributionList(QFMDistributionListDTO dto) {
    QFMDistributionList dlist = QFMConverterUtil.dlistConvert(dto);
    em.persist(dlist);
  }

  /**
   * Edit an existing distribution list.
   */
  public void editDistributionList(QFMDistributionListDTO dto) {
    String id = dto.getId();
    QFMDistributionList dlist = QFMConverterUtil.dlistConvert(dto);
    dlist.setId(id);
    em.merge(dlist);
  }

  /**
   * Delete a distribution list.
   */
  public void deleteDistributionList(String id) {
    QFMDistributionList dlist = findById(id);
    em.remove(dlist);
  }

  /**
   * Find a specific distribution list.
   */
  public QFMDistributionListDTO find(Object id) {
    QFMDistributionList dlist = findById(id);
    return QFMConverterUtil.dlistConvert(dlist);
  }

  /**
   * Find QFMDistributionList Entity object for provided id.
   *
   * @return QFMDistributionList
   */
  private QFMDistributionList findById(Object id) {
    return em.find(QFMDistributionList.class, id);
  }

  /**
   * Search for a specific distribution list, with the criteria provided. (Only the name can be
   * provided as criteria at the moment.)
   */
  public List<QFMDistributionListDTO> search(String name) {
    List<QFMDistributionList> distributionList = null;
    if (name == null) {
      distributionList = QFMDistributionList.findAll(em);
    } else {
      distributionList = QFMDistributionList.findByName(em, name);
    }

    List<QFMDistributionListDTO> distributionDtoList = new ArrayList<>();
    for (QFMDistributionList distribution : distributionList) {
      QFMDistributionListDTO distributionListDto = QFMConverterUtil.dlistConvert(distribution);
      distributionDtoList.add(distributionListDto);
    }

    return distributionDtoList;
  }

  /**
   * Create a new contact.
   *
   * @return id of contact
   */
  public String createContact(QFMContactDTO dto) {
    QFMContact contact = QFMConverterUtil.contactConvert(dto);
    em.persist(contact);

    return contact.getId();
  }

  /**
   * Add a contact to a distribution list.
   */
  public void addContactToDistributionList(String distributionId, String contactId) {
    QFMDistributionList dlist = findById(distributionId);
    QFMContact contact = findContactById(contactId);
    dlist.getContacts().add(contact);
  }

  /**
   * Remove a contact from a distribution list.
   */
  public void removeContactFromDistributionList(String distributionId, String contactId) {
    QFMDistributionList dlist = findById(distributionId);
    QFMContact contact = findContactById(contactId);
    dlist.getContacts().remove(contact);
  }

  private QFMContact findContactById(String contactId) {
    return em.find(QFMContact.class, contactId);
  }

}
