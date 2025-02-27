package com.hezebin.template.domain.repository;

import com.hezebin.template.domain.entity.Example;

public interface ExampleRepository {
    public void insertOne(Example example);

    public Example findByUsername(String username);

    public Example findByEmail(String email);
}
