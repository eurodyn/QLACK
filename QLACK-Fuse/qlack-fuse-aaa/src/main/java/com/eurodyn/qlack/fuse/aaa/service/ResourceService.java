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
public interface ResourceService {

    String createResource(ResourceDTO resourceDTO);

    void updateResource(ResourceDTO resourceDTO);

    void deleteResource(String resourceID);

    void deleteResources(Collection<String> resourceIDs);

    void deleteResourceByObjectId(String objectID);

    void deleteResourcesByObjectIds(Collection<String> objectIDs);

    ResourceDTO getResourceById(String resourceID);

    ResourceDTO getResourceByObjectId(String objectID);

}
