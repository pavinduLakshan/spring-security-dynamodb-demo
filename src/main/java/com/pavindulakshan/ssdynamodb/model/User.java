package com.pavindulakshan.ssdynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.pavindulakshan.ssdynamodb.model.converter.RoleConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "powerlink")
public class User {
    @DynamoDBAttribute(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "name")
    private String name;

    @DynamoDBRangeKey(attributeName = "sk")
    private String email;

    @DynamoDBAttribute(attributeName = "username")
    private String username;

    @DynamoDBAttribute(attributeName = "password")
    private String password;

    @DynamoDBAttribute(attributeName = "roles")
    @DynamoDBTypeConverted(converter = RoleConverter.class)
    private Set<Role> roles = new HashSet<>();

    @DynamoDBHashKey(attributeName = "pk")
    private String pk = "USER";

    public User(String name, String email, String password){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.username = email;
        this.password = password;
    }
}