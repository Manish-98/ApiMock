package one.june.apimock.repository;

import one.june.apimock.model.MockRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MockRequestRepository extends MongoRepository<MockRequest, UUID> {
}
