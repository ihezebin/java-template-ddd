package com.hezebin.template.domain.repository;

import org.springframework.stereotype.Repository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.hezebin.template.domain.entity.Example;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
@Repository("exampleMongo")
public class ExampleMongo implements ExampleRepository {

    private final MongoClient mongoClient;
    private static final String DATABASE_NAME = "java-template-ddd";
    private static final String COLLECTION_NAME = "example";

    public ExampleMongo(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
    }

    @Override
    public void insertOne(Example example) {
        try {
            Document doc = new Document()
                    .append("_id", example.getId())
                    .append("username", example.getUsername())
                    .append("password", example.getPassword())
                    .append("email", example.getEmail())
                    .append("salt", example.getSalt());

            // upsert: true 表示如果文档不存在则插入
            getCollection().replaceOne(
                    Filters.eq("_id", example.getId()),
                    doc,
                    new ReplaceOptions().upsert(true));
        } catch (Exception e) {
            log.error("MongoDB插入文档失败: ", e);
            throw new RuntimeException("MongoDB插入文档失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Example findByUsername(String username) {
        try {
            Document doc = getCollection().find(Filters.eq("username", username)).first();
            return documentToExample(doc);
        } catch (Exception e) {
            log.error("MongoDB查询失败: ", e);
            throw new RuntimeException("MongoDB查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Example findByEmail(String email) {
        try {
            Document doc = getCollection().find(Filters.eq("email", email)).first();
            return documentToExample(doc);
        } catch (Exception e) {
            log.error("MongoDB查询失败: ", e);
            throw new RuntimeException("MongoDB查询失败: " + e.getMessage(), e);
        }
    }

    private Example documentToExample(Document doc) {
        if (doc == null) {
            return null;
        }
        Example example = new Example();
        example.setId(doc.getString("_id"));
        example.setUsername(doc.getString("username"));
        example.setPassword(doc.getString("password"));
        example.setEmail(doc.getString("email"));
        example.setSalt(doc.getString("salt"));
        return example;
    }
}