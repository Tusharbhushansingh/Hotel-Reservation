package com.mindtree.hotelreservation.Dto;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class UserDto {
	@NotBlank
	@NotEmpty
	@Size(min = 5, max = 20)
	private String userName;
	@NotBlank
	@NotEmpty
	@Size(min = 5, max = 20)
	private String password;
	private int hotelId;

	public UserDto() {
		super();
	}

	public UserDto(String userName, String password, int hotelId) {
		super();
		this.userName = userName;
		this.password = password;
		this.hotelId = hotelId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getHotelId() {
		return hotelId;
	}

	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}

}
