import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_getOrg_Test {

    @Test(expected = IllegalStateException.class)
    public void testFailure() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });
        dm.getOrg("hi");
    }

    @Test
    public void testSimpleSuccess() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"login\":\"new\",\"password\":\"new\",\"name\":\"new org\",\"description\":\"this is the new org\",\"funds\":[],\"__v\":0}}";
            }
        });
        assertTrue(dm.getOrg("new"));
    }

    @Test
    public void testOrgNotFound() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"login\":\"new\",\"password\":\"new\",\"name\":\"new org\",\"description\":\"this is the new org\",\"funds\":[],\"__v\":0}}";
            }
        });
        assertFalse(dm.getOrg("hello"));
    }

    @Test(expected = IllegalStateException.class)
    public void testException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3002) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{}";
            }

        });;

        dm.getOrg("login");


    }


}
