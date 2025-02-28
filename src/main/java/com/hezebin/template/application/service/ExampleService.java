package com.hezebin.template.application.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hezebin.template.domain.entity.Example;
import com.hezebin.template.domain.repository.ExampleRepository;
import com.hezebin.template.exception.ErrorException;

@Service
public class ExampleService {

    @Autowired(required = false)
    @Qualifier("exampleEs")
    private ExampleRepository exampleEs;

    @Autowired(required = false)
    @Qualifier("exampleMapper")
    private ExampleRepository exampleMapper;

    @Autowired(required = false)
    @Qualifier("exampleMongo")
    private ExampleRepository exampleMongo;

    @Autowired(required = false)
    @Qualifier("exampleRedis")
    private ExampleRepository exampleRedis;

    public Example insert(Example example) throws ErrorException {
        // 生成 id
        example.setId(UUID.randomUUID().toString());
        // 生成随机 salt
        String salt = UUID.randomUUID().toString().substring(0, 8);
        example.setSalt(salt);

        // 根据是否启用来调用对应的实现
        if (exampleMapper != null) {
            exampleMapper.insertOne(example);
        }
        if (exampleEs != null) {
            exampleEs.insertOne(example);
        }
        if (exampleMongo != null) {
            exampleMongo.insertOne(example);
        }
        if (exampleRedis != null) {
            exampleRedis.insertOne(example);
        }
        return example;
    }

    public Example findOne(String username) throws ErrorException {
        return exampleMapper != null ? exampleMapper.findByUsername(username) : null;
    }

    public Example findEs(String username) throws ErrorException {
        return exampleEs != null ? exampleEs.findByUsername(username) : null;
    }

    public Example findMongo(String username) throws ErrorException {
        return exampleMongo != null ? exampleMongo.findByUsername(username) : null;
    }

    public Example findRedis(String username) throws ErrorException {
        return exampleRedis != null ? exampleRedis.findByUsername(username) : null;
    }
}
