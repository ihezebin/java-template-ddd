package com.hezebin.template.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "customize")
@Data
public class CustomizeConfig {
    private String a;
    private String b;
}
