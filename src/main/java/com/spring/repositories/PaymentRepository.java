package com.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.models.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String>{
    
}
