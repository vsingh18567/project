import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_getOrg_Test {

    private DataManager dm;

    public static String generateResponse(String status, String data) {
        String base = "{";
        if (status != null) {
            base += "\"status\": \"" + status + "\",";
        }
        if (data != null) {
            base += "\"data\":" + data;
        }
        base += "}";
        return base;
    }

    public static DataManager dataManagerFactory(String response) {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return response;
            }
        });
        return dm;
    }

    @Test(expected = IllegalStateException.class)
    public void testFailure() {
        dm = dataManagerFactory(generateResponse("error", null));
        dm.getOrg("hi");
    }

    @Test
    public void testSimpleSuccess() {
        String response = generateResponse("success", "{\"_id\": \"123\"}");
        dm = dataManagerFactory(response);
        assertTrue(dm.getOrg("new"));
    }

    @Test
    public void testOrgNotFound() {
        String response = generateResponse("success", null);
        dm = dataManagerFactory(response);
        assertFalse(dm.getOrg("hello"));
    }

    @Test(expected = IllegalStateException.class)
    public void testJSONException() {
        dm = dataManagerFactory("{}");
        dm.getOrg("login");
    }
    
    @Test
    public void testStatusException() {
        dm = dataManagerFactory(generateResponse("success", "{\"_id\": null}"));
        assertFalse(dm.getOrg("login"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testParseException() {
        dm = dataManagerFactory("");
        dm.getOrg("login");
    }

}
