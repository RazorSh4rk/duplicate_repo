import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import database.DBUtils;
import models.IssueModel;
import server.API;

public class Main {

	public static void main(String args[]) {
		API api = new API();
		/*DBUtils db = new DBUtils();
		String s = db.get("srsLTE/srsLTE").toString();
		
		DB db = DBMaker.fileDB("src/main/java/databases/previous_hits.db").make();
		ConcurrentMap map = db.hashMap("map").createOrOpen();

		System.out.println(db.get("srsLTE/srsLTE"));
		//Type listType = new TypeToken<List<IssueModel>>(){}.getType();
		//List<IssueModel> l = new Gson().fromJson((String)map.get("srsLTE/srsLTE"), listType);
		
		for(IssueModel m : db.get("srsLTE/srsLTE")) {
			System.out.println(m);
		}
		db.destroy();*/
	}
}