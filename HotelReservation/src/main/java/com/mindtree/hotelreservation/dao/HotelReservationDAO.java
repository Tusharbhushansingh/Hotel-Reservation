package com.mindtree.hotelreservation.dao;

import java.util.List;
import com.mindtree.hotelreservation.entity.Hotel;
import com.mindtree.hotelreservation.entity.RegisteredUser;
import com.mindtree.hotelreservation.entity.Reservation;

public interface HotelReservationDAO {

	List<Hotel> getAllHotels();

	String getPasswordByUserName(String userName);

	RegisteredUser getUserByName(String userName);

	boolean isValidUser(String userName);

	Hotel getHotelById(int hotelId);

	void addReservation(Reservation reservation);

	List<Reservation> getReservationsByUserId(int userId);

	void createUser(RegisteredUser user);
}
