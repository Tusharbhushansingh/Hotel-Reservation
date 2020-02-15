package com.mindtree.hotelreservation.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.mindtree.hotelreservation.Dto.ReservationDto;
import com.mindtree.hotelreservation.Dto.UserDto;
import com.mindtree.hotelreservation.entity.Hotel;
import com.mindtree.hotelreservation.entity.RegisteredUser;
import com.mindtree.hotelreservation.entity.Reservation;
import com.mindtree.hotelreservation.service.HotelReservationService;
import com.mindtree.hotelreservation.util.StringToDateConversion;

@Controller
public class HotelReservationController {
	@Autowired
	HotelReservationService hotelReservationService;// = new
													// HotelReservationServiceImpl();
	@PostConstruct
	public void display() {
		System.out.println("hotelReservationService created");
	}

	public HotelReservationService getHotelReservationService() {
		return hotelReservationService;
	}

	public void setHotelReservationService(HotelReservationService hotelReservationService) {
		this.hotelReservationService = hotelReservationService;
	}

	@RequestMapping("/searchHotels")
	public String searchHotels() {
		return "SearchHotels";
	}

	@RequestMapping("/getRegistrationPage")
	public ModelAndView getRegistrationPage() {
		return new ModelAndView("userregistration", "registeredUser", new RegisteredUser());
	}
	/*
	 * @RequestMapping("/searchHotels") public ModelAndView searchHotels() {
	 * ModelAndView modelView = new ModelAndView("searchHotels"); List<String>
	 * hotelNames = new ArrayList<String>(); List<Hotel> hotels =
	 * hotelReservationService.getAllHotels(); for(Hotel hotel: hotels) {
	 * hotelNames.add(hotel.getName()); } modelView.addObject("hotelNames",
	 * hotelNames); return modelView; }
	 */

	@RequestMapping("/getHotels")
	public ModelAndView getHotels(HttpServletRequest request, HttpServletResponse response) {
	
		ModelAndView modelView = null;
		System.out.println("get Hotels in controller is called successfully");
		String searchHotel = request.getParameter("searchHotel");
		System.out.println(searchHotel);
		if (searchHotel.isEmpty()) {
			modelView = new ModelAndView("SearchHotels");
			modelView.addObject("errorMessage", "Search String is empty, Please enter a valid search");
			return modelView;
		}
		modelView = new ModelAndView("Hotels");
		List<Hotel> hotels = null;
		try {
			hotels = hotelReservationService.getAllHotels();
		} catch (HibernateException hEx) {
			modelView = new ModelAndView("searchhotelspage");
			modelView.addObject("errorMessage", "Please make sure Database is connected");
			return modelView;
		}

		List<Hotel> matchedHotels = new ArrayList<Hotel>();
		for (Hotel hotelI : hotels) {
			System.out.println(hotelI.getName());
			if (hotelI.getName().matches("(.*)" + searchHotel + "(.*)")) {
				matchedHotels.add(hotelI);
				System.out.println("Added to matched hotels " + hotelI.getName());
			}
		}
		if (matchedHotels.isEmpty()) {
			modelView = new ModelAndView("SearchHotels");
			modelView.addObject("errorMessage", "Sorry, Your search doesn't match any of the hotels");
			return modelView;
		}
		modelView.addObject("hotels", matchedHotels);

		return modelView;
	}

	@RequestMapping("/getHotel")
	public ModelAndView getHotel(@RequestParam("id") int id) {
		System.out.println(id);
		System.out.println("get Hotel in controller is called successfully");
		ModelAndView modelView = new ModelAndView("Hotel");
		Hotel hotel = null;
		try {
			hotel = hotelReservationService.getHotelById(id);
		} catch (HibernateException hEx) {
			modelView = new ModelAndView("hotels");
			modelView.addObject("hotel",
					"U have done something wrong (Database disconnected) you Please make sure Database is connected");
			return modelView;
		}
		modelView.addObject("hotel", hotel);
		return modelView;
	}

	@RequestMapping("/bookHotelAfterLogin")
	public ModelAndView bookHotelAfterLogin(@RequestParam("id") int id, HttpSession session) {
		System.out.println(id);
		System.out.println("get Hotel in controller is called successfully");
		ModelAndView modelView = new ModelAndView("userLogin", "userDto", new UserDto());
		Hotel hotel = null;
		try {
			session.setAttribute("hotelId", id);
			hotel = hotelReservationService.getHotelById(id);
		} catch (HibernateException hEx) {
			modelView = new ModelAndView("hotel");
			modelView.addObject("errorMessage",
					"U have done something wrong (Database disconnected) you Please make sure Database is connected");
			return modelView;
		}
		modelView.addObject("hotel", hotel);
		return modelView;
	}

	@RequestMapping("/bookHotel")
	public ModelAndView bookHotel(@ModelAttribute UserDto userDto, BindingResult result, HttpSession session) {
		ModelAndView modelView = null;
		System.out.println("Validation Errors " + result.hasErrors());
		Boolean isErrors = false;
		int userNameLength = userDto.getUserName().length();
		int passwordLength = userDto.getPassword().length();
		if (userNameLength < 1 || userNameLength > 20 || passwordLength < 1 || passwordLength > 20)
			isErrors = true;
		if (result.hasErrors() || isErrors) {
			System.out.println("spring validation started");
			modelView = new ModelAndView("userLogin");
			modelView.addObject("errorMessage", "Invalid user name or password! enter again");
			return modelView;
		}

		String userName = userDto.getUserName();
		String password = userDto.getPassword();
		if (!hotelReservationService.isValidUser(userName)) {
			System.out.println("is User Name valid in controller");
			modelView = new ModelAndView("userLogin");
			modelView.addObject("errorMessage", "User name doesn't exist! enter again");
			return modelView;
		}
		Hotel hotel = null;
		RegisteredUser user = null;
		try {
			hotel = hotelReservationService.getHotelById((int) session.getAttribute("hotelId"));
			user = hotelReservationService.getUserByName(userName);
		} catch (HibernateException hEx) {
			modelView = new ModelAndView("userLogin");
			modelView.addObject("errorMessage",
					"U have done something wrong (Database disconnected) you Please make sure Database is connected");
			return modelView;
		}
		Reservation reservation = new Reservation();
		reservation.setHotelId(hotel.getId());
		reservation.setUserId(user.getId());
		System.out.println(userDto.getUserName());
		System.out.println(userDto.getPassword());
		System.out.println(userDto.getHotelId());
		boolean status = false;
		try {
			status = hotelReservationService.authenticateUser(userName, password);
		} catch (HibernateException hEx) {
			modelView = new ModelAndView("userLogin");
			modelView.addObject("errorMessage",
					"U have done something wrong (Database disconnected) you Please make sure Database is connected");
			return modelView;
		}
		System.out.println(status);

		if (status == false) {
			modelView = new ModelAndView("userLogin");
			modelView.addObject("errorMessage", "User name or password is wrong! enter again");
		} else {
			modelView = new ModelAndView("reservation", "reservation", new Reservation());
			modelView.addObject("reservationObj", reservation);
			modelView.addObject("hotel", hotel);
			modelView.addObject("user", user);

		}
		return modelView;
	}

	@RequestMapping("/showBookedHotels")
	public ModelAndView showBookedHotels(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelView = null;
		System.out.println(request.getParameter("checkInDate"));
		System.out.println(request.getParameter("checkOutDate"));
		System.out.println();
		System.out.println();
		System.out.println("showBookedHotels in controller");
		Reservation reservation = new Reservation();
		int hotelId = Integer.parseInt(request.getParameter("hotelId"));
		String userName = request.getParameter("userName");
		String checkInDateString = request.getParameter("checkInDate");
		String checkOutDateString = request.getParameter("checkOutDate");
		Boolean isError = (checkInDateString.length() < 1) || (checkOutDateString.length() < 1);
		System.out.println(isError);
		if (checkInDateString.isEmpty() || checkOutDateString.isEmpty() || isError) {
			modelView = new ModelAndView("reservation");
			modelView.addObject("errorMessage", "Please fill all the fields");
			modelView.addObject("reservationObj", reservation);
			modelView.addObject("hotel", hotelReservationService.getHotelById(hotelId));
			modelView.addObject("user", hotelReservationService.getUserByName(userName));
			return modelView;
		}
		System.out.println(checkInDateString.length());
		System.out.println(checkOutDateString.length());
		// Hotel hotel = reservationServiceImpl.getHotelById(hotelId);
		RegisteredUser user = hotelReservationService.getUserByName(userName);
		Date checkInDate = StringToDateConversion.getDayMonthYearFromStringDate(checkInDateString);
		Date checkOutDate = StringToDateConversion.getDayMonthYearFromStringDate(checkOutDateString);
		System.out.println(checkInDate);
		System.out.println(checkOutDate);
		System.out.println(new Date());
		// new Scanner(System.in).nextLine();
		if (checkInDate.before(new Date())) {
			System.out.println("Before current date");
			modelView = new ModelAndView("reservation");
			modelView.addObject("errorMessage", "CheckInDate cant be a past date " + "or before current date");
			modelView.addObject("reservationObj", reservation);
			modelView.addObject("hotel", hotelReservationService.getHotelById(hotelId));
			modelView.addObject("user", hotelReservationService.getUserByName(userName));
			return modelView;
		} else if (checkOutDate.before(checkInDate)) {
			modelView = new ModelAndView("reservation");
			System.out.println("Before checkin date");
			modelView.addObject("errorMessage", "CheckOutDate can't be before CheckInDate");
			modelView.addObject("reservationObj", reservation);
			modelView.addObject("hotel", hotelReservationService.getHotelById(hotelId));
			modelView.addObject("user", hotelReservationService.getUserByName(userName));
			return modelView;
		}
		reservation.setUserId(user.getId());
		reservation.setHotelId(hotelId);
		reservation.setCheckInDate(checkInDate);
		reservation.setCheckOutDate(checkOutDate);

		modelView = new ModelAndView("showBookedHotels");

		hotelReservationService.addReservation(reservation);
		List<Reservation> reservationsById = null;
		try {
			reservationsById = hotelReservationService.getReservationsByUserId(user.getId());
		} catch (HibernateException hEx) {
			modelView = new ModelAndView("showBookedHotels");
			modelView.addObject("errorMessage",
					"U have done something wrong (Database disconnected) you Please make sure Database is connected");
			return modelView;
		}
		List<ReservationDto> reservationsOfUser = new ArrayList<ReservationDto>();
		for (Reservation reservtn : reservationsById) {
			reservationsOfUser.add(new ReservationDto(user, hotelReservationService.getHotelById(reservtn.getHotelId()),
					reservtn.getCheckInDate(), reservtn.getCheckOutDate()));
		}
		modelView.addObject("reservations", reservationsOfUser);
		modelView.addObject("user", user);
		return modelView;
	}

	@RequestMapping(value = "/createUser")
	public ModelAndView createUser(@ModelAttribute RegisteredUser registeredUser, BindingResult result) {
		ModelAndView modelView = null;
		Boolean isErrors = false;
		int userNameLength = registeredUser.getUserName().length();
		int passwordLength = registeredUser.getPassword().length();
		if (userNameLength < 1 || userNameLength > 20 || passwordLength < 1 || passwordLength > 20)
			isErrors = true;
		if (result.hasErrors() || isErrors) {
			System.out.println("validation started");
			modelView = new ModelAndView("userregistration");
			modelView.addObject("errorMessage",
					"Invalid user name or password!" + " enter again" + "\n" + "Didn't match the Requirements");
			return modelView;
		}
		if (hotelReservationService.isValidUser(registeredUser.getUserName())) {
			System.out.println("server side validations started");
			modelView = new ModelAndView("userregistration");
			modelView.addObject("errorMessage", "User name already existed" + "\nPlease choose other User name");
			return modelView;
		}
		hotelReservationService.createUser(registeredUser);
		modelView = new ModelAndView("userLogin", "userDto", new UserDto());
		modelView.addObject("successMessage","Successfully Registered! Now you can login"
				+ "with your user name");
		return modelView;

	}
}
