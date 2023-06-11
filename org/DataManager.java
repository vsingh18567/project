
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataManager {

	private final WebClient client;
	private HashMap<String, String> contributerNameCache;
	public DataManager(WebClient client) {
		this.client = client;
		this.contributerNameCache = new HashMap<>();
	}

	private String makeRequestWrapper(String resource, Map<String, Object> map) {
		if (client == null) {
			throw new IllegalStateException();
		}
		String response = client.makeRequest(resource, map);
		if (response == null) {
			throw new IllegalStateException();
		}
		return response;
	}

	/**
	 * Attempt to log the user into an Organization account using the login and password.
	 * This method uses the /findOrgByLoginAndPassword endpoint in the API
	 * @return an Organization object if successful; null if unsuccessful
	 */
	public Organization attemptLogin(String login, String password) {
		if (login == null || password == null) {throw new IllegalArgumentException();}
		Map<String, Object> map = new HashMap<>();
		map.put("login", login);
		map.put("password", password);
		String response = makeRequestWrapper("/findOrgByLoginAndPassword", map);
		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
		String status = (String)json.get("status");

		if (status == null) {throw new IllegalStateException();}

		if (status.equals("success")) {
			JSONObject data = (JSONObject)json.get("data");
			String fundId = (String)data.get("_id");
			if (fundId == null) {
				return null;
			}
			String name = (String)data.get("name");
			String description = (String)data.get("description");
			Organization org = new Organization(fundId, name, description);

			JSONArray funds = (JSONArray)data.get("funds");
			if (funds != null) {
				Iterator it = funds.iterator();
				while(it.hasNext()){
					JSONObject fund = (JSONObject) it.next();
					fundId = (String)fund.get("_id");
					name = (String)fund.get("name");
					description = (String)fund.get("description");
					Double target = (Double) fund.get("target");

					Fund newFund = new Fund(fundId, name, description, target);
					JSONArray donations = (JSONArray)fund.get("donations");
					List<Donation> donationList = new LinkedList<>();
					Iterator it2 = donations.iterator();
					while(it2.hasNext()){
						JSONObject donation = (JSONObject) it2.next();
						String contributorId = (String)donation.get("contributor");
						String contributorName = this.getContributorName(contributorId);
						long amount = (Long)donation.get("amount");
						String date = (String)donation.get("date");
						donationList.add(new Donation(fundId, contributorName, amount, date));
					}

					newFund.setDonations(donationList);

					org.addFund(newFund);
				}
			}

			return org;
		}
		else throw new IllegalStateException();
	}

	/**
	 * Look up the name of the contributor with the specified ID.
	 * This method uses the /findContributorNameById endpoint in the API.
	 * @return the name of the contributor on success; null if no contributor is found
	 */
	public String getContributorName(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}

		if (contributerNameCache.containsKey(id)) {
			return contributerNameCache.get(id);
		}


		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		String response = makeRequestWrapper("/findContributorNameById", map);

		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
		String status = (String)json.get("status");
		if (status == null) {throw new IllegalStateException();}
		if (status.equals("success")) {
			String name = (String)json.get("data");
			if (name != null) {
				contributerNameCache.put(id, name);
			}
			return name;
		}
		else throw new IllegalStateException();
	}

	/**
	 * This method creates a new fund in the database using the /createFund endpoint in the API
	 * @return a new Fund object if successful; null if unsuccessful
	 */
	public Fund createFund(String orgId, String name, String description, double target) {

		if (orgId == null || name == null || description == null) {throw new IllegalArgumentException();}
		Map<String, Object> map = new HashMap<>();
		map.put("orgId", orgId);
		map.put("name", name);
		map.put("description", description);
		map.put("target", target);
		String response = makeRequestWrapper("/createFund", map);

		JSONParser parser = new JSONParser();
		JSONObject json;
		try {
			json = (JSONObject) parser.parse(response);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IllegalStateException();
		}
		String status = (String)json.get("status");
		if (status == null) {throw new IllegalStateException();}
		if (status.equals("success")) {
			JSONObject fund = (JSONObject)json.get("data");
			String fundId = (String)fund.get("_id");
			return new Fund(fundId, name, description, target);
		}
		else throw new IllegalStateException();

	}


}
