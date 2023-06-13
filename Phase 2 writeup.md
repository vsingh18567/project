# Additional Tasks
We would like to get the following additional tasks graded: 2.4, 2.5, 2.8, 2.9.

# Completed Tasks

## Task 2.1. Organization App caching


## Task 2.2. Organization App defensive programming


## Task 2.3. Organization App aggregate donations by contributor


## Task 2.4. Contributor App caching


## Task 2.5. Contributor App defensive programming


## Task 2.8. Organization App logout/login


## Task 2.9. Organization App all contributions

The Organization App was enhanced to offer the user the option of listing all the contributions to the organization's funds. The option is available from the start menu, along with the options to select an existing fund or create a new fund. The Java code enhancementments that support this new feature include.

UserInterface.java
  start() - The code was enhanced to allow the user to select option of "$" to display all funds for the organization. For clarity, the organization name is now displayed in welcome message upon login to the application.
  In order to avoid changing the data type (int) for the option variable, an arbitrary int value (-99) was used to represent the option to display all donations for the organization.  

  displayAllDonations() - The fund name, donation amount, and donation date are displayed in a list of all donations for the organization when this method is called from start() method. A custom comparator (DonationDateComparator) is used to sort an ArrayList of donations by donation date in date descending order.

Organization.java
  getFundById() - This method was added to facilitate retrieval of the fund name for a donation, using the fundId which is accessible on a Donation object.

# Contributions of team members
Vikram completed tasks 2.1 and 2.2, Lisa completed tasks 2.3 and 2.8 and fixed some previous bugs, Grace completed task 2.4 and 2.5 and Michelle completed tasks 2.9.
For the write-up, everyone focused on the tasks they completed.

