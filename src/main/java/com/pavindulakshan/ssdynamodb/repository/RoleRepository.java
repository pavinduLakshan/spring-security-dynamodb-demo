package com.pavindulakshan.ssdynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.pavindulakshan.ssdynamodb.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class RoleRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Role save(Role role){
        dynamoDBMapper.save(role);
        return role;
    }

    public Optional<Role> findByName(String roleName){
        Role role = dynamoDBMapper.load(Role.class, "ROLE",roleName);
        return Optional.ofNullable(role);
    }
}
