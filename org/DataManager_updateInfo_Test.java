import org.junit.Before;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_updateInfo_Test {


    private DataManager dm;
    private Organization org;
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

    @Before
    public void makeOrg() {
        org = new Organization("id", "name", "desc");
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
        dm.updateInfo(null, "test", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArgs() {
        dm = dataManagerFactory(null);
        dm.updateInfo(org, "", "");
    }

    @Test
    public void testNullArgs() {
        dm = dataManagerFactory(null);
        assertFalse(dm.updateInfo(org, null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testMangledJSON() {
        dm = dataManagerFactory("ru{{}bus");
        dm.updateInfo(org, "test", "test");
    }

    @Test(expected = IllegalStateException.class)
    public void testStatusFailure() {
        dm = dataManagerFactory(generateResponse("error", null));
        dm.updateInfo(org, "test", "test");
    }

    @Test(expected = IllegalStateException.class)
    public void testNoData() {
        dm = dataManagerFactory(generateResponse("success", null));
        assertTrue(dm.updateInfo(org, "test", "test"));
    }

    @Test
    public void testSuccessful() {
        dm = dataManagerFactory(generateResponse(
                "success",
                "{\"name\":\"new\",\"description\":\"new\"}"
        ));
        assertTrue(dm.updateInfo(org, "new", "new"));
        assertEquals(org.getName(), "new");
        assertEquals(org.getDescription(), "new");
    }


}