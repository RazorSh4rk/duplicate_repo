package server;

import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import http.HttpUtils;

import static spark.Spark.before;

import Models.DataModel;

public class API {
	public API() {
		port(8081);
		setUpEndPoint();
	}
	
	private void setUpEndPoint() {
		/* CORS */
		options("/*", (request, response) -> {
			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});

		before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

		/*  */

		get("/ping", ((request, response) -> {
			System.out.println("pinged");
			return "pong";
		}));
		
		post("/detect", ((request, response) -> {
			System.out.println(".");
			JsonParser parser = new JsonParser();
					
			JsonObject jsonObject = (parser.parse(request.body())).getAsJsonObject();
			String URL = jsonObject.get("URL").toString();
			String REPO = URL.replace("https://github.com/", "").replaceAll("\"", "");
			String TEXT = jsonObject.get("TEXT").toString();
			
			ArrayList<DataModel> ret = new ArrayList<DataModel>();
			for(int i = 0; true; i++) {
				String issues0 = "https://api.github.com/repos/";
				String issues1 = "/issues?sort=comments&state=all&page=" + i;
				
				System.out.print(issues0 + REPO + issues1 + "\n result: ");
				String res = HttpUtils.GET(issues0 + REPO + issues1);
				System.out.println(res.length() + " bytes");
				
				if(res.length() < 100 || i > 9) break;
				
				JsonArray resJson = (parser.parse(res)).getAsJsonArray();
				JsonObject resObj = (parser.parse(resJson.get(3).toString())).getAsJsonObject();
				//TODO modularize this
				ret.add(new DataModel( resObj.get("url").toString(), resObj.get("title").toString(), resObj.get("body").toString() ) );
			}
			String e = "";
			for(DataModel i : ret) { e += i.toString(); }
			return e;
		}));
	}
}
