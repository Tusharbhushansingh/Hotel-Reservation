package com.mindtree.hotelreservation.service;

import java.util.List;

import com.mindtree.hotelreservation.entity.Hotel;
import com.mindtree.hotelreservation.entity.RegisteredUser;
import com.mindtree.hotelreservation.entity.Reservation;

public interface HotelReservationService {
	
	List<Hotel> getAllHotels();

	RegisteredUser getUserByName(String userName);

	boolean authenticateUser(String userName, String password);

	Hotel getHotelById(int hotelId);

	void addReservation(Reservation reservation);

	List<Reservation> getReservationsByUserId(int userId);
	
	void createUser(RegisteredUser user);
	
	boolean isValidUser(String userName);

	
	
}
