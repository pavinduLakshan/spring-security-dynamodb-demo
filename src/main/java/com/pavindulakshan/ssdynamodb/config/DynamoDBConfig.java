package com.pavindulakshan.ssdynamodb.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class DynamoDBConfig {

    @Value("${amazon.region}")
    private String region;

    @Value("${dynamodb.endpoint.url}")
    private String dynamoDBEndpoint;

    private AmazonDynamoDB amazonDynamoDB;

    @Bean("amazonDynamoDB")
    public AmazonDynamoDB amazonDynamoDB() {

        amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDBEndpoint, region))
                .build();
        return amazonDynamoDB;
    }

    @Bean
    @DependsOn({"amazonDynamoDB"})
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB);
    }
}
