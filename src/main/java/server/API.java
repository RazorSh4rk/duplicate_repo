package server;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import database.DBUtils;
import http.HttpUtils;
import lucene.TextUtils;
import models.IssueModel;

public class API {
	public API(){
		port(8081);
		setUpEndPoints();
	}

	private void setUpEndPoints() {
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
		before((request, response) -> response.header("Content-Security-Policy", "connect-src http://localhost:*/"));
		before((request, response) -> response.type("application/json"));

		/*  */

		get("/ping", ((request, response) -> {
			System.out.println(request.host());
			return "pong";
		}));

		get("/results", ((request, response) -> {
			response.type("text/html");
			String raw = new Scanner(new File("src/main/java/templates/index.html")).useDelimiter("\\Z").next();

			String url = request.queryParams("url");
			String text = request.queryParams("text");
			
			return raw
					.replace("$$url", url)
					.replace("$$text", text);
		}));

		post("/detect", ((request, response) -> {
			JsonParser parser = new JsonParser();

			JsonObject jsonObject = (parser.parse(request.body())).getAsJsonObject();
			String URL = jsonObject.get("URL").toString();
			String REPO = URL.replace("https://github.com/", "").replace("/issues/new", "").replaceAll("\"", "");
			String TEXT = jsonObject.get("TEXT").toString();

			//return (URL + "\n" + TEXT);
			
			DBUtils db = new DBUtils();
			if(db.recordExists(REPO)) {
				List<IssueModel> ret = db.get(REPO);
				db.destroy();
		
				return new Gson().toJson(new TextUtils().getSimilarResults(TEXT, ret));
			
				
			}else {
				List<IssueModel> allIssues = HttpUtils.getAllIssues(parser, REPO);
				db.put(REPO, allIssues);
				db.destroy();
				return new Gson().toJson(new TextUtils().getSimilarResults(TEXT, allIssues));
			}
		}));
	}
}
