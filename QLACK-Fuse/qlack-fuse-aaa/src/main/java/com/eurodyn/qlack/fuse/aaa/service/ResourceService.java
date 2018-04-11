package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.util.ConverterUtil;
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
public class ResourceService {

  @PersistenceContext
  private EntityManager em;

  public String createResource(ResourceDTO resourceDTO) {
    Resource resource = new Resource();
    resource.setName(resourceDTO.getName());
    resource.setDescription(resourceDTO.getDescription());
    resource.setObjectId(resourceDTO.getObjectID());
    em.persist(resource);
    return resource.getId();
  }

  public void updateResource(ResourceDTO resourceDTO) {
    Resource resource = em.find(Resource.class, resourceDTO.getId());
    resource.setName(resourceDTO.getName());
    resource.setDescription(resourceDTO.getDescription());
    resource.setObjectId(resourceDTO.getObjectID());
  }

  public void deleteResource(String resourceID) {
    em.remove(Resource.find(resourceID, em));
  }

  public void deleteResources(Collection<String> resourceIDs) {
    for (String resourceID : resourceIDs) {
      em.remove(Resource.find(resourceID, em));
    }
  }

  public void deleteResourceByObjectId(String objectID) {
    em.remove(Resource.findByObjectID(objectID, em));
  }

  public void deleteResourcesByObjectIds(Collection<String> objectIDs) {
    for (String objectID : objectIDs) {
      em.remove(Resource.findByObjectID(objectID, em));
    }
  }

  public ResourceDTO getResourceById(String resourceID) {
    return ConverterUtil.resourceToResourceDTO(Resource.find(resourceID, em));
  }

  public ResourceDTO getResourceByObjectId(String objectID) {
    return ConverterUtil.resourceToResourceDTO(Resource.findByObjectID(
        objectID, em));
  }

}
