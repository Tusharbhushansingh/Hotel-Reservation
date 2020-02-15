package com.mindtree.hotelreservation.exception;

@SuppressWarnings("serial")
public class HotelReservationException extends Exception {

	public HotelReservationException() {
		super();

	}

	public HotelReservationException(String message, Throwable cause) {
		super(message, cause);

	}

	public HotelReservationException(String message) {
		super(message);

	}

	public HotelReservationException(Throwable cause) {
		super(cause);

	}

}
