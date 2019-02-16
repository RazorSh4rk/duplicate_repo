package server;

import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.*;

import http.HttpUtils;
import models.IssueModel;
import lucene.TextUtils;

import static spark.Spark.before;

public class API {
	public API() {
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

		/*  */

		get("/ping", ((request, response) -> {
			return "pong";
		}));
		
		post("/detect", ((request, response) -> {
			System.out.println(".");
			JsonParser parser = new JsonParser();
					
			JsonObject jsonObject = (parser.parse(request.body())).getAsJsonObject();
			String URL = jsonObject.get("URL").toString();
			String REPO = URL.replace("https://github.com/", "").replace("/issues/new", "").replaceAll("\"", "");
			String TEXT = jsonObject.get("TEXT").toString();
			
			List<IssueModel> allIssues = HttpUtils.getAllIssues(parser, REPO);
			return new Gson().toJson(new TextUtils().getSimilarResults(TEXT, allIssues));
		}));
	}
}
