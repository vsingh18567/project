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

		while (true) {
			System.out.println("\n\n");
			if (org.getFunds().size() > 0) {
				System.out.println("There are " + org.getFunds().size() + " funds in this organization:");

				int count = 1;
				for (Fund f : org.getFunds()) {

					System.out.println(count + ": " + f.getName());

					count++;
				}
				System.out.println("Enter the fund number to see more information.");
			}
			System.out.println("Enter 0 to create a new fund");

			int option = -1;

			boolean prompt = true;

			do {
				try {
					option = in.nextInt();

					if (option >= 0 && option <= org.getFunds().size()) {
						prompt = false;
					} else {
						// request data re-entry due to invalid integer value
						System.out.println("Re-enter a listed fund number or 0 to create a new fund:");
					}
					in.nextLine();
				} catch (InputMismatchException ime) {
					// request data re-entry due to invalid data type
					System.out.println("Re-enter a listed fund number or 0 to create a new fund:");
					in.nextLine();
				}
			} while (prompt);

			if (option == 0) {
				createFund();
			} else {
				displayFund(option);
			}
		}
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

	public static void main(String[] args) {

		DataManager ds = new DataManager(new WebClient("localhost", 3001));
		Scanner in = new Scanner(System.in);

		String login = args[0];
		String password = args[1];
		Organization org = null;
		boolean retry = true;
		while (retry) {
			try {
				org = ds.attemptLogin(login, password);
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
			System.out.println("Login failed.");
		} else {
			UserInterface ui = new UserInterface(ds, org);

			ui.start();

		}
		in.close();
	}
	
}
