package edu.upenn.cis573.project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;

public class DataManager_checkPassword_Test {
    private DataManager dm;

    @Test
    public void test_SuccessPasswordMatch() {

        String id = "0";
        String password = "pw";

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"" + password + "\"}";
            }
        });
        assertTrue(dm.checkPassword(id, password));
    }

    @Test
    public void test_SuccessPasswordMismatch() {

        String id = "0";
        String password = "pw";

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"" + "password" + "\"}";
            }
        });
        assertFalse(dm.checkPassword(id, password));
    }

    @Test(expected=IllegalStateException.class)
    public void test_ClientReturnsNotFound() {

        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"not found\"}";
            }
        });

        assertFalse(dm.checkPassword("0", "password"));
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientIsNull() {
        dm = new DataManager(null);
        dm.checkPassword("0", "password");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IdIsNull() {
        dm = new DataManager(new WebClient("10.0.2.2", 3001));
        dm.checkPassword(null, "password");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_PasswordIsNull() {
        dm = new DataManager(new WebClient("10.0.2.2", 3001));
        dm.checkPassword("0", null);
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientCannotConnectToServer() {
        // assumes no server is running on port 3002
        dm = new DataManager(new WebClient("10.0.2.2", 3002));
        dm.checkPassword("0", "password");
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientReturnsNull() {
        dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.checkPassword("0", "password");

    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientReturnsError() {
        dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"error\":\"An unexpected database error occurred\"}";
            }
        });
        dm.checkPassword("0", "password");
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientReturnsErrorData() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":\"err\"}";
            }
        });
        dm.checkPassword("0", "password");
    }

    @Test(expected=IllegalStateException.class)
    public void test_WebClientReturnsMalformedJSON() {
        dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "Malformed JSON";
            }
        });
        dm.checkPassword("0", "password");

    }
}
