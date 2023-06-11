package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DataManager_attemptLogin_Test {

    @Test
    public void testSuccess() {

        String contributorObj;
        String id, name, email, ccn, cvv, ccem, ccey,ccpc, fund, date;
        double amt;
//        int amt;
        id = "0";
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        fund = "f";
        date = "2023-06-06";
        amt = 100;
        contributorObj = "{\"status\":\"success\",\"data\": {" +
                        "\"_id\":\"" + id + "\",\"login\":\"user\", \"password\":\"pw\"," +
                        "\"name\":\"" + name + "\",\"email\":\"" + email + "\"," +
                        "\"creditCardNumber\":\"" + ccn + "\",\"creditCardCVV\":\"" + cvv + "\"," +
                        "\"creditCardExpiryMonth\":" + ccem + "," +
                        "\"creditCardExpiryYear\":" + ccey + "," +
                        "\"creditCardPostCode\":\"" + ccpc + "\"," +
                        "\"donations\":[{" +
                        "\"contributor\":\"" + name + "\",\"fund\":\"" + fund + "\"," +
                        "\"date\":\"" + date + "\",\"amount\":" + amt + "" +
                        "}]" +
                        "}" +
                        "}";

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/findContributorByLoginAndPassword")) {
                    return contributorObj;
                } else {
                    return "{\"status\":\"success\",\"data\":\"" + fund + "\"}";
                }
            }
        });
        double epsilon = 1e-6;

        Contributor c = dm.attemptLogin("user", "pw");
        assertNotNull(c);
        assertEquals(id, c.getId());
        assertEquals(name, c.getName());
        assertEquals(email, c.getEmail());
        assertEquals(ccn, c.getCreditCardNumber());
        assertEquals(cvv, c.getCreditCardCVV());
        assertEquals(ccem, c.getCreditCardExpiryMonth());
        assertEquals(ccey, c.getCreditCardExpiryYear());
        assertEquals(ccpc, c.getCreditCardPostCode());
        List<Donation> donationList = c.getDonations();
        assertEquals(1, donationList.size());
        for (Donation d : donationList) {
            assertEquals(name, d.getContributorName());
            assertEquals(fund, d.getFundName());
            assertEquals(date, d.getDate());
            assertEquals(amt, d.getAmount(), epsilon);
        }
    }

    @Test
    public void testLoginFailed() {

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"login failed\"}";
            }
        });

        Contributor c = dm.attemptLogin("user", "password");
        assertNull(c);
    }

    @Test(expected=IllegalStateException.class)
    public void testLoginError() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":\"err\"}";
            }
        });

        dm.attemptLogin("user", "password");
    }

}
