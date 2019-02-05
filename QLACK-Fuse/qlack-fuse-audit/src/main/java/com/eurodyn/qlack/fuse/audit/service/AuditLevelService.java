package com.eurodyn.qlack.fuse.audit.service;


import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.exceptions.AlreadyExistsException;
import com.eurodyn.qlack.fuse.audit.mappers.AuditLevelMapper;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Transactional
@Validated
@Service
public class AuditLevelService {

  private static final Logger LOGGER = Logger
    .getLogger(AuditLevelService.class.getSimpleName());

  private final AuditLevelRepository auditLevelRepository;

  private final AuditLevelMapper mapper;

  public AuditLevelService(AuditLevelRepository auditLevelRepository, AuditLevelMapper mapper) {
    this.auditLevelRepository = auditLevelRepository;
    this.mapper = mapper;
  }

  public String addLevel(AuditLevelDTO level) {
    LOGGER.log(Level.FINER, "Adding custom Audit level ''{0}''.", level);
    AuditLevel alLevel = mapper.mapToEntity(level);
    alLevel.setCreatedOn(System.currentTimeMillis());
    auditLevelRepository.save(alLevel);
    return alLevel.getId();
  }

  public String addLevelIfNotExists(AuditLevelDTO level) throws AlreadyExistsException {
    if (listAuditLevels().stream().filter(o -> o.getName().equals(level.getName())).count() == 0) {
      return addLevel(level);
    } else {
      throw new AlreadyExistsException("Level: " + level.getName() + " already exists and will not be added.");
    }
  }

  public void deleteLevelById(String levelId) {
    LOGGER.log(Level.FINER, "Deleting Audit level with id ''{0}''.",
      levelId);
    auditLevelRepository.delete(auditLevelRepository.fetchById(levelId));
  }

  public void deleteLevelByName(String levelName) {
    LOGGER.log(Level.FINER, "Deleting Audit level with name ''{0}''.",
      levelName);
    auditLevelRepository.delete(auditLevelRepository.findByName(levelName));
  }

  public void updateLevel(AuditLevelDTO level) {
    LOGGER.log(Level.FINER, "Updating Audit level ''{0}'',", level);
    AuditLevel lev = mapper.mapToEntity(level);
    auditLevelRepository.save(lev);
    clearAuditLevelCache();
  }

  public AuditLevelDTO getAuditLevelByName(String levelName) {
    LOGGER.log(Level.FINER, "Searching Audit level by name ''{0}''.",
      levelName);
    return mapper.mapToDTO(auditLevelRepository.findByName(levelName));
  }

  public void clearAuditLevelCache() {
    AuditLevel.clearCache();
  }

  public List<AuditLevelDTO> listAuditLevels() {
    LOGGER.log(Level.FINER, "Retrieving all audit levels");
    return mapper.mapToDTO(auditLevelRepository.findAll());
  }

}