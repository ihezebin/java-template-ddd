package com.hezebin.template.application.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hezebin.template.domain.entity.Example;
import com.hezebin.template.domain.repository.ExampleRepository;

@Service
public class ExampleService {

    private final ExampleRepository exampleEs;

    private final ExampleRepository exampleMapper;

    public ExampleService(
            @Qualifier("exampleEs") ExampleRepository exampleEs,
            @Qualifier("exampleMapper") ExampleRepository exampleMapper) {
        this.exampleEs = exampleEs;
        this.exampleMapper = exampleMapper;
    }

    public Example insert(Example example) {
        // 生成 id
        example.setId(UUID.randomUUID().toString());
        // 生成随机 salt
        String salt = UUID.randomUUID().toString().substring(0, 8);
        example.setSalt(salt);
        exampleMapper.insertOne(example);
        return example;
    }

    public Example findOne(String username) {
        return exampleMapper.findByUsername(username);
    }

    public Example findEs(String username) {
        return exampleEs.findByUsername(username);
    }
}
