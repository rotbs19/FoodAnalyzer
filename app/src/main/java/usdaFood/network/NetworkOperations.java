package usdaFood.network;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface NetworkOperations {
	JSONObject sendGet(String url) throws IOException, JSONException;
}
