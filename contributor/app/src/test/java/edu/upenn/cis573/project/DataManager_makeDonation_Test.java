package edu.upenn.cis573.project;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;

public class DataManager_makeDonation_Test {

    @Test
    public void testSuccess() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"donation\":\"donationstuff\"}}";
            }
        });
        // according to the API, the only time status = success is if the donation is successful
        assertTrue(dm.makeDonation("0", "1", "100.0"));
    }

    @Test
    public void testNotSuccess() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":\"err\"}";
            }
        });

        assertFalse(dm.makeDonation("0", "1", "100.0"));
    }

    @Test
    public void testExceptionIsFalse() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });

        assertFalse(dm.makeDonation("0", "1", "100.0"));
    }
}
