package http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.IssueModel;

public class HttpUtils {
	public static String GET(String URL){
        String responseBody = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(URL/* + "?page=" + page*/);
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseBody = httpclient.execute(httpget, responseHandler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {

            }
        }
        return responseBody;
    }
	
	public static List<IssueModel> getAllIssues(JsonParser parser, String REPO){
		ArrayList<IssueModel> ret = new ArrayList<IssueModel>();
		for(int i = 0; true; i++) {
			String issues0 = "https://api.github.com/repos/";
			String issues1 = "/issues?sort=comments&state=all&page=" + i;
			
			System.out.print(issues0 + REPO + issues1 + "\t result: ");
			String res = HttpUtils.GET(issues0 + REPO + issues1);
			System.out.println(res.length() + " bytes");
			
			if(res.length() < 100 || i > 1) break;
			
			JsonArray resJson = (parser.parse(res)).getAsJsonArray();
			JsonObject resObj = (parser.parse(resJson.get(3).toString())).getAsJsonObject();
			for(JsonElement je : resJson) {
				JsonObject jo = parser.parse(je.toString()).getAsJsonObject();
				String u = jo.get("url").toString().replace("/api", "").replace("/repos", "");
				System.out.println(u);
				ret.add( new IssueModel(u, jo.get("title").toString(), jo.get("body").toString()) );
			}
		}
		
		return ret;
	}
}
