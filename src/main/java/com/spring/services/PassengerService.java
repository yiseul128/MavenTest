package com.spring.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.models.Passenger;
import com.spring.models.Ticket;
import com.spring.repositories.PassengerRepository;
import com.spring.repositories.TicketRepository;

@Service
public class PassengerService {
    
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketRepository ticketRepository;
    
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }
    
    public Optional<Passenger> getPassengerById(String id) {
        return passengerRepository.findById(id);
    }

    public Passenger getPassengerByuserName(String userName) {
        return passengerRepository.findByuserName(userName);
    }
    
    public void createPassenger(Passenger passenger) {
        passengerRepository.save(passenger);
    }
    
    public void updatePassenger(Passenger passenger) {

        // validations to only update Passenger object if the value is different than
		// null

		passenger.setPassword(passenger.getPassword());
		passenger.setFirstName(passenger.getFirstName());
		passenger.setLastName(passenger.getLastName());
		passenger.setAddress(passenger.getAddress());
		passenger.setCity(passenger.getCity());
		passenger.setPostalCode(passenger.getPostalCode());
		passenger.setPhone(passenger.getPhone());
		passenger.setEmail(passenger.getEmail());
		passenger.setAge(passenger.getAge());

        passengerRepository.save(passenger);
    }

    public void addPassengerTerms(Passenger passenger, boolean termsAcceptance){
    System.out.println("passenger object at addPassengerTerms(): " + passenger);
    passenger.setTermsAcceptance(termsAcceptance);
    passengerRepository.save(passenger);
    }
    
    public void deletePassengerById(String id) {
        passengerRepository.deleteById(id);
    }

    public void addTicketsToPassenger(String passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId).orElse(null);

        try {
            Set<Ticket> tickets = ticketRepository.findAllByPassengerId(passengerId);

            if (passenger.getTickets() == null) {
                passenger.setTickets(new HashSet<>());
            }
            passenger.setTickets(tickets);
    
            passengerRepository.save(passenger);
        } catch (Exception e) {
            System.out.println("Something went wrong finding the tickets of the passenger.");
        }
        

        
    }
}
