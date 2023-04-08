package com.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.models.Payment;
import com.spring.models.Ticket;
import com.spring.repositories.PaymentRepository;
import com.spring.repositories.TicketRepository;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired TicketRepository ticketRepository;

    public Ticket findTicketByPaymentId(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        Ticket dbTicket = ticketRepository.findById(payment.getTicket().getTicketNo()).orElse(null);

        return dbTicket;
    }

    public Payment addPayment(Ticket ticket, Payment payment) {

        payment.setStatus("Approved");

        payment.setTicket(ticket);
        
        return paymentRepository.save(payment);
    }
    
}
