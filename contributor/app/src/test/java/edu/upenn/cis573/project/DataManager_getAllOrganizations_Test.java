package edu.upenn.cis573.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DataManager_getAllOrganizations_Test {

    @Test
    public void testSuccess() {
        String org_id = "0";
        String fund_id = "0";
        String name = "n";
        String fund_name = "n";
//        double target = 200;
//        double total = 100;
        int target = 200;
        int total = 100;
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":[" +
                        "{\"_id\":\"" + org_id + "\",\"name\":\"" + name + "\",\"funds\":[{" +
                        "\"_id\":\"" + fund_id + "\",\"name\":\"" + fund_name + "\"," +
                        "\"target\":" + target + ",\"totalDonations\":" + total + "}]}" +
                        "]}";
            }
        });
        double epsilon = 1e-6;

        List<Organization> organizationList = dm.getAllOrganizations();
        assertEquals(1, organizationList.size());
        for (Organization o : organizationList) {
            assertEquals(org_id, o.getId());
            assertEquals(name, o.getName());
            List<Fund> fundsList = o.getFunds();
            assertEquals(1, fundsList.size());
            for (Fund f : fundsList) {
                assertEquals(fund_id, f.getId());
                assertEquals(fund_name, f.getName());
                assertEquals(target, f.getTarget(), epsilon);
                assertEquals(total, f.getTotalDonations(), epsilon);
            }
        }

    }

    @Test
    public void testErrorInQuery() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":\"err\"}";
            }
        });

        List<Organization> organizationList = dm.getAllOrganizations();
        assertNull(organizationList);
    }

    @Test
    public void testExceptionInMethod() {
        DataManager dm = new DataManager(new WebClient(null, 0) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });

        List<Organization> organizationList = dm.getAllOrganizations();
        assertNull(organizationList);
    }


}
