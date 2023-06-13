# Additional Tasks
We would like to get the following additional tasks graded: 2.4, 2.5, 2.8, 2.9.

# Completed Tasks

## Task 2.1. Organization App caching
A cache called `contributorNameCache` is implemented for `getContributorName` using a HashMap stored in the DataManager class. It is checked before making a client request. It only caches the results of successful calls to the web client.

## Task 2.2. Organization App defensive programming
This required significant change to DataManager. Previously, errors resulted in functions returning `null` instead of erroring out. Changes have been made throughout `DataManager` to ensure all test cases pass. This work builds on the defensive programming that was already done in Phase 1. A wrapper called `makeRequestWrapper` was made over `client.makeRequest` that intertwines defensive programming into every client request. 

## Task 2.3. Organization App aggregate donations by contributor
Within UserInterface.displayFund(int fundNumber) method allowed user to choose if they want to display donations in chronological order or by contributor. 
Created new aggregateDonationsString(int fundNumber, List<Map.Entry<String, Long>> sortedDonations, Map<String, Integer> contributorNumDonations) method in UserInterface that creates a string to be printed if donations by contributor is chosen. This method uses two new methods in Fund class, which are public Map<String, Integer> getContributorNumDonations(), which creates a map of contributorName and corresponding number of donations to this fund, and List<Map.Entry<String, Long>> getContributorSumDonations(), which creates a list of contributors and their corresponding total donation value sorted in descending order of donation value. Added private Map<Integer, String> aggDonationsFundMap to UserInterface to cache the results of aggregateDonationsString method to avoid having to re-compute the aggregate donations info for a fund that has been viewed previously.
Also re-added code to show total donation amount from last week's task 1.3 as it was overwritten in our previous submission. Created getTotalDonation() in Fund class that calculates total donations. 

## Task 2.4. Contributor App caching
A cache named `fundNameCache` was added to `DataManager.java` as an instance variable, implemented as a HashMap with Strings for keys (ids) and values (names). Before every call to getFundName, we test to see if the `fundId` that was received from the JSON is already in the cache. If so, we skip calling getFundName and return the cached name. Otherwise, we call getFundName and if successful, add this information to the `fundNameCache`, so the API would not have be called in the next reference to this fund.

## Task 2.5. Contributor App defensive programming
DataManager changes
- Using the given robustness tests, each method in DataManager was altered to accomodate various cases such as inputs being null, critical variables being null, losing connection to the server, and invalid responses from the client. It was previously specified for the methods to return null if there were errors, but the updated specficiation given by the tests required that these errors were caught and to throw exceptions, so all DataManager methods were updated accordingly.

DataManager Unit Test Changes
- Due to the method specification previously saying that failed calls to the API should return null, previous unit tests included this in the expected output. In particular, old unit tests that raised an exception in the DataManager method were removed, because these test cases were identical to those the provided robustness tests, and other tests that returned an error message in JSON form were altered to fit the new specification.

## Task 2.8. Organization App logout/login
UserInterface.java: 
created a new login(String login, String password) name that creates a new DataManager and uses given login name and password to attemptLogin. Let's user re-enter username and password if login fails. Adjusted UserInteface.start() to give user possibility to logout (by choosing option -1), and adjusted main method to use new login(String login, String password) method and give user option to log back in after logging out or end program.

## Task 2.9. Organization App all contributions

The Organization App was enhanced to offer the user the option of listing all the contributions to the organization's funds. The option is available from the start menu, along with the options to select an existing fund or create a new fund. The Java code enhancementments that support this new feature include.

UserInterface.java
  start() - The code was enhanced to allow the user to select option of "$" to display all funds for the organization. For clarity, the organization name is now displayed in welcome message upon login to the application.
  In order to avoid changing the data type (int) for the option variable, an arbitrary int value (-99) was used to represent the option to display all donations for the organization.  

  displayAllDonations() - The fund name, donation amount, and donation date are displayed in a list of all donations for the organization when this method is called from start() method. A custom comparator (DonationDateComparator) is used to sort an ArrayList of donations by donation date in date descending order.

Organization.java
  getFundById() - This method was added to facilitate retrieval of the fund name for a donation, using the fundId which is accessible on a Donation object.

# Known bugs / issues
Usage of Double / Long for fund target. Our Organization app works with a long variable for target, but the admin app allows inputting a float value, an exception might be thrown if someone uses the web app to add a new fund. If the Organization app is used for adding a new fund, the user is prompted to re-enter a valid positive whole number if a float value is entered for target.

# Contributions of team members
Vikram completed tasks 2.1 and 2.2, Lisa completed tasks 2.3 and 2.8 and fixed some previous bugs, Grace completed task 2.4 and 2.5 and Michelle completed tasks 2.9.
For the write-up, everyone focused on the tasks they completed.

