import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class DataManager_attemptLogin_Test {

    @Test(expected = IllegalStateException.class)
    public void testFailure() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"failure\"}";
            }
        });
        dm.attemptLogin("hi", "there");
    }

    @Test
    public void testSimpleSuccess() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"1\",\"name\":\"hello\",\"description\":\"afund\",\"funds\":[]}}";
            }
        });
        Organization org = dm.attemptLogin("name", "pass");
        assertNotNull(org);
        assertEquals("1", org.getId());
        assertEquals("hello", org.getName() );
        assertEquals("afund", org.getDescription());
        assertEquals(0, org.getFunds().size());
    }

    @Test
    public void testFundsList() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"1\",\"name\":\"hello\",\"description\":\"afund\",\"funds\":[{\"_id\":\"1\",\"name\":\"fund1\",\"description\":\"fund\",\"target\":123.4,\"donations\":[{\"contributor\":\"me\",\"amount\":1,\"date\":\"1/1/01\",}]}]}}";
            }
        })
        {
            @Override
            public String getContributorName(String id) {return "me";}
        };
        Organization org = dm.attemptLogin("name", "pass");
        assertNotNull(org);
        List<Fund> funds = org.getFunds();
        assertEquals(1, funds.size());
        Fund fund = funds.get(0);
        assertEquals("1", fund.getId());
        assertEquals("fund1", fund.getName());
        assertEquals("fund", fund.getDescription());
        assertEquals(123.4, fund.getTarget(), 0);
        List<Donation> donations = fund.getDonations();
        assertEquals(1, donations.size());
        assertEquals("me", donations.get(0).getContributorName());
        assertEquals(1, donations.get(0).getAmount());
        assertEquals("1/1/01", donations.get(0).getDate());

    }

    @Test(expected = IllegalStateException.class)
    public void testException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3002) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{}";
            }

        });;

        Organization org = dm.attemptLogin("name", "pass");


    }

    @Test
    public void testOrgMustHaveID() {
        DataManager dm = new DataManager(new WebClient("localhost", 3002) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"name\":\"hello\",\"description\":\"afund\",\"funds\":[{\"_id\":\"1\",\"name\":\"fund1\",\"description\":\"fund\",\"target\":123.4,\"donations\":[{\"contributor\":\"me\",\"amount\":1,\"date\":\"1/1/01\",}]}]}}";

            }

        });;

        Organization org = dm.attemptLogin("name", "pass");

        assertNull(org);

    }

    @Test
    public void testFundsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3002) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"hello\",\"name\":\"hello\",\"description\":\"afund\"}}";

            }
        });;

        Organization org = dm.attemptLogin("name", "pass");

        assertNotNull(org);


    }

}
