package com.eurodyn.qlack.fuse.scheduler.service;

import com.eurodyn.qlack.fuse.scheduler.dto.JobDTO;
import com.eurodyn.qlack.fuse.scheduler.exception.QSchedulerException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SchedulerService {

  // Logger reference.
  private static final Logger LOGGER = Logger.getLogger(SchedulerService.class.getName());

  // Reference to Quartz scheduler.
  private Scheduler scheduler;

  @Autowired
  public SchedulerService(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  /**
   * A helper method to define the name of a job based on the class implementing the job.
   *
   * @param jobClass The class implementing the job.
   * @return Returns the name of the job.
   */
  public static <J extends Job> String getJobName(Class<J> jobClass) {
    return jobClass.getName();
  }

  /**
   * A helper method to define the name of a trigger based on the class implementing the job.
   *
   * @param jobClass The class implementing the job.
   * @return Returns the name of the trigger.
   */
  public static <J extends Job> String getTriggerName(Class<J> jobClass) {
    return jobClass.getName() + "_trigger";
  }

  public String getSchedulerName() throws QSchedulerException {
    try {
      return scheduler.getSchedulerName();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public String getSchedulerInstanceID() throws QSchedulerException {
    try {
      return scheduler.getSchedulerInstanceId();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public void start() throws QSchedulerException {
    try {
      scheduler.start();
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  public void shutdown() throws QSchedulerException {
    try {
      scheduler.shutdown();
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  public void standby() throws QSchedulerException {
    try {
      scheduler.standby();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public boolean isStarted() throws QSchedulerException {
    try {
      return scheduler.isStarted();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public boolean isShutdown() throws QSchedulerException {
    try {
      return scheduler.isShutdown();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public boolean isInStandbyMode() throws QSchedulerException {
    try {
      return scheduler.isInStandbyMode();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public <J extends Job> void registerJob(Class<J> jobClass) {
    registerJob(jobClass, null);
  }

  public <J extends Job> void registerJob(Class<J> jobClass, Map<String, Object> jobData) {
    try {
      // Check if the job is already registered and ignore the request.
      if (scheduler.getJobDetail(JobKey.jobKey(getJobName(jobClass))) != null) {
        LOGGER.log(Level.CONFIG, "Job {0} is already registered.", getJobName(jobClass));
      } else {
        scheduler.addJob(buildJob(jobClass, jobData), true);
      }
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  public <J extends Job> void scheduleJob(Class<J> jobClass, String cronExpression,
      Map<String, Object> jobData) {
    try {
      // Check if the job is already registered, in that case only reschedule it.
      if (scheduler.getJobDetail(JobKey.jobKey(getJobName(jobClass))) != null) {
        LOGGER.log(Level.CONFIG, "Job {0} is already registered, rescheduling it.", getJobName(jobClass));
        final Trigger oldTrigger = scheduler.getTrigger(TriggerKey.triggerKey(getTriggerName(jobClass)));
        final CronTrigger newTrigger = buildTrigger(jobClass, cronExpression);
        scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
      } else {
        scheduler.scheduleJob(buildJob(jobClass, jobData), buildTrigger(jobClass, cronExpression));
      }
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  public <J extends Job> void scheduleJob(Class<J> jobClass, String cronExpression)
      throws QSchedulerException {
    scheduleJob(jobClass, cronExpression, null);
  }

  public <J extends Job> void rescheduleJob(Class<J> jobClass, String cronExpression)
      throws QSchedulerException {
    try {
      scheduler.rescheduleJob(TriggerKey.triggerKey(getTriggerName(jobClass)), buildTrigger(jobClass, cronExpression));
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
  }

  private <J extends Job> JobDetail buildJob(Class<J> jobClass, Map<String, Object> jobData) {
    JobDataMap jobDataMap = new JobDataMap();
    if (jobData != null) {
      jobDataMap.putAll(jobData);
    }
    return JobBuilder
        .newJob(jobClass)
        .withIdentity(getJobName(jobClass))
        .storeDurably()
        .setJobData(jobDataMap)
        .build();
  }

  private <J extends Job> CronTrigger buildTrigger(Class<J> jobClass, String cronExpression) {
    return TriggerBuilder
        .newTrigger()
        .forJob(jobClass.getName())
        .withIdentity(getTriggerName(jobClass))
        .startNow()
        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
        .build();
  }

  public boolean deleteJob(String jobName) throws QSchedulerException {
    try {
      return scheduler.deleteJob(JobKey.jobKey(jobName));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public boolean deleteJobs(List<String> jobNames) {
    boolean deletedAll = true;
    for (String jobName : jobNames) {
      deletedAll = deletedAll && deleteJob(jobName);
    }
    return deletedAll;
  }

  public <J extends Job> void triggerJob(Class<J> jobClass) throws QSchedulerException {
    triggerJob(jobClass, null);
  }

  public <J extends Job> void triggerJob(Class<J> jobClass, Map<String, Object> jobData) throws QSchedulerException {
    try {
      JobDataMap jobDataMap = new JobDataMap();
      if (jobData != null) {
        jobDataMap.putAll(jobData);
      }
      scheduler.triggerJob(JobKey.jobKey(getJobName(jobClass)), jobDataMap);
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public <J extends Job> void pauseJob(Class<J> jobClass) throws QSchedulerException {
    try {
      scheduler.pauseJob(JobKey.jobKey(getJobName(jobClass)));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public void pauseAllTriggers() throws QSchedulerException {
    try {
      scheduler.pauseAll();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public void resumeAllTriggers() throws QSchedulerException {
    try {
      scheduler.resumeAll();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public void clear() throws QSchedulerException {
    try {
      scheduler.clear();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public <J extends Job> Date getNextFireForJob(Class<J> jobClass) throws QSchedulerException {
    try {
      return scheduler.getTrigger(TriggerKey.triggerKey(getTriggerName(jobClass))).getNextFireTime();
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public <J extends Job> boolean isJobScheduled(Class<J> jobClass) {
    try {
      return scheduler.checkExists(JobKey.jobKey(getJobName(jobClass)));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public <J extends Job> boolean isTriggerExisting(Class<J> jobClass) {
    try {
      return scheduler.checkExists(TriggerKey.triggerKey(getTriggerName(jobClass)));
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public List<String> getCurrentlyExecutingJobsNames() {
    try {
      List<JobExecutionContext> runningJobs = scheduler.getCurrentlyExecutingJobs();
      List<String> names = new ArrayList<>();

      for (JobExecutionContext runningJob : runningJobs) {
        names.add(runningJob.getJobDetail().getKey().getName());
      }
      return names;
    } catch (SchedulerException ex) {
      throw new QSchedulerException(ex);
    }
  }

  public List<JobDTO> getJobInfo() {
    List<JobDTO> jobs = new ArrayList<>();

    try {
      for (String groupName : scheduler.getJobGroupNames()) {
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
          List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
          Date nextFireTime = triggers.get(0).getNextFireTime();

          JobDTO jobDTO = new JobDTO(jobKey.getName(), jobKey.getGroup(), nextFireTime);
          jobs.add(jobDTO);
        }
      }
    } catch (SchedulerException e) {
      throw new QSchedulerException(e);
    }
    return jobs;
  }
}
