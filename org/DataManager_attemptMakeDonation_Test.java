
	import static org.junit.Assert.*;

	import java.util.Map;

	import org.junit.Test;

public class DataManager_attemptMakeDonation_Test {
		 
		/*
		 * This is a test class for the DataManager.attemptMakeDonation method.
		 */ 

		@Test
		public void testSuccessfulCreation() {

			DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
				@Override
				public String makeRequest(String resource, Map<String, Object> queryParams) {
					return "{\"data\":{\"date\":\"2023-06-26T02:08:26.916Z\",\"amount\":10,\"contributor\":\"647d10b20a3b770a28abf4ac\",\"fund\":\"6498e27ffa9aaf38c4145111\",\"__v\":0,\"_id\":\"6498f31afa9aaf38c4145121\"},\"status\":\"success\"}";
				}
			
			});
			
			String response = dm.attemptMakeDonation("647d10b20a3b770a28abf4ac", "6498e27ffa9aaf38c4145111", 10);
			
			assertEquals("success", response);		
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void testNullContributor() {

			DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			});

			dm.attemptMakeDonation("12345", "6498e27ffa9aaf38c4145111", -1);
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void testNullFund() {

			DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			});
 
			dm.attemptMakeDonation(null, "6498e27ffa9aaf38c4145111", 10);
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void testNegativeDonationAmount() {

			DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			});

			dm.attemptMakeDonation("12345", "6498e27ffa9aaf38c4145111", -1);
		}
		
		@Test(expected = IllegalArgumentException.class)
		public void testAllInvalidArguments() {

			DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			});

			dm.attemptMakeDonation(null, null, 0);
		}

		@Test(expected = IllegalStateException.class)
		public void testNoStatus() {

			DataManager dm = new DataManager(new WebClient("localhost", 3002) {

				@Override
				public String makeRequest(String resource, Map<String, Object> queryParams) {
					return "{}";
				}
			});

			dm.attemptMakeDonation("647d10b20a3b770a28abf4ac", "6498e27ffa9aaf38c4145111", 10);
		}
		
		@Test(expected = IllegalStateException.class)
		public void testParseException() {

			DataManager dm = new DataManager(new WebClient("localhost", 3002) {

				@Override
				public String makeRequest(String resource, Map<String, Object> queryParams) {
					return  "{\"data\":{\"date\":\"2023-06-26T02:08:26.916Z\",xxxxxxxxxxx,\"fund\":\"6498e27ffa9aaf38c4145111\",\"__v\":0,\"_id\":\"6498f31afa9aaf38c4145121\"},\"status\":\"success\"}";
				}

			});

			dm.attemptMakeDonation("647d10b20a3b770a28abf4ac", "6498e27ffa9aaf38c4145111", 10);
		}
}
