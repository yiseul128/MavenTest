package com.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.models.Passenger;

public interface PassengerRepository extends MongoRepository <Passenger, String>{

	Passenger findByUserNameAndPassword(String userName, String password);
	Passenger findByuserName(String userName);
}
