# Additional Tasks
We would like to get the following additional tasks graded: 1.4, 1.5, xxx

# Completed Tasks
### what to add here: For each task you completed in this phase (Required or Additional), a brief description of the changes you made to the provided code, e.g. the names of any classes or methods that were changed, new methods that were created, etc.

## Task 1.1. Organization (Java) App testing

## Task 1.2. Organization (Java) App debugging
Bugs
- `org/DataManger.java:attemptLogin`: misspelling of description
- JSON parses decimals as doubles not floats. For ease, the `Fund` class is altered to use doubles throughout the codebase
- `org/DataManager.java:attemptLogin` Organization must have id: Return null if id is missing.
- There must be a status: Return null if no status
- `org/DataManager.java:attemptLogin`: If funds is null, don't iterate over it but still return object
- `org/DataManager.java:createFund`: Check for nullity before making request
- `org/DataManager.java:getContributorName`: Check for id nullity before making request

## Task 1.3. Organization (Java) App display total donations for fund
To display total donations, the displayFund(fundNumber) method was adjusted. Within the for-loop that prints out each donation, the amount of each donation is now also being added to a new variable totalDonation. The amount is then divided by the value of fund.getTarget() to get the percentage of target that has been reached.  

## Task 1.4. Contributor (Android) App testing and debugging
Bugs
- `contributor/DataManger.java:getFundName`: typo in fund-not-found case
- `contributor/DataManger.java:getFundName`: when query returns status 'error', consider it as an error occuring and return null for the method rather than 'Unknown fund'
- JSON stores Number data types as 64-bit floating points. As such, changed some object fields to doubles so as to minimize accuracy loss. Total changes:
  - `contributor/Donation.java`: Changed amount from long to double.
  - `contributor/Fund.java`: Changed target and totalDonations from long to double.
  - `contributor/DataManager.java:attemptLogin`: Adjusted JSON parsing to accomodate above changes.
  - `contributor/DataManager.java:getAllOrganizations`: Adjusted JSON parsing to accomodate above changes.

## Task 1.5. Contributor (Android) App display total donations
To display total donations, the onResume() method in the ViewDonationsActivity class was adjusted. Within the for-loop that adds all donations to the donations array, the amount of each donation is now also being added to a new variable totalDonations. totalDonations is then added to the donations array in order to display it as another entry in the ListView.

## Task 1.7. Organization (Java) App input error handling
The following bulletproofing measures were implemented in torder to prevent crashes in the org/UserInterface.java start() and createFund() methods. For all invalid entries, the user is prompted to re-enter a valid input.

start()method:
- restrict input to a listed fund number or 0 to create a new fund.

createFund() method:
- disallow empty string or entries with only space characters for fund name and fund description
- restrict fund target to positive whole numbers 

## Task 1.9 - Date formatting
A new method, getDateFormatted(), was added to the org/Donation.java class to return a string with "MMM d, yyyy" format. This formatted date string was then used in the displayFund() routine, in lieu of the default UTC format, to display donation date in the fund donations list.

### what to add here - optional: Any known bugs or other issues with the tasks you attempted in this phase.
### what to add here - optional: Instructions on how to start each app, if you changed anything from the original version of the code, e.g. the name of the Java main class or JavaScript entry point, arguments to the programs, etc. If you did not change anything, you may omit this.

# Contributions of team members
Vikram completed tasks 1.1 and 1.2, Lisa completed tasks 1.3 and 1.5, Grace completed task 1.4 and Michelle completed tasks 1.7 and 1.9.
Vikram also took the lead in creating the github repo, Michelle in setting up a first team meeting. 
For the write-up, everyone focused on the tasks they completed.




