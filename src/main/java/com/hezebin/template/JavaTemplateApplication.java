package com.hezebin.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hezebin.template.config.CustomizeConfig;

@SpringBootApplication
@EnableConfigurationProperties(CustomizeConfig.class)
public class JavaTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaTemplateApplication.class, args);
	}

}
