package com.spring.utils;

import com.spring.models.Passenger;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    
    public static boolean checkSession(HttpSession session) {
        Passenger loggedPassenger = (Passenger) session.getAttribute("loggedPassenger");
        return loggedPassenger != null;
    }
}
