# QLACK Settings module

This module is responsible for storing and configuring internal settings of the application.

## Integration

### Add qlack-fuse-settings dependency to your pom.xml:

```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-settings</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add the packages in the Spring boot application main class declaration:

```java
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.settings.repository")
@EntityScan("com.eurodyn.qlack.fuse.settings.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.settings"
})
```

### Example

```java

import com.eurodyn.qlack.fuse.settings.SettingsService;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
// ..

    @Autowired
    private SettingsService settingsService;
// ..

    public void run(String... args) {
        SettingDTO setting = settingsService.getSetting("TheOwner", "SomeKey", "SomeGroup");
        List<SettingDTO> groupSettings = settingsService.getGroupSettings("TheOwner", "SomeGroup");
    }
}
```
