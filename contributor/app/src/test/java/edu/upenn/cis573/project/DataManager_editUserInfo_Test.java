package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DataManager_editUserInfo_Test {
    private DataManager dm;
    String id, name, email, ccn, cvv, ccem, ccey,ccpc, fund, date;

    @Test
    public void test_Success() {

        String contributorObj;
        double amt;
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
                if (resource.equals("/updateContributor")) {
                    return contributorObj;
                } else {
                    return "{\"status\":\"success\",\"data\":\"" + fund + "\"}";
                }
            }
        });
        double epsilon = 1e-6;

        Contributor c = dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);
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

    @Test(expected=IllegalStateException.class)
    public void test_Error() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":\"err\"}";
            }
        });

        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);
    }

    @Test
    public void test_UnknownStatus() {

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"unknown\"}";
            }
        });

        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        assertNull(dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc));
    }

    @Test
    public void test_NullStatus() {

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":null}";
            }
        });

        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        assertNull(dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc));
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientIsNull() {
        dm = new DataManager(null);
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_NameIsNull() {
        dm = new DataManager(new WebClient(null, 0));
//        name = null;
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);

    }

    @Test(expected=IllegalArgumentException.class)
    public void test_EmailIsNull() {
        dm = new DataManager(new WebClient(null, 0));
        name = "c";
//        email = null;
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);

    }

    @Test(expected=IllegalArgumentException.class)
    public void test_CCNIsNull() {
        dm = new DataManager(new WebClient(null, 0));
        name = "c";
        email = "c@email.com";
//        ccn = null;
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);

    }

    @Test(expected=IllegalArgumentException.class)
    public void test_CVVIsNull() {
        dm = new DataManager(new WebClient(null, 0));
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
//        cvv = null;
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);

    }

    @Test(expected=IllegalArgumentException.class)
    public void test_ExpirationMonthIsNull() {
        dm = new DataManager(new WebClient(null, 0));
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
//        ccem = null;
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);

    }

    @Test(expected=IllegalArgumentException.class)
    public void test_ExpirationYearIsNull() {
        dm = new DataManager(new WebClient(null, 0));
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
//        ccey = null;
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);

    }

    @Test(expected=IllegalArgumentException.class)
    public void test_PostalCodeIsNull() {
        dm = new DataManager(new WebClient(null, 0));
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
//        ccpc = null;
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);

    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientCannotConnectToServer() {
        // assumes no server is running on port 3002
        dm = new DataManager(new WebClient("10.0.2.2", 3002));
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientReturnsNull() {
        dm = new DataManager(new WebClient("10.0.2.2", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientReturnsError() {
        dm = new DataManager(new WebClient("10.0.2.2", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"error\":\"An unexpected database error occurred\"}";
            }
        });
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientReturnsMalformedJSON() {
        dm = new DataManager(new WebClient("10.0.2.2", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "Malformed JSON";
            }
        });
        name = "c";
        email = "c@email.com";
        ccn = "1234567812345678";
        cvv = "999";
        ccem = "0";
        ccey = "0";
        ccpc = "19104";
        dm.editUserInfo(name, email, ccn, cvv, ccem, ccey, ccpc);
    }
}
