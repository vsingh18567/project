package edu.upenn.cis573.project;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataManager_getFundName_Test {

    @Test
    public void testSuccess() {

        DataManager dm = new DataManager(new WebClient(null, 0) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"Snoopy\"}";
            }
        });

        String name = dm.getFundName("12345");
        assertNotNull(name);
        assertEquals("Snoopy", name);


    }

    @Test
    public void testNotFound() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"not found\"}";
            }
        });

        String name = dm.getFundName("1");
        assertNotNull(name);
        assertEquals("Unknown fund", name);
    }

    @Test(expected=IllegalStateException.class)
    public void testErrorOccurs_Query() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\", \"data\":\"err\"}";
            }
        });

        dm.getFundName("1");
    }

}
