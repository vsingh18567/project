import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataManager_createOrg_Test {
	
	/*
	 * This is a test class for the DataManager.createOrg method.
	 */

	@Test
	public void testSuccessfulCreation() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"login\":\"new\",\"password\":\"new\",\"name\":\"new org\",\"description\":\"this is the new org\",\"funds\":[],\"__v\":0}}";

			}
			
		});
		
		Organization o = dm.createOrg("new", "new","new org", "this is the new org");
		
		assertNotNull(o);
		assertEquals("this is the new org", o.getDescription());
		assertEquals("12345", o.getId());
		assertEquals("new org", o.getName());
		
	}
	@Test(expected = IllegalStateException.class)
	public void testFailedCreation() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"failure\",\"data\":{\"_id\":\"12345\",\"login\":\"new\",\"password\":\"new\",\"name\":\"new org\",\"description\":\"this is the new org\",\"funds\":[],\"__v\":0}}";

			}

		});

		dm.createOrg("new", "new","new fund", "this is the new fund");

	}

	@Test(expected = IllegalStateException.class)
	public void testNoStatus() {

		DataManager dm = new DataManager(new WebClient("localhost", 3002) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{}";
			}

		});;

		dm.createOrg("new", "new","new org", "this is the new org");

	}


}
