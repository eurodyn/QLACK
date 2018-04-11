package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.QFAResourceDTO;
import com.eurodyn.qlack.fuse.aaa.model.QFAResource;
import com.eurodyn.qlack.fuse.aaa.util.QFAConverterUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class QFAResourceService {

  @PersistenceContext
  private EntityManager em;

  public String createResource(QFAResourceDTO resourceDTO) {
    QFAResource resource = new QFAResource();
    resource.setName(resourceDTO.getName());
    resource.setDescription(resourceDTO.getDescription());
    resource.setObjectId(resourceDTO.getObjectID());
    em.persist(resource);
    return resource.getId();
  }

  public void updateResource(QFAResourceDTO resourceDTO) {
    QFAResource resource = em.find(QFAResource.class, resourceDTO.getId());
    resource.setName(resourceDTO.getName());
    resource.setDescription(resourceDTO.getDescription());
    resource.setObjectId(resourceDTO.getObjectID());
  }

  public void deleteResource(String resourceID) {
    em.remove(QFAResource.find(resourceID, em));
  }

  public void deleteResources(Collection<String> resourceIDs) {
    for (String resourceID : resourceIDs) {
      em.remove(QFAResource.find(resourceID, em));
    }
  }

  public void deleteResourceByObjectId(String objectID) {
    em.remove(QFAResource.findByObjectID(objectID, em));
  }

  public void deleteResourcesByObjectIds(Collection<String> objectIDs) {
    for (String objectID : objectIDs) {
      em.remove(QFAResource.findByObjectID(objectID, em));
    }
  }

  public QFAResourceDTO getResourceById(String resourceID) {
    return QFAConverterUtil.resourceToResourceDTO(QFAResource.find(resourceID, em));
  }

  public QFAResourceDTO getResourceByObjectId(String objectID) {
    return QFAConverterUtil.resourceToResourceDTO(QFAResource.findByObjectID(
        objectID, em));
  }

}
