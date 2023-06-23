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
	private Scanner in = new Scanner(System.in);
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
					
					} else if (in.next().equals("$")) {
						    // assign -99 as the option for displaying all donations for the organization
							option = -99;

							prompt = false;
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
			} else if (option == -1) {
				break;
			} else {
				displayFund(option);
			}
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

		System.out.println("\n" + //
				"You can display donations in chronological order or by contributor." + "\n" + //
				"Do you want to display donations in chronological order? y/n");
		if (in.nextLine().equals("y")) {
			System.out.println();
			for (Donation donation : donations) {
				// print donation dates in display format (e.g., June 18, 2021)
				System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on "
						+ donation.getDateFormatted());
			}
		}

		System.out.println("\n" + //
				"Do you want to display donations by contributor? y/n");
		if (in.nextLine().equals("y")) {
			System.out.println("\n" + aggregateDonationsString(fundNumber, fund.getContributorSumDonations(), fund.getContributorNumDonations()));
		}
		

		System.out.println("Press the Enter key to go back to the listing of funds");
		in.nextLine();
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
				System.out.println("Please enter a name: ");
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
		System.out.println("Would you like to log in with an existing organization or create a new organization?\n" + //
					"Enter e for existing or n for new organization: ");

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
