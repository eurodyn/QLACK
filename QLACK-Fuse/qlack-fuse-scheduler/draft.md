```
@Component
public class Test implements Job {

  private static final String jobName = "j1";
  private static final String jobGroup = "g1";
  private static final String triggerName = "t1";

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    System.out.println("Hello!");
  }

  @Bean(name = "jobWithSimpleTriggerBean")
  public JobDetailFactoryBean sampleJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(Test.class);
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean(name = "jobWithSimpleTriggerBeanTrigger")
  public SimpleTriggerFactoryBean sampleJobTrigger(@Qualifier("jobWithSimpleTriggerBean") JobDetail jobDetail) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setStartDelay(0L);
    factoryBean.setRepeatInterval(1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }
}
```

```

```