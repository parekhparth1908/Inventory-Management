package com.example.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "products")
public class Product extends PanacheMongoEntity {
    public String name;
    public double price;
    public int quantity;
}