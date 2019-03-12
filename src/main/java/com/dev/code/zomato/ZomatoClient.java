package com.dev.code.zomato;

import java.util.Scanner;

import org.apache.commons.lang.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ZomatoClient {

	final static String USER_KEY = "8470894b0b411a3b52c64c7ceb221815";

	public static void main(String[] args) {
		System.out.println("Welcome to Zomato!!");
		System.out.println("Below is the list of cuisines available in your city, Bangalore!");
		getCuisines();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a valid Cuisine name to get resturants list: ");
		String cuisine = sc.next();
		long[] ids = getRestuarants(cuisine);

		System.out.println("Enter restuarant id from the above shown list to get restaurant details: ");
		long id = sc.nextLong();
		if (ArrayUtils.contains(ids, id)) {
			restuarantDetails(String.valueOf(id));
		} else {
			System.out.println("restuarant id not from provided list!");
		}

	}

	/**
	 * get cuisine
	 */
	public static void getCuisines() {
		final String url = "https://developers.zomato.com/api/v2.1/cuisines?city_id=4";
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("user-key", USER_KEY);
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			ResponseEntity<String> response = (ResponseEntity<String>) restTemplate.exchange(url, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonObj = new JSONObject(response.getBody().toString());
			JSONArray c = jsonObj.getJSONArray("cuisines");
			System.out.println("List of cuisines available in Bangalore: ");
			for (int i = 0; i < 10; i++) {
				JSONObject obj = c.getJSONObject(i).getJSONObject("cuisine");
				System.out.println(obj.get("cuisine_name"));
			}
		} catch (Exception e) {
			System.out.println("Excpetion occurred while processing your request: " + e);
		}
	}

	/**
	 * @param cuisine
	 * @return
	 */
	public static long[] getRestuarants(String cuisine) {
		final String url = "https://developers.zomato.com/api/v2.1/search?entity_id=4&entity_type=city&count=10&cuisines="
				+ cuisine + "&sort=rating&order=desc";
		long[] arr = new long[10];
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("user-key", USER_KEY);
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			ResponseEntity<String> response = (ResponseEntity<String>) restTemplate.exchange(url, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonObj = new JSONObject(response.getBody().toString());
			JSONArray c = jsonObj.getJSONArray("restaurants");
			System.out.println("List of restaurants sorted by rating for the cuisine " + cuisine + " are : ");
			for (int i = 0; i < 10; i++) {
				JSONObject r = c.getJSONObject(i).getJSONObject("restaurant").getJSONObject("R");
				arr[i] = r.getLong("res_id");
				System.out.println("Restuarant Id: " + arr[i] + " Details -> Name: "
						+ c.getJSONObject(i).getJSONObject("restaurant").getString("name") + " , Rating: "
						+ c.getJSONObject(i).getJSONObject("restaurant").getJSONObject("user_rating")
								.getString("aggregate_rating"));
			}
		} catch (Exception e) {
			System.out.println("Excpetion occurred while processing your request: " + e);
		}
		return arr;
	}

	/**
	 * @param res_id
	 */
	public static void restuarantDetails(String res_id) {
		final String url = "https://developers.zomato.com/api/v2.1/restaurant?res_id=" + res_id;
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("user-key", USER_KEY);
			HttpEntity<String> entity = new HttpEntity<String>("Parameters", headers);
			ResponseEntity<String> response = (ResponseEntity<String>) restTemplate.exchange(url, HttpMethod.GET,
					entity, String.class);
			JSONObject jsonObj = new JSONObject(response.getBody().toString());
			if (jsonObj.has("name")) {
				System.out.println("name: " + jsonObj.getString("name"));
			} else {
				System.out.println("name for this restuarant is not provided by zomato api");
			}
			if (jsonObj.has("location")) {
				System.out.println("location: " + jsonObj.getJSONObject("location").toString());
			} else {
				System.out.println("location for this restuarant is not provided by zomato api");
			}
			if (jsonObj.has("user_rating")) {
				System.out.println("rating: " + jsonObj.getJSONObject("user_rating").getString("aggregate_rating"));
			} else {
				System.out.println("rating for this restuarant is not provided by zomato api");
			}
			if (jsonObj.has("phone_numbers")) {
				System.out.println("phone_numbers: " + jsonObj.getString("phone_numbers"));
			} else {
				System.out.println("phone numbers for this restuarant is not provided by zomato api");
			}
		} catch (Exception e) {
			System.out.println("Excpetion occurred while processing your request: " + e);
		}
	}

}
