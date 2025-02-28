package com.hezebin.template.domain.repository;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hezebin.template.application.dto.ResponseBodyCode;
import com.hezebin.template.domain.entity.Example;
import com.hezebin.template.exception.ErrorException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository("exampleRedis")
@ConditionalOnProperty(prefix = "redis", name = "host", matchIfMissing = false)
public class ExampleRedis implements ExampleRepository {

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private static final String KEY_PREFIX = "example:";
    private static final String USERNAME_KEY_PREFIX = "example:username:";
    private static final String EMAIL_KEY_PREFIX = "example:email:";
    private static final int EXPIRE_TIME = 3600; // 1小时过期

    public ExampleRedis(JedisPool jedisPool, ObjectMapper objectMapper) {
        this.jedisPool = jedisPool;
        this.objectMapper = objectMapper;
    }

    @Override
    public void insertOne(Example example) throws ErrorException {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = objectMapper.writeValueAsString(example);

            // 修改pipeline使用方式
            try (var pipeline = jedis.pipelined()) {
                // 存储主数据
                pipeline.setex(KEY_PREFIX + example.getId(), EXPIRE_TIME, json);
                // 建立用户名索引
                pipeline.setex(USERNAME_KEY_PREFIX + example.getUsername(), EXPIRE_TIME, example.getId());
                // 建立邮箱索引
                pipeline.setex(EMAIL_KEY_PREFIX + example.getEmail(), EXPIRE_TIME, example.getId());
                pipeline.sync();
            }
        } catch (Exception e) {
            throw new ErrorException(ResponseBodyCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Example findByUsername(String username) throws ErrorException {
        try (Jedis jedis = jedisPool.getResource()) {
            // 先通过用户名索引获取ID
            String id = jedis.get(USERNAME_KEY_PREFIX + username);
            if (id == null) {
                return null;
            }

            // 再通过ID获取完整数据
            String json = jedis.get(KEY_PREFIX + id);
            if (json == null) {
                return null;
            }

            return objectMapper.readValue(json, Example.class);
        } catch (Exception e) {
            throw new ErrorException(ResponseBodyCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Example findByEmail(String email) throws ErrorException {
        try (Jedis jedis = jedisPool.getResource()) {
            String id = jedis.get(EMAIL_KEY_PREFIX + email);
            if (id == null) {
                return null;
            }

            String json = jedis.get(KEY_PREFIX + id);
            if (json == null) {
                return null;
            }

            return objectMapper.readValue(json, Example.class);
        } catch (Exception e) {
            throw new ErrorException(ResponseBodyCode.INTERNAL_SERVER_ERROR, e);
        }
    }
}