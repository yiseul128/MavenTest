package com.spring.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.models.Journey;

public interface JourneyRepository extends MongoRepository <Journey, String>{
    
}




