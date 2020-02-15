
package com.mindtree.hotelreservation.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.mindtree.hotelreservation.dao.HotelReservationDAO;
import com.mindtree.hotelreservation.dao.HotelReservationDAOImpl;
import com.mindtree.hotelreservation.entity.Hotel;
import com.mindtree.hotelreservation.entity.RegisteredUser;
import com.mindtree.hotelreservation.entity.Reservation;

public class HotelReservationServiceImpl implements HotelReservationService {
    @Autowired
	private HotelReservationDAO reservationDaoImpl;// = new HotelReservationDAOImpl();
	@PostConstruct
	public void display() {
		System.out.println("reservationDaoImpl created"); 
	}
	

	public HotelReservationDAO getReservationDaoImpl() {
		return reservationDaoImpl;
	}

	public void setReservationDaoImpl(HotelReservationDAO reservationDaoImpl) {
		this.reservationDaoImpl = reservationDaoImpl;
	}

	public List<Hotel> getAllHotels() throws HibernateException {
		System.out.println(reservationDaoImpl);
		return reservationDaoImpl.getAllHotels();
	}

	public RegisteredUser getUserByName(String userName) throws HibernateException {
		return reservationDaoImpl.getUserByName(userName);
	}

	public Hotel getHotelById(int hotelId) throws HibernateException {
		return reservationDaoImpl.getHotelById(hotelId);
	}

	public boolean authenticateUser(String userName, String password) throws HibernateException {
		if (!reservationDaoImpl.isValidUser(userName))
			return false;
		if (reservationDaoImpl.getPasswordByUserName(userName).compareTo(password) != 0)
			return false;
		return true;

	}

	public void addReservation(Reservation reservation) throws HibernateException {
		reservationDaoImpl.addReservation(reservation);

	}

	public List<Reservation> getReservationsByUserId(int userId) throws HibernateException {
		return reservationDaoImpl.getReservationsByUserId(userId);
	}


	@Override
	public void createUser(RegisteredUser user) {
		reservationDaoImpl.createUser(user);
		
	}


	public boolean isValidUser(String userName) {
		return reservationDaoImpl.isValidUser(userName);
	}

}