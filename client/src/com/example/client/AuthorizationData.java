package com.example.client;

public class AuthorizationData {

	public static synchronized AuthorizationData getInstance() {
		if (authorizationData == null) {
			authorizationData = new AuthorizationData();
		}
		return authorizationData;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	private AuthorizationData() {
		login = "";
		password = "";
	}

	private static AuthorizationData authorizationData;
	private String login;
	private String password;
}
