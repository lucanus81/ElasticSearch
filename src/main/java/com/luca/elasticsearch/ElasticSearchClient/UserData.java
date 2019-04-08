package com.luca.elasticsearch.ElasticSearchClient;

import com.fasterxml.jackson.annotation.JsonProperty;

final public class UserData {

	private final String name;
	private final String surname;
	private final int age;

	public UserData(@JsonProperty("name") String name, @JsonProperty("surname") String surname,
			@JsonProperty("age") int age) {
		this.name = name;
		this.surname = surname;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public int getAge() {
		return age;
	}

}
