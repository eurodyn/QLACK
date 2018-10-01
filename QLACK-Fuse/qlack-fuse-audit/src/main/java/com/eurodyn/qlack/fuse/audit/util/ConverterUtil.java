package com.eurodyn.qlack.fuse.audit.util;


import java.util.logging.Logger;

/**
 * This is utility class used to convert Audit related models/list data to DTO
 * and vice-a-versa.
 */
public final class ConverterUtil {

  private static final Logger LOGGER = Logger.getLogger(ConverterUtil.class
      .getSimpleName());

  /**
   * Convert AuditLevelDTO DTO to AlAuditLevel Model.
   *
   * @return the AlAuditLevel
   */
//  public static AuditLevel convertToAuditLevelModel(AuditLevelDTO level) {
//    LOGGER.log(Level.FINEST,
//        "Converting audit level DTO to audit level ''{0}''.", level);
//    AuditLevel alLevel = null;
//    if (null != level) {
//      alLevel = new AuditLevel();
//      if (StringUtils.isBlank(level.getId())) {
//        alLevel.setId(UUID.randomUUID().toString());
//      } else {
//        alLevel.setId(level.getId());
//      }
//      alLevel.setName(level.getName());
//      alLevel.setDescription(level.getDescription());
//      alLevel.setPrinSessionId(level.getPrinSessionId());
//      if (level.getCreatedOn() != null) {
//        alLevel.setCreatedOn(level.getCreatedOn().getTime());
//      }
//    }
//    return alLevel;
//  }

  /**
   * Convert AlAuditLevel Model to AuditLevelDTO DTO
   *
   * @return AuditLevelDTO
   */
//  public static AuditLevelDTO convertToAuditLevelDTO(AuditLevel alLevel) {
//    LOGGER.log(Level.FINEST,
//        "Converting audit level model to audit level ''{0}''.", alLevel);
//    AuditLevelDTO level = null;
//    if (null != alLevel) {
//      level = new AuditLevelDTO();
//      level.setId(alLevel.getId());
//      level.setName(alLevel.getName());
//      level.setDescription(alLevel.getDescription());
//      if (alLevel.getCreatedOn() != null) {
//        level.setCreatedOn(new Date(alLevel.getCreatedOn()));
//      }
//      level.setPrinSessionId(alLevel.getPrinSessionId());
//    }
//    return level;
//  }

  /**
   * Convert AuditLogDTO DTO to AlAudit model
   *
   * @return Audit
   */
//  public static Audit convertToAuditLogModel(AuditLogDTO log) {
//    LOGGER.log(Level.FINEST,
//        "Converting audit log DTO to audit log ''{0}''.", log);
//    Audit alLog = null;
//    if (null != log) {
//      alLog = new Audit();
//      alLog.setTraceId(getAuditTraceObject(log));
//      if (null != log.getCreatedOn()) {
//        alLog.setCreatedOn(log.getCreatedOn().getTime());
//      }
//      alLog.setEvent(log.getEvent());
//      alLog.setPrinSessionId(log.getPrinSessionId());
//      alLog.setShortDescription(log.getShortDescription());
//      alLog.setLevelId(ConverterUtil.convertToAuditLevelModel(null == log
//          .getLevel() ? null : new AuditLevelDTO(log.getLevel())));
//      alLog.setReferenceId(log.getReferenceId());
//      alLog.setGroupName(log.getGroupName());
//      alLog.setOpt1(log.getOpt1());
//      alLog.setOpt2(log.getOpt2());
//      alLog.setOpt3(log.getOpt3());
//    }
//    return alLog;
//  }

  /**
   * Convert AlAudit model to AuditLogDTO DTO
   *
   * @return AuditLogDTO
   */
//  public static AuditLogDTO convertToAuditLogDTO(Audit log, boolean setTraceData) {
//    LOGGER.log(Level.FINEST,
//        "Converting audit level model to audit ''{0}''.", log);
//    AuditLogDTO alLog = null;
//    if (null != log) {
//      alLog = new AuditLogDTO();
//      if (setTraceData && null != log.getTraceId()) {
//        alLog.setTraceData(log.getTraceId().getTraceData());
//      }
//      if (null != log.getCreatedOn()) {
//        alLog.setCreatedOn(new Date(log.getCreatedOn()));
//      }
//      alLog.setId(log.getId());
//      alLog.setEvent(log.getEvent());
//      alLog.setPrinSessionId(log.getPrinSessionId());
//      alLog.setShortDescription(log.getShortDescription());
//      alLog.setLevel(log.getLevelId().getName());
//      alLog.setReferenceId(log.getReferenceId());
//      alLog.setGroupName(log.getGroupName());
//      alLog.setOpt1(log.getOpt1());
//      alLog.setOpt2(log.getOpt2());
//      alLog.setOpt3(log.getOpt3());
//    }
//    return alLog;
//  }

//  public static AuditLogDTO convertToAuditLogDTO(Audit log) {
//    return ConverterUtil.convertToAuditLogDTO(log, true);
//  }

  /**
   * Get AuditLogDTO List from AlAudit List
   *
   * @param setTraceData Set to false if trace data should be ignored
   * @return List
   */
//  public static List<AuditLogDTO> convertToAuditLogList(List<Audit> list, boolean setTraceData) {
//    LOGGER.log(Level.FINEST,
//        "Converting audit log model list to audit log DTO list.");
//    if (list == null) {
//      return null;
//    }
//    List<AuditLogDTO> aList = new ArrayList<>(list.size());
//    for (int i = 0; i < list.size(); i++) {
//      Audit auditLog = list.get(i);
//      aList.add(ConverterUtil.convertToAuditLogDTO(auditLog, setTraceData));
//    }
//    return aList;
//  }

//  public static List<AuditLogDTO> convertToAuditLogList(List<Audit> list) {
//    return ConverterUtil.convertToAuditLogList(list, true);
//  }

  /**
   * Get AuditLevelDTO List from AlAuditLevel List
   *
   * @return List
   */
//  public static List<AuditLevelDTO> convertToAuditLevelList(
//      List<AuditLevel> list) {
//    LOGGER.log(
//        Level.FINEST,
//        "Converting audit level model list to audit level DTO list ''{0}''.",
//        list);
//    if (list == null) {
//      return null;
//    }
//    List<AuditLevelDTO> aList = new ArrayList<>(list.size());
//    for (int i = 0; i < list.size(); i++) {
//      AuditLevel auditLog = list.get(i);
//      aList.add(ConverterUtil.convertToAuditLevelDTO(auditLog));
//    }
//    return aList;
//  }

//  private static AuditTrace getAuditTraceObject(AuditLogDTO log) {
//    AuditTrace trace = null;
//    String obj = log.getTraceData();
//    if (null != obj) {
//      trace = new AuditTrace();
//      trace.setTraceData(log.getTraceData());
//    }
//    LOGGER.log(Level.FINEST, "Trace ''{0}''.", log);
//    return trace;
//  }
}
