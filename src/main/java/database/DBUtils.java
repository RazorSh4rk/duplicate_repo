package database;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import models.IssueModel;

public class DBUtils {
	
	private DB db;
	private ConcurrentMap map; 
	
	public DBUtils() {
		db = DBMaker.fileDB("src/main/java/databases/previous_hits.db").make();
		map = db.hashMap("map").createOrOpen();
	}
	
	public void destroy() {
		db.close();
	}
	
	public boolean recordExists(String record) {
		System.out.println("Checking " + record + "...");
		return !(map.get(record) == null);
	}
	
	public void put(String key, List<IssueModel> value) {
		String v = new Gson().toJson(value);
		System.out.println("Saving " + key + " : " + v);
		map.put(key, v);
	}
	
	public List<IssueModel> get(String key) {
		System.out.println("Returning " + map.get(key).toString());
		Type listType = new TypeToken<List<IssueModel>>(){}.getType();
		return new Gson().fromJson(map.get(key).toString(), listType);
	}
}
