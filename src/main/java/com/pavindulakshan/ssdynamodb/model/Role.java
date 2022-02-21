package com.pavindulakshan.ssdynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBTable(tableName = "powerlink")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @DynamoDBHashKey(attributeName = "pk")
    private String pk = "ROLE";

    @DynamoDBRangeKey(attributeName = "sk")
    private String roleName;

    public Role(String name) {
        this.roleName = name;
    }
}