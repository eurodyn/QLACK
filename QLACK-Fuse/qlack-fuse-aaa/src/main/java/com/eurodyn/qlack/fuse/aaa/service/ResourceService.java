package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.ResourceMapper;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class ResourceService {

//  @PersistenceContext
//  private EntityManager em;

  // Repositories
  private final ResourceRepository resourceRepository;
  private final ResourceMapper resourceMapper;

  public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
    this.resourceRepository = resourceRepository;
    this.resourceMapper = resourceMapper;
  }

  public String createResource(ResourceDTO resourceDTO) {
//    Resource resource = new Resource();
//    resource.setName(resourceDTO.getName());
//    resource.setDescription(resourceDTO.getDescription());
//    resource.setObjectId(resourceDTO.getObjectID());
//    em.persist(resource);
    Resource resource = resourceMapper.mapToEntity(resourceDTO);
    resourceRepository.save(resource);

    return resource.getId();
  }

  public void updateResource(ResourceDTO resourceDTO) {
//    Resource resource = em.find(Resource.class, resourceDTO.getId());
    Resource resource = resourceRepository.fetchById(resourceDTO.getId());
//    resource.setName(resourceDTO.getName());
//    resource.setDescription(resourceDTO.getDescription());
//    resource.setObjectId(resourceDTO.getObjectID());
    resourceMapper.mapToExistingEntity(resourceDTO, resource);
  }

  public void deleteResource(String resourceID) {
//    em.remove(Resource.find(resourceID, em));
    resourceRepository.delete(resourceRepository.fetchById(resourceID));
  }

  public void deleteResources(Collection<String> resourceIDs) {
    for (String resourceID : resourceIDs) {
//      em.remove(Resource.find(resourceID, em));
      resourceRepository.delete(resourceRepository.fetchById(resourceID));
    }
  }

  public void deleteResourceByObjectId(String objectID) {
//    em.remove(Resource.findByObjectID(objectID, em));
    resourceRepository.delete(resourceRepository.findByObjectId(objectID));
  }

  public void deleteResourcesByObjectIds(Collection<String> objectIDs) {
    for (String objectID : objectIDs) {
      resourceRepository.delete(resourceRepository.findByObjectId(objectID));
    }
  }

  public ResourceDTO getResourceById(String resourceID) {
//    return ConverterUtil.resourceToResourceDTO(Resource.find(resourceID, em));
//  return ConverterUtil.resourceToResourceDTO(resourceRepository.fetchById(resourceID));
    return resourceMapper.mapToDTO(resourceRepository.fetchById(resourceID));
  }

  public ResourceDTO getResourceByObjectId(String objectID) {
//    return ConverterUtil.resourceToResourceDTO(Resource.findByObjectID(
//        objectID, em));
    return resourceMapper.mapToDTO(resourceRepository.findByObjectId(objectID));
  }

}
