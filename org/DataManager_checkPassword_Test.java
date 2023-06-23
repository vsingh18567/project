import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataManager_checkPassword_Test {


    private DataManager dm;

    /*
     * Tests for checkPassword
     */
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


    @Test(expected = IllegalArgumentException.class)
    public void testNullOrgId() {
        dm = dataManagerFactory(null);
        dm.checkPassword(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPassword() {
        dm = dataManagerFactory(null);
        dm.checkPassword("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPassword() {
        dm = dataManagerFactory(null);
        dm.checkPassword("test", "");
    }

    @Test(expected = IllegalStateException.class)
    public void testMangledJSON() {
        dm = dataManagerFactory("ru{{}bus");
        dm.checkPassword("test", "test");
    }

    @Test(expected = IllegalStateException.class)
    public void testStatusFailure() {
        dm = dataManagerFactory(generateResponse("error", null));
        dm.checkPassword("test", "test");
    }

    @Test
    public void testNoData() {
        dm = dataManagerFactory(generateResponse("success", null));
        assertFalse(dm.checkPassword("test", "test"));
    }

    @Test
    public void testSuccessfulCall() {
        String response = generateResponse("success", "{\"_id\": \"123\"}");
        dm = dataManagerFactory(response);
        assertTrue(dm.checkPassword("test", "test"));
    }

}