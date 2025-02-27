package com.hezebin.template.domain.repository;

import org.springframework.stereotype.Repository;

import com.hezebin.template.domain.entity.Example;

@Repository("exampleEs")
public class ExampleEs implements ExampleRepository {

    public void insertOne(Example example) {
        throw new UnsupportedOperationException("Unimplemented method 'insertOne'");
    }

    public Example findByUsername(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    public Example findByEmail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

}
