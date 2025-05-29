package com.example.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "categories")
public class Category extends PanacheMongoEntity {
    public String name;
    public String description;
}