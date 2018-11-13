package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;

/**
 * @author European Dynamics SA
 */
public interface OpTemplateService {

    String createTemplate(OpTemplateDTO templateDTO);

    void deleteTemplateByID(String templateID);

    void deleteTemplateByName(String templateName);

    OpTemplateDTO getTemplateByID(String templateID);

    OpTemplateDTO getTemplateByName(String templateName);

    void addOperation(
        String templateID, String operationName,
        boolean isDeny);

    void addOperation(
        String templateID, String operationName,
        String resourceID, boolean isDeny);

    void removeOperation(String templateID, String operationName);

    void removeOperation(
        String templateID, String operationName,
        String resourceID);

    Boolean getOperationAccess(String templateID, String operationName);

    Boolean getOperationAccess(
        String templateID, String operationName,
        String resourceID);

    boolean updateTemplate(OpTemplateDTO templateDTO);
}
