import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DataManager_getContributorName_Test {

    @Test
    public void testFailure() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });
        assertNull(dm.getContributorName("test"));
    }

    @Test
    public void testNoStatus() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{}";
            }
        });
        assertNull(dm.getContributorName("test"));
    }

    @Test
    public void testSuccess() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":\"name\"}";
            }
        });
        String name = dm.getContributorName("test");
        assertNotNull(name);
        assertEquals("name", name);
    }


}