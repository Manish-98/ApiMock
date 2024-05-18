package one.june.apimock.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import one.june.apimock.model.MockRequest;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
@AllArgsConstructor
public class MongoConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps(MockRequest.class);
        indexOps.ensureIndex(new CompoundIndexDefinition(new Document("path", 1).append("httpMethod", 1)).unique());
    }
}