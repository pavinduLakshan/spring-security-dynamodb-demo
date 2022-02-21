package com.pavindulakshan.ssdynamodb.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pavindulakshan.ssdynamodb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

class CheckUserResponse {
    User user;
    boolean isPresent;

    public CheckUserResponse(User user, boolean isPresent){
        this.user = user;
        this.isPresent = isPresent;
    }
}

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public User save(User user){
        dynamoDBMapper.save(user);
        return user;
    }

    public Optional<User> findByUsernameOrEmail(String username, String email){
        CheckUserResponse duserResp = this.checkUser(email);
        return Optional.ofNullable(duserResp.user);
    }

    public Boolean existsByUsername(String username){
        CheckUserResponse duserResp = this.checkUser(username);
        return duserResp.isPresent;
    }

    public Boolean existsByEmail(String email){
        CheckUserResponse duserResp = this.checkUser(email);
        return duserResp.isPresent;
    }

    public Optional<User> findById(String id){
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS("USER"));
        eav.put(":val2", new AttributeValue().withS(id));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("pk = :val1")
                .withFilterExpression("id = :val2")
                .withExpressionAttributeValues(eav);

        List<User> savedUsers = dynamoDBMapper.query(User.class, queryExpression);
        if (savedUsers.size() == 0){
            return Optional.empty();
        }
        else {
            return Optional.of(savedUsers.get(0));
        }
    }

    CheckUserResponse checkUser(String usernameOrEmail){
        User user = dynamoDBMapper.load(User.class,"USER",usernameOrEmail);
        boolean isPresent = user != null;
        return new CheckUserResponse(user,isPresent);

        //            // Username and email are same
//            Map<String,AttributeValue> eav = new HashMap<>();
//            eav.put(":pk",new AttributeValue("USER"));
//            eav.put(":sk",new AttributeValue(usernameOrEmail));
//
//            QueryRequest queryRequest = new QueryRequest()
//                    .withTableName("powerlink")
//                    .withKeyConditionExpression("pk = :pk and sk = :sk")
//                    .withExpressionAttributeValues(eav);
//            QueryResult queryResult = amazonDynamoDB.query(queryRequest);
//            if(queryResult.getCount() > 0) {
//                Map<String,AttributeValue> user = queryResult.getItems().get(0);
//                List<String> rolesList = user.get("roles").getSS();
//                Set<Role> roles = new HashSet<>();
//                for(String role:rolesList){
//                    roles.add(new Role(role));
//                }
//                User result = new User(user.get("id").getS(),user.get("name").getS(),user.get("sk").getS(),user.get("password").getS(),roles);
//                return new CheckUserResponse(result,true);
//            }
//            else {
//                return new CheckUserResponse(null,false);
//            }

    }
}
