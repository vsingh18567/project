package edu.upenn.cis573.project;

import android.util.Log;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class DataManager {

    private WebClient client;
    private Map<String, String> fundNameCache; // key = id, val = name

    public DataManager(WebClient client) {
        this.client = client;
        fundNameCache = new HashMap<String, String>();
    }


    /**
     * Attempt to log in to the Contributor account using the specified login and password.
     * This method uses the /findContributorByLoginAndPassword endpoint in the API
     * @return the Contributor object if successfully logged in, null otherwise
     */
    public Contributor attemptLogin(String login, String password) {

        if (client == null) {
            throw new IllegalStateException("Client cannot be null!!");
        }
        if (login == null || password == null) {
            throw new IllegalArgumentException("Arguments cannot be null!");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("login", login);
        map.put("password", password);
        String response = client.makeRequest("/findContributorByLoginAndPassword", map);
        if (response == null) {
            throw new IllegalStateException("Client did not return valid response");
        }

        try {
            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (status.equals("success")) {
                JSONObject data = (JSONObject)json.get("data");
                String id = (String)data.get("_id");
                String name = (String)data.get("name");
                String email = (String)data.get("email");
                String creditCardNumber = (String)data.get("creditCardNumber");
                String creditCardCVV = (String)data.get("creditCardCVV");
                String creditCardExpiryMonth = ((Integer)data.get("creditCardExpiryMonth")).toString();
                String creditCardExpiryYear = ((Integer)data.get("creditCardExpiryYear")).toString();
                String creditCardPostCode = (String)data.get("creditCardPostCode");

                Contributor contributor = new Contributor(id, name, email, creditCardNumber, creditCardCVV, creditCardExpiryYear, creditCardExpiryMonth, creditCardPostCode);

                List<Donation> donationList = new LinkedList<>();

                JSONArray donations = (JSONArray)data.get("donations");

                for (int i = 0; i < donations.length(); i++) {

                    JSONObject jsonDonation = donations.getJSONObject(i);

                    String fundId = (String)jsonDonation.get("fund");
                    String fund = getFundName(fundId);
                    String date = (String)jsonDonation.get("date");
                    double amount = Double.parseDouble(jsonDonation.get("amount").toString());

                    Donation donation = new Donation(fund, name, amount, date);
                    donationList.add(donation);

                }

                contributor.setDonations(donationList);

                return contributor;

            } else if (status.equals("error")) {
                throw new IllegalStateException("Client returned error");
            }

            return null;

        } catch (JSONException je) {
            throw new IllegalStateException("Client returned Malformed JSON");
        } catch (IllegalStateException ie) {
            throw ie;
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks that the inputted password is correct using the /findContributorPasswordById endpoint
     * @return boolean representing if tested password matches or not, null if error
     * @throws IllegalStateException for client errors
     * @throws IllegalArgumentException for invalid arguments
     */
    public boolean checkPassword(String id, String password) {
        if (client == null) {
            throw new IllegalStateException("Client cannot be null!!");
        }
        if (id == null || password == null) {
            throw new IllegalArgumentException("Arguments cannot be null!");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        String response = client.makeRequest("/findContributorPasswordById", map);
        if (response == null) {
            throw new IllegalStateException("Client did not return valid response");
        }

        try {
            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (status.equals("success")) {
                String correct_pw = (String)json.get("data");
                return correct_pw.equals(password);
            } else if (status.equals("not found")) {
                return false;
            } else {
                // if status.equals("error")
                throw new IllegalStateException("Client returned error");
            }

        } catch (JSONException je) {
            throw new IllegalStateException("Client returned Malformed JSON");
        } catch (IllegalStateException ie) {
            throw ie;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates user (Contributor) account information using the /updateContributor endpoint
     * @return the new Contributor information if successful, null otherwise
     * @throws IllegalStateException for client errors
     * @throws IllegalArgumentException for invalid arguments
     */
    public Contributor editUserInfo(String id, String name, String email, String creditCardNumber, String creditCardCVV, String creditCardExpiryMonth, String creditCardExpiryYear, String creditCardPostCode) {
        if (client == null) {
            throw new IllegalStateException("Client cannot be null!!");
        }
        if (id == null || name == null || email == null || creditCardNumber == null || creditCardCVV == null ||
                creditCardExpiryYear == null || creditCardExpiryMonth == null || creditCardPostCode == null) {
            throw new IllegalArgumentException("Arguments cannot be null!");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        map.put("card_number", creditCardNumber);
        map.put("card_cvv", creditCardCVV);
        map.put("card_month", creditCardExpiryMonth);
        map.put("card_year", creditCardExpiryYear);
        map.put("card_postcode", creditCardPostCode);
        String response = client.makeRequest("/updateContributor", map);
        if (response == null) {
            throw new IllegalStateException("Client did not return valid response");
        }

        try {
            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (status.equals("success")) {
                JSONObject data = (JSONObject)json.get("data");
                // String id = (String)data.get("_id");
                Contributor contributor = new Contributor(id, name, email, creditCardNumber, creditCardCVV, creditCardExpiryYear, creditCardExpiryMonth, creditCardPostCode);

                List<Donation> donationList = new LinkedList<>();

                JSONArray donations = (JSONArray)data.get("donations");

                for (int i = 0; i < donations.length(); i++) {

                    JSONObject jsonDonation = donations.getJSONObject(i);

                    String fundId = (String)jsonDonation.get("fund");
                    String fund = getFundName(fundId);
                    String date = (String)jsonDonation.get("date");
                    double amount = Double.parseDouble(jsonDonation.get("amount").toString());

                    Donation donation = new Donation(fund, name, amount, date);
                    donationList.add(donation);

                }

                contributor.setDonations(donationList);

                return contributor;

            } else if (status.equals("error")) {
                throw new IllegalStateException("Client returned error");
            }
            Log.v("DataManager_editUserInfo", "returning null.");
            return null;

        } catch (JSONException je) {
            throw new IllegalStateException("Client returned Malformed JSON");
        } catch (IllegalStateException ie) {
            throw ie;
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the name of the fund with the specified ID using the /findFundNameById endpoint
     * @return the name of the fund if found, "Unknown fund" if not found, null if an error occurs
     */
    public String getFundName(String id) {

        if (client == null) {
            throw new IllegalStateException("Client cannot be null!!");
        }
        if (id == null) {
            throw new IllegalArgumentException("Arguments cannot be null!");
        }

        if (fundNameCache.containsKey(id)) {
            return fundNameCache.get(id);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        String response = client.makeRequest("/findFundNameById", map);
        if (response == null) {
            throw new IllegalStateException("Client did not return valid response");
        }

        try {
            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (status.equals("success")) {
                String name = (String)json.get("data");
                fundNameCache.put(id, name);
                return name;
            } else if (status.equals("not found")) {
                return "Unknown fund";
            } else if (status.equals("error")) {
                throw new IllegalStateException("Client returned error");
            } else {
                return null;
            }
//            else return "Unknown fund";

        } catch (JSONException je) {
            throw new IllegalStateException("Client returned Malformed JSON");
        } catch (IllegalStateException ie) {
            throw ie;
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * Get information about all of the organizations and their funds.
     * This method uses the /allOrgs endpoint in the API
     * @return a List of Organization objects if successful, null otherwise
     */
    public List<Organization> getAllOrganizations() {
        if (client == null) {
            throw new IllegalStateException("Client cannot be null!!");
        }
        Map<String, Object> map = new HashMap<>();
        String response = client.makeRequest("/allOrgs", map);
        if (response == null) {
            throw new IllegalStateException("Client did not return valid response");
        }

        try {
            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");

            if (status.equals("success")) {

                List<Organization> organizations = new LinkedList<>();

                JSONArray data = (JSONArray)json.get("data");

                for (int i = 0; i < data.length(); i++) {

                    JSONObject obj = data.getJSONObject(i);

                    String id = (String)obj.get("_id");
                    String name = (String)obj.get("name");

                    Organization org = new Organization(id, name);

                    List<Fund> fundList = new LinkedList<>();

                    JSONArray array = (JSONArray)obj.get("funds");

                    for (int j = 0; j < array.length(); j++) {

                        JSONObject fundObj = array.getJSONObject(j);

                        id = (String)fundObj.get("_id");
                        name = (String)fundObj.get("name");
                        double target = Double.parseDouble(fundObj.get("target").toString());
                        double totalDonations = Double.parseDouble(fundObj.get("totalDonations").toString());

                        Fund fund = new Fund(id, name, target, totalDonations);

                        fundList.add(fund);

                    }

                    org.setFunds(fundList);

                    organizations.add(org);

                }

                return organizations;

            } else if (status.equals("error")) {
                // if mongo is disconnected, JSON = error, MongoError
                throw new IllegalStateException("Client returned error");
            }

            return null;

        } catch (JSONException je) {
            throw new IllegalStateException("Client returned Malformed JSON");
        } catch (IllegalStateException ie) {
            throw ie;
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * Make a donation to the specified fund for the specified amount.
     * This method uses the /makeDonation endpoint in the API
     * @return true if successful, false otherwise
     */
    public boolean makeDonation(String contributorId, String fundId, String amount) {

        if (client == null) {
            throw new IllegalStateException("Client cannot be null!!");
        }
        if (contributorId == null || fundId == null || amount == null) {
            throw new IllegalArgumentException("Arguments cannot be null!");
        }
        if (!amount.matches("^\\d*.?\\d+$")) {
            throw new IllegalArgumentException("Amount must be numeric!");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("contributor", contributorId);
        map.put("fund", fundId);
        map.put("amount", amount);
        String response = client.makeRequest("/makeDonation", map);
        if (response == null) {
            throw new IllegalStateException("Client did not return valid response");
        }

        try {
            JSONObject json = new JSONObject(response);
            String status = (String)json.get("status");
            if (status.equals("error")) {
                throw new IllegalStateException("Client returned error");
            }
            return status.equals("success");

        } catch (JSONException je) {
            throw new IllegalStateException("Client returned Malformed JSON");
        } catch (IllegalStateException ie) {
            throw ie;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }

    }
}
