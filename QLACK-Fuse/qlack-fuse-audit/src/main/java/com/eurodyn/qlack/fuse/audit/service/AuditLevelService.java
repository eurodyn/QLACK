package com.eurodyn.qlack.fuse.audit.service;


import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.util.ConverterUtil;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Transactional
@Validated
@Service
public class AuditLevelService {

  private static final Logger LOGGER = Logger
    .getLogger(AuditLevelService.class.getSimpleName());

  // Reference to the persistence context.
  @PersistenceContext
  private EntityManager em;

  public String addLevel(AuditLevelDTO level) {
    LOGGER.log(Level.FINER, "Adding custom Audit level ''{0}''.", level);
    AuditLevel alLevel = ConverterUtil.convertToAuditLevelModel(level);
    alLevel.setCreatedOn(System.currentTimeMillis());
    em.persist(alLevel);
    return alLevel.getId();
  }

  public String addLevelIfNotExists(AuditLevelDTO level) {
    if (listAuditLevels().stream().filter(o -> o.getName().equals(level.getName())).count() == 0) {
      return addLevel(level);
    } else {
      return null;
    }
  }

  public void deleteLevelById(String levelId) {
    LOGGER.log(Level.FINER, "Deleting Audit level with id ''{0}''.",
      levelId);
    em.remove(em.find(AuditLevel.class, levelId));
  }

  public void deleteLevelByName(String levelName) {
    LOGGER.log(Level.FINER, "Deleting Audit level with name ''{0}''.",
      levelName);
    em.remove(AuditLevel.findByName(em, levelName));
  }

  public void updateLevel(AuditLevelDTO level) {
    LOGGER.log(Level.FINER, "Updating Audit level ''{0}'',", level);
    AuditLevel lev = ConverterUtil.convertToAuditLevelModel(level);
    em.merge(lev);
    clearAuditLevelCache();
  }

  public AuditLevelDTO getAuditLevelByName(String levelName) {
    LOGGER.log(Level.FINER, "Searching Audit level by name ''{0}''.",
      levelName);
    return ConverterUtil.convertToAuditLevelDTO(AuditLevel.findByName(em, levelName));
  }

  public void clearAuditLevelCache() {
    AuditLevel.clearCache();
  }

  public List<AuditLevelDTO> listAuditLevels() {
    LOGGER.log(Level.FINER, "Retrieving all audit levels");
    return ConverterUtil.convertToAuditLevelList(AuditLevel.findAll(em));
  }

}