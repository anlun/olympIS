package com.example.client;

import beans.Athlete;
import beans.Sex;

import java.util.ArrayList;

public class ClientAthlete {
	public ClientAthlete() {
		this.competitions = new ArrayList<String>();
	}

	public ClientAthlete(String name, Sex sex, int weight, int height, ArrayList<String> competitions){
		this.name   = name;
		this.sex    = sex;
		this.weight = weight;
		this.height = height;
		this.competitions = competitions;
	}

	public ClientAthlete(Athlete newAthlete){
		this.name   = newAthlete.getName();
		this.sex    = newAthlete.getSex();
		this.weight = newAthlete.getWeight();
		this.height = newAthlete.getHeight();
		this.competitions = new ArrayList<String>();
		this.competitions.add(newAthlete.getCompetition());
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Sex getSex() {
		return this.sex;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return this.weight;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return this.height;
	}

	public void setCompetitions(ArrayList<String> competitions) {
		this.competitions = competitions;
	}

	public void addCompetition(String competition) {
		if (!this.competitions.contains(competition)) {
			this.competitions.add(competition);
		}
	}

	public ArrayList<String> getCompetitions() {
		return this.competitions;
	}

	private String name;
	private Sex    sex;
	private int    weight;
	private int    height;
	private ArrayList<String> competitions;
}
