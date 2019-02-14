package Models;

public class DataModel {
	public String URL, title, body;
	public DataModel(String u, String t, String b) {
		URL = u;
		title = t;
		body = b;
	}
	
	@Override
	public String toString() {
		return URL + "\n" + title + "\n" + body;
	}
}
