package com.spring.repositories;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.models.Ticket;

public interface TicketRepository extends MongoRepository <Ticket, String>{
    Set<Ticket> findAllByPassengerId(String passengerId);
    Ticket findByPaymentId(String paymentId);

}

