package com.mindtree.hotelreservation.dao;

import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import com.mindtree.hotelreservation.entity.Hotel;
import com.mindtree.hotelreservation.entity.RegisteredUser;
import com.mindtree.hotelreservation.entity.Reservation;

public class HotelReservationDAOImpl implements HotelReservationDAO {
	@Autowired
	SessionFactory sessionFactory;
	@PostConstruct
	public void display() {
		System.out.println("sessionFactory created"); 
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	public List<Hotel> getAllHotels() throws HibernateException {
		Session session = sessionFactory.openSession();
		List<Hotel> hotels=null;
		hotels = session.createCriteria(Hotel.class).list();
		session.flush();
		session.close();
		return hotels;
	}

	public Hotel getHotelById(int hotelId) throws HibernateException {
		Session session = sessionFactory.openSession();
		Hotel hotel = (Hotel) session.createCriteria(Hotel.class).add(Restrictions.eq("id", hotelId)).list().get(0);
		session.flush();
		session.close();
		return hotel;
	}

	public String getPasswordByUserName(String userName) throws HibernateException {
		Session session = sessionFactory.openSession();
		RegisteredUser registerUser = (RegisteredUser) session.createCriteria(RegisteredUser.class)
				.add(Restrictions.eq("userName", userName)).list().get(0);
		session.flush();
		session.close();
		return registerUser.getPassword();
	}

	public boolean isValidUser(String userName) throws HibernateException {
		Session session = sessionFactory.openSession();
		int size = session.createCriteria(RegisteredUser.class).add(Restrictions.eq("userName", userName)).list()
				.size();
		session.flush();
		session.close();
		System.out.println("is valid user called");
		System.out.println(size>0?"true":"false");
		if (size > 0)
			return true;
		else
			return false;
	}

	public RegisteredUser getUserByName(String userName) throws HibernateException {
		System.out.println("get user by Name called");
		Session session = sessionFactory.openSession();
		RegisteredUser user = (RegisteredUser) session.createCriteria(RegisteredUser.class)
				.add(Restrictions.eq("userName", userName)).list().get(0);
		session.flush();
		session.close();
		return user;
	}

	public void addReservation(Reservation reservation) throws HibernateException {
		System.out.println("add reservation is called");
		System.out.println(reservation.getCheckInDate());

		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.saveOrUpdate(reservation);
			session.flush();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Reservation> getReservationsByUserId(int userId) throws HibernateException {
		Session session = sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<Reservation> reservations = session.createCriteria(Reservation.class)
				.add(Restrictions.eq("userId", userId)).list();
		session.flush();
		session.close();
		return reservations;
	}

	@Override
	public void createUser(RegisteredUser user) {
		Session session = sessionFactory.openSession();
		session.save(user);
		session.flush();
		session.close();
		
	}

}
