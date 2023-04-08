package com.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.models.Journey;
import com.spring.models.Passenger;
import com.spring.models.Payment;
import com.spring.models.Ticket;
import com.spring.services.JourneyService;
import com.spring.services.PassengerService;
import com.spring.services.PaymentService;
import com.spring.services.TicketService;
import com.spring.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class TicketController {

	@Autowired
	private TicketService ticketService;

	@Autowired
	private JourneyService journeyService;

	@Autowired
	private PassengerService passengerService;

	@Autowired
	private PaymentService paymentService;

	@GetMapping("/payment/{journeyId}")
	public String getPaymentPage(@PathVariable String journeyId, Payment payment, Model model) {

		// block to check if nullability of journey.
		try {
			Journey journey = journeyService.getJourneyById(journeyId).orElse(null);

			// debug
			System.out.println("inside GET /payment");
			System.out.println("Price from journey session GET /payment: " + journey.getTicketPrice());

			double ticketPrice = journey.getTicketPrice();

			// passing the journey calculated attribute to the payment object to display it
			// in the view
			payment.setTotalFare(ticketPrice);

			model.addAttribute("payment", payment);
			// adding journey Id to the model to access it in the post method of payment
			model.addAttribute("journeyId", journeyId);

		} catch (Exception e) {
			System.out.println("Journey Object was found null, returning to /booking. Error: " + e.getMessage());
			return "redirect:/booking";
		}

		return "payment";
	}

	@PostMapping("/payment")
	public String processPayment(@RequestParam("journeyId") String journeyId, @Valid @ModelAttribute("payment") Payment payment,
			BindingResult result,
			Ticket paidTicket, HttpSession session, Model model) {

		// validate session
		if (!SessionUtil.checkSession(session)) {
			System.out.println("Passenger Object was found null, returning to /login-form");
			return "redirect:/login-form";
		}

		// debug errors
		System.out.println("inside POST /payment");
		if (result.hasErrors()) {
			// get all field errors
			System.out.println("Errors in fields");
			for (FieldError error : result.getFieldErrors()) {
				System.out.println(String.format("field Rejected: %s", error.getField()));
				System.out.println(String.format("Value Rejected: %s", error.getRejectedValue()));
				System.out.println(String.format("Custom error field message: %s", error.getDefaultMessage()));
			}

			//returning back the journeyId value to the model
			model.addAttribute("journeyId", journeyId);

			return "payment";
		}

		// Saved journey in session
		Passenger loggedPassenger = (Passenger) session.getAttribute("loggedPassenger");

		Journey dbJourney = journeyService.getJourneyById(journeyId).orElse(null);
		Passenger dbPassenger = passengerService.getPassengerByuserName(loggedPassenger.getUserName());

		// ticket saved to repo
		Ticket dbTicket = ticketService.createTicket(paidTicket, dbJourney, dbPassenger, payment);

		paymentService.addPayment(dbTicket, payment);

		// AFTER successfull payment:
		// set ticket to ticket set of passenger
		String passengerId = dbPassenger.getPassengerId();
		passengerService.addTicketsToPassenger(passengerId);

		return "redirect:/ticket-confirmation/" + dbTicket.getTicketNo();

	}

	@GetMapping("/ticket-confirmation/{ticketId}")
	public String getConfirmationPage(@PathVariable String ticketId,
			HttpSession session, Model model) {
		if (!SessionUtil.checkSession(session)) {
			System.out.println("Passenger Object was found null, returning to /login-form");
			return "redirect:/login-form";
		}

		Passenger loggedPassenger = (Passenger) session.getAttribute("loggedPassenger");
		Passenger dbPassenger = passengerService.getPassengerByuserName(loggedPassenger.getUserName());

		model.addAttribute("passenger", dbPassenger);

		// handling nullability of journey
		try {
			Ticket dbTicket = ticketService.getTicketById(ticketId).orElse(null);

			System.out.println("Journey object inside Ticket at - /ticket-confirmation: " + dbTicket.getJourney());
			model.addAttribute("ticketJourney", dbTicket.getJourney());

			session.setAttribute("PaidTicket", dbTicket);
		} catch (Exception e) {
			System.out.println("Journey object inside Ticket was found null. returning to /booking");
			return "redirect:/booking";
		}

		return "ticket-confirmation";
	}
}
