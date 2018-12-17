package com.eurodyn.qlack.util.swagger.config;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * This class is responsible for corfiguring the Swagger tool.
 * The configuration values can be set in swagger-config.properties file.
 * Swagger will automatically scan your Spring boot application and document the API, styling it with Swagger UI.
 * The created Swagger Ui documentation will be accessible under:
 * /context-path/api-docs?url=/context-path/swagger.json
 */
@Configuration
@PropertySource(value = "classpath:swagger-config.properties")
public class Swagger2Config {

    @Value("${title}")
    private String title;

    @Value("${description}")
    private String description;

    @Value("${version}")
    private String version;

    @Value("${schemes}")
    private String schemes[];

    @Value("${license}")
    private String license;

    @Value("${licenseUrl}")
    private String licenseUrl;

    /**
     * Configures a Swagger UI feature which automatically scans the project for APIs and creates a Swagger Ui page.
     *
     * @return feature
     */
    @Bean
    public Swagger2Feature swagger2Feature() {
        Swagger2Feature feature = new Swagger2Feature();
        feature.setTitle(title);
        feature.setDescription(description);
        feature.setVersion(version);
        feature.setSchemes(schemes);
        feature.setPrettyPrint(true);
        feature.setLicense(license);
        feature.setLicenseUrl(licenseUrl);
        feature.setScanAllResources(true);
        return feature;
    }
}
