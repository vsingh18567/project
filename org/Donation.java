import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

public class Donation {
	
	private String fundId;
	private String contributorName;
	private long amount;
	private String date;
	
	public Donation(String fundId, String contributorName, long amount, String date) {
		this.fundId = fundId;
		this.contributorName = contributorName;
		this.amount = amount;
		this.date = date;
	}

	public String getFundId() {
		return fundId;
	}

	public String getContributorName() {
		return contributorName;
	}

	public long getAmount() {
		return amount;
	}

	public String getDate() {
		return date;
	}
	
	public String getDateFormatted() {
		LocalDate temp = LocalDate.parse(date.substring(0, 10));
		
		DateTimeFormatter f = DateTimeFormatter.ofPattern("MMMM d, yyyy");
		
		return temp.format(f);
	}
}
