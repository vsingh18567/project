import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Fund {

	private String id;
	private String name;
	private String description;
	private long target;
	private List<Donation> donations;
	private long totalDonation;
	
	public Fund(String id, String name, String description, long target) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.target = target;
		donations = new LinkedList<>();
		totalDonation = 0;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getTarget() {
		return target;
	}

	public long getTotalDonation() {
		totalDonation = 0;
		for (Donation donation : donations) {
			totalDonation += donation.getAmount();
		} 
		return totalDonation;
	}
	
	protected void addDonation(Donation d) {
		if (d != null) {
			donations.add(d);
		}
	}

	public Map<String, Integer> getContributorNumDonations() {
		Map<String, Integer> contributorNumDonations = new HashMap<>();
		for (Donation donation : donations) {
            String contributorName = donation.getContributorName();
            int numDonations = contributorNumDonations.getOrDefault(contributorName, 0) + 1;
            
            contributorNumDonations.put(contributorName, numDonations);
        }
		return contributorNumDonations;
	}

	public List<Map.Entry<String, Long>> getContributorSumDonations() {
		
        Map<String, Long> contributorSumDonations = new HashMap<>();

        for (Donation donation : donations) {
            String contributorName = donation.getContributorName();
            long sumDonations = contributorSumDonations.getOrDefault(contributorName, 0L) + donation.getAmount();

            contributorSumDonations.put(contributorName, sumDonations);
        }

        List<Map.Entry<String, Long>> sortedDonations = contributorSumDonations.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        

        return sortedDonations;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	
	public List<Donation> getDonations() {
		return donations;
	}
	
	
	
}
