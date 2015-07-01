package com.robo.store.dao;

public class UserLoginResponse extends CommonResponse {
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
