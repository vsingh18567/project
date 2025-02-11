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
                return "{\"status\":\"success\",\"data\":{\"donationfield\":\"donationstuff\"}}";
            }
        });
        // according to the API, the only time status = success is if the donation is successful
        assertTrue(dm.makeDonation("0", "1", "100.0"));
    }

    @Test(expected=IllegalStateException.class)
    public void testNotSuccess() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":\"err\"}";
            }
        });

        dm.makeDonation("0", "1", "100.0");
    }

}
