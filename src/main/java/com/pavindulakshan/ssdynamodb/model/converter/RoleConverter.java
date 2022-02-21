package com.pavindulakshan.ssdynamodb.model.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.pavindulakshan.ssdynamodb.model.Role;

import java.util.HashSet;
import java.util.Set;

public class RoleConverter implements DynamoDBTypeConverter<Set<String>, Set<Role>> {

    @Override
    public Set<String> convert(Set<Role> roles) {
        Set<String> roleStrArr = new HashSet<String>();

        if (roles != null) {
            roles.stream().forEach(e -> roleStrArr.add(e.getRoleName().toString()));
        }
        return roleStrArr;
    }

    @Override
    public Set<Role> unconvert(Set<String> roleStrArr) {
        Set<Role> result = new HashSet<>();
        if (roleStrArr != null) {
            roleStrArr.stream().forEach(e -> result.add(new Role(e)));
        }
        return result;
    }
}
