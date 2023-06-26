import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserInterface {

	private DataManager dataManager;
	private Organization org;
	private final Scanner in = new Scanner(System.in);
	private Map<Integer, String> aggDonationsFundMap;

	public UserInterface(DataManager dataManager, Organization org) {
		this.dataManager = dataManager;
		this.org = org;
		aggDonationsFundMap = new HashMap<>();
	}

	public void start() {
		System.out.println("\nWelcome to the Organization App for " + org.getName() + ".");
		while (true) {
			System.out.println("\n");
			if (org.getFunds().size() > 0) {
				System.out.println("There are " + org.getFunds().size() + " funds in this organization:");

				int count = 1;
				for (Fund f : org.getFunds()) {

					System.out.println(count + ": " + f.getName());

					count++;
				}
				System.out.println("\nEnter the fund number to see more information.");
				System.out.println("Enter $ to see all donations to this organization.");
			}
			System.out.println("Enter 0 to create a new fund");
			System.out.println("Enter -1 to logout.");
			System.out.println("Enter p to change password");
			System.out.println("Enter u to update organization info");
			int option = -2;

			boolean prompt = true;

			do {
				try {
					if (in.hasNextInt()) {
						option = in.nextInt();	

						if (option >= -1 && option <= org.getFunds().size()) {
							prompt = false;
							in.nextLine();
						} else {
							// request data re-entry due to invalid integer value
							System.out.println("Re-enter a listed fund number, $ to see all donations, -1 to logout, or 0 to create a new fund:");
							in.nextLine();
						}

					} else {
						String next = in.next();
						if (next.equals("$")) {
							option = -99;
							prompt = false;
						} else if (next.equals("p")) {
							option = -98;
							prompt = false;
						} else if (next.equals("u")) {
							option = -97;
							prompt = false;
						}
					}
				} catch (InputMismatchException ime) {
					
					// request data re-entry due to invalid data type
					System.out.println("Re-enter a listed fund number, $ to see all donations, -1 to logout, or 0 to create a new fund:");
					in.nextLine();
				}
			} while (prompt);

			if (option == 0) {
				createFund();
			} else if (option == -99) {
				displayAllDonations();
			} else if (option == -98) {
				changePassword();
			} else if (option == -97) {
				updateInfo();
			} else if (option == -1) {
				break;
			} else {
				displayFund(option);
			}
		}
	}

	private void updateInfo() {
		System.out.println("Enter existing password:");
		in.nextLine();
		String password = in.nextLine();
		boolean passwordCheck;
		try {
			passwordCheck = dataManager.checkPassword(org.getId(), password);
		} catch (Exception e) {
			System.out.println("Error in checking password. Would you like to retry operation? [y/n]");
			if (in.nextLine().equals("y")) {
				changePassword();
			}
			return;
		}
		if (passwordCheck) {
			System.out.println("Organization info:");
			System.out.println("- name: " + org.getName());
			System.out.println("- description: " + org.getDescription());
			String newName = null;
			String newDesc = null;
			System.out.println("Would you like to change name [y/n]?");
			String yes = in.nextLine();
			if (yes.equals("y")) {
				System.out.println("Enter new name:");
				newName = in.nextLine();
			}
			System.out.println("Would you like to change description [y/n]?");
			if (in.nextLine().equals("y")) {
				System.out.println("Enter new description:");
				newDesc = in.nextLine();
			}
			try {
				if (dataManager.updateInfo(org, newName, newDesc)) System.out.println("Successfully updated organization");
			} catch (Exception e) {
				System.out.println("Error updating info. Would you like to retry? [y/n]");
				if (in.nextLine().equals("y")) {
					updateInfo();
				}
			}
		} else {
			System.out.println("Wrong password entered");
		}
	}


	/*Comparator for sorting the donation list by donation date in descending order*/
	public static Comparator<Donation> DonationDateComparator = new Comparator<Donation>() {

		public int compare(Donation d1, Donation d2) {
		   LocalDateTime date1 = LocalDateTime.parse(d1.getDate().substring(0, 19));
		   LocalDateTime date2 = LocalDateTime.parse(d2.getDate().substring(0, 19));

		   return date2.compareTo(date1);
	    }
	};

	/*Display all donations for the organization*/    	
	private void displayAllDonations() {
		
		ArrayList<Fund> funds = new ArrayList<>(org.getFunds());
		ArrayList<Donation> donations = new ArrayList<>();
		
		for (Fund f : funds) {
			ArrayList<Donation> fundDonations = new ArrayList<>(f.getDonations());
			
			for (Donation fd : fundDonations) {
				donations.add(fd);
			}
		}
		
		donations.sort(DonationDateComparator);
		System.out.println("Number of donations: " + donations.size());
			for (Donation d : donations) {
				Fund donationFund = org.getFundById(d.getFundId());
				String donationFundName = "";
				if (donationFund != null) {
					donationFundName = donationFund.getName();
				}
				// display donation dates in display format (e.g., June 18, 2021)
				System.out.println("* " + donationFundName + ": $" + d.getAmount() + " on "
						+ d.getDateFormatted());
		}
		
		in.nextLine();		
	}

	public void createFund() {

		// prompt user for a fund name
		System.out.print("Enter the fund name: ");
		String name = in.nextLine().trim();

		// if invalid, request data re-entry
		while (name.trim().length() == 0) {
			System.out.println("Re-enter a fund name with alphanumeric characters:");
			name = in.nextLine().trim();
		}

		// prompt user for a fund description
		System.out.print("Enter the fund description: ");
		String description = in.nextLine().trim();

		// if invalid, request data re-entry
		while (description.trim().length() == 0) {
			System.out.println("Re-enter a fund description with alphanumeric characters:");
			description = in.nextLine().trim();
		}

		// prompt user for a fund target
		System.out.print("Enter the fund target: ");

		boolean prompt = true;

		long target = 0;

		do {
			try {
				target = in.nextInt();
				
				if (target > 0) {
					prompt = false;
				} else {
					// request data re-entry due to invalid value
					System.out.println("Re-enter a target value as a positive whole number:");					
				}
				in.nextLine();
			} catch (InputMismatchException ime) {
				// request data re-entry due to invalid value
				System.out.println("Re-enter a target value as a positive whole number:");
				in.nextLine();
			}
		} while (prompt);

		// create new fund with above information
		try {
			Fund fund = dataManager.createFund(org.getId(), name, description, target);
			org.getFunds().add(fund);
		} catch (Exception e) {
			System.out.println("Error in creating fund. Would you like to retry operation? [y/n]");
			if (in.nextLine().equals("y")) {
				createFund();
			}
		}
	}

	public void changePassword() {
		System.out.println("Enter existing password:");
		in.nextLine();
		String password = in.nextLine();
		boolean passwordCheck;
		try {
			passwordCheck = dataManager.checkPassword(org.getId(), password);
		} catch (Exception e) {
			System.out.println("Error in checking password. Would you like to retry operation? [y/n]");
			if (in.nextLine().equals("y")) {
				changePassword();
			}
			return;
		}
		if (passwordCheck) {
			System.out.println("Enter new password:");
			String newPassword1 = in.nextLine();
			System.out.println("Enter new password again:");
			String newPassword2 = in.nextLine();
			if (newPassword1.equals(newPassword2)) {
				boolean success = true;
				try {
					if (!dataManager.updatePassword(org.getId(), newPassword1))  {
						success = false;
					}
				} catch (Exception e) {
					success = false;
				}
				if (success) {
					System.out.println("Password successfully updated.");
				}
				else {
					System.out.println("Error in changing password. Would you like to retry operation? [y/n]");
					if (in.nextLine().equals("y")) {
						changePassword();
					}
				}
			} else {
				System.out.println("Error: Passwords don't match");
			}
		} else {
			System.out.println("Wrong password entered");
		}
	}

	private String aggregateDonationsString(int fundNumber, List<Map.Entry<String, Long>> sortedDonations, Map<String, Integer> contributorNumDonations) {
		if (!aggDonationsFundMap.containsKey(fundNumber)) {
			StringBuilder result = new StringBuilder();
			for (Map.Entry<String, Long> entry : sortedDonations) {
				String contributorName = entry.getKey();
				int numDonations = contributorNumDonations.get(contributorName);
				long sumDonations = entry.getValue();
				result.append(contributorName).append(", ")
						.append(numDonations).append(" donations, $")
						.append(sumDonations).append(" total")
						.append("\n");
			}
			aggDonationsFundMap.put(fundNumber, result.toString());
		}
		return aggDonationsFundMap.get(fundNumber);
		
	}

	public void displayFund(int fundNumber) {

		Fund fund = org.getFunds().get(fundNumber - 1);

		long totalDonation = fund.getTotalDonation();
		long target = fund.getTarget();

		System.out.println("\n\n");
		System.out.println("Here is information about this fund:");
		System.out.println("Name: " + fund.getName());
		System.out.println("Description: " + fund.getDescription());
		System.out.println("Target: $" + target);

		List<Donation> donations = fund.getDonations();
		System.out.println("Number of donations: " + donations.size());
		System.out.println("Total donation amount: $" + totalDonation + " (" + (totalDonation/target)*100 + "% of target)");
		
		System.out.println("\nEnter 'donation' to make a donation to this fund.");		

		System.out.println("\n" + //
				"You can display donations in chronological order or by contributor." + "\n" + //
				"Do you want to display donations in chronological order? y/n");
		
		String response1 = in.nextLine();
		
		if (response1.equals("y")) {
			System.out.println();
			for (Donation donation : donations) {
				// print donation dates in display format (e.g., June 18, 2021)
				System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on "
						+ donation.getDateFormatted());
			}
		} else if (response1.equals("donation")) {
			createDonation(fund.getId());
		}

		System.out.println("\n" + //
				"Do you want to display donations by contributor? y/n");
		
		String response2 = in.nextLine();
		
		if (response2.equals("y")) {
			System.out.println("\n" + aggregateDonationsString(fundNumber, fund.getContributorSumDonations(), fund.getContributorNumDonations()));
		} else if (response2.equals("donation")) {
			createDonation(fund.getId());
		}	

		System.out.println("Press the Enter key to go back to the listing of funds");
		in.nextLine();
	}
	
	private void createDonation(String fundId) {

		boolean promptContributorId = true;
		boolean promptDonationAmount = true;
		boolean retry = true;

		// 1 - collect contributor ID

		String contributorName = "";

		System.out.println("\nEnter a contributor ID: ");
		String contributorId = in.nextLine().trim();

		// if empty string, request data re-entry
		while (contributorId.trim().length() == 0) {
			System.out.println("Re-enter a contributor ID with alphanumeric characters:");
			contributorId = in.nextLine().trim();
		}

		// check that contributor ID exists
		while (promptContributorId) {
			contributorName = "";
			contributorName = contributorNameById(contributorId).trim();
			if (contributorName.length() > 0) {
				promptContributorId = false;
			} else {

				System.out.println("The contributor ID could not be located. Would you like to retry? [y/n]");

				if (in.nextLine().equals("n")) {
					return;
				}
			}
		}

		// 2 - collect donation amount

		System.out.println("Enter the donation amount: ");

		long donationAmount = 0;

		do {
			try {
				donationAmount = in.nextInt();

				if (donationAmount > 0) {
					promptDonationAmount = false;
				} else {
					// request data re-entry due to invalid value
					System.out.println("Re-enter a donation amount as a positive whole number:");					
				}
				in.nextLine();
			} catch (InputMismatchException ime) {
				// request data re-entry due to invalid value
				System.out.println("Re-enter a donation amount as a positive whole number:");
				in.nextLine();
			}
		} while (promptDonationAmount);	

		// 3 - attempt to save the donation

		Donation d = null;

		while (retry) {
			try {
				d = dataManager.attemptMakeDonation(contributorId, contributorNameById(contributorId).trim(), fundId, donationAmount);
				retry = false;
			} catch (Exception e) {
				System.out.println("Error in saving donation (" + e.toString() + "). Would you like to retry operation? [y/n]");
				if (in.nextLine().equals("n")) {
					return;
				}
			}
		}

		if (d != null) {
			Fund f = org.getFundById(fundId);

			//Donation d = new Donation(fundId, contributorName, donationAmount, Instant.now().toString());
			f.addDonation(d);

			System.out.println("The donation to " + org.getFundById(fundId).getName() + " was successfully saved.");

			return;
		} else {
			System.out.println("The donation was not saved due to a system problem. Would you like to retry operation? [y/n]");
			if (in.nextLine().equals("y")) {
				createDonation(fundId);
				return;
			} else {
				return;
			}
		}	
	}

	private String contributorNameById (String Id) {
		String contributorName = "";
		
		String contributorId = Id.trim();
		
		if (contributorId.length() == 0) {
			return "";
		}
		
			try {
				contributorName = dataManager.getContributorName(contributorId);
				if (contributorName.trim().length() == 0) {
					return "";
				} else {
					System.out.println("The contributor, " + contributorName + ", was located in the database.");
				}
			} catch (Exception e) {
				return "";
			}	

		return contributorName;
	}

	public boolean login() {
		dataManager = new DataManager(new WebClient("localhost", 3001));
		boolean retry = true;

		System.out.println("Please enter your login: ");
		String login = in.nextLine();
		System.out.println("Please enter your password: ");
		String password = in.nextLine();

		while (retry) {
			try {
				org = dataManager.attemptLogin(login, password);
				retry = false;
			} catch (Exception e) {
				System.out.println("Error in logging in. Would you like to retry operation? [y/n]");
				if (in.nextLine().equals("y")) {
					System.out.println("Please re-enter your login: ");
					login = in.nextLine();
					System.out.println("Please re-enter your password: ");
					password = in.nextLine();
				} else {
					retry = false;
				}
			}
		}
		if (org == null) {
			return false;
		} else {
			return true;
		}
		
	}

	public boolean createOrg() {
		dataManager = new DataManager(new WebClient("localhost", 3001));
		boolean retry = true;
		
		
		while (retry) {
				System.out.println("Please enter a login: ");
				String login = in.nextLine().trim();
				// if invalid, request data re-entry
				while (login.length() == 0 || dataManager.getOrg(login)) {
					if (login.length() == 0) {
						System.out.println("Re-enter a login with alphanumeric characters:");
						login = in.nextLine().trim();
					}
					if (dataManager.getOrg(login)) {
						System.out.println("Login already exists. Please try again.");
						login = in.nextLine().trim();
					}
				}
				System.out.println("Please enter a password: ");
				String password = in.nextLine().trim();
				// if invalid, request data re-entry
				while (password.length() == 0) {
					System.out.println("Re-enter a password with alphanumeric characters:");
					password = in.nextLine().trim();
				}				
				System.out.println("Please enter an organization name: ");
				String name = in.nextLine().trim();
				// if invalid, request data re-entry
				while (name.length() == 0) {
					System.out.println("Re-enter an organization name with alphanumeric characters:");
					name = in.nextLine().trim();
				}				
				System.out.println("Please enter a description: ");
				String description = in.nextLine().trim();	
				// if invalid, request data re-entry
				while (description.length() == 0) {
					System.out.println("Re-enter a description with alphanumeric characters:");
					description = in.nextLine().trim();
				}	
			try {
				org = dataManager.createOrg(login, password, name, description);
				retry = false;
			} catch (Exception e) {
				System.out.println("Error communicating with API. Would you like to retry operation? [y/n]");
				if (!in.nextLine().equals("y")) {
					retry = false;
				}
			}
		}
		if (org == null) {
			return false;
		} else {
			return true;
		}
		
	}

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		UserInterface ui = new UserInterface(null, null);

		System.out.println("Welcome to the Organization App.");
		System.out.println("Enter key for options");
		System.out.println("- e (log into an existing organization)");
		System.out.println("- n (create a new organization)");

		String answer = in.nextLine();
		while (true) {
			if (answer.equals("n") || answer.equals("e")) {
				if (answer.equals("n")) {

					if (!ui.createOrg()) {
						System.out.println("Creating new Organization failed.");
						break;
					} else {
						ui.start();
					}

				} else if (answer.equals("e")) {
					if (!ui.login()) {
						System.out.println("Login failed.");
						break;
					} else {
						ui.start();
					}
				}

				System.out.println("Do you want to log back in? y/n");
				if (in.nextLine().equals("y")) {
					ui = new UserInterface(null, null);
					System.out.println("Would you like to log in with an existing organization or create a new organization?\n" + //
						"Enter e for existing or n for new organization: ");
					answer = in.nextLine();
				} else {
					System.out.println("Thanks for using Organization App.");
					break;
				}

			} else {
				System.out.println("Invalid response, please try again.\n" + //
						"Enter e for existing or n for new organization: ");
				answer = in.nextLine();
			}
		}
		
		in.close();
	}
	
}
