package models;

public class IssueModel {
	private String URL, title, body;
	public IssueModel(String u, String t, String b) {
		setURL(u);
		setTitle(t);
		setBody(b);
	}
	
	@Override
	public String toString() {
		return getURL() + "\n" + getTitle() + "\n" + getBody() + "\n---------------";
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
