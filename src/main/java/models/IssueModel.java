package models;

public class IssueModel {
	public String URL, title, body;
	public IssueModel(String u, String t, String b) {
		URL = u;
		title = t;
		body = b;
	}
	
	@Override
	public String toString() {
		return URL + "\n" + title + "\n" + body;
	}
}
