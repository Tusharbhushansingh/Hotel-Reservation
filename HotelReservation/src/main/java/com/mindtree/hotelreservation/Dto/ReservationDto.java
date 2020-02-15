package com.mindtree.hotelreservation.Dto;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.mindtree.hotelreservation.entity.Hotel;
import com.mindtree.hotelreservation.entity.RegisteredUser;

@SuppressWarnings("unused")
public class ReservationDto {
	private RegisteredUser user;
	private Hotel hotel;
	private Date checkInDate;
	private Date checkOutDate;

	public ReservationDto(RegisteredUser user, Hotel hotel, Date checkInDate, Date checkOutDate) {
		super();
		this.user = user;
		this.hotel = hotel;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
	}

	public RegisteredUser getUser() {
		return user;
	}

	public void setUser(RegisteredUser user) {
		this.user = user;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

}
