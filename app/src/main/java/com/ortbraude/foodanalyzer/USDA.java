package com.ortbraude.foodanalyzer;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class USDA {
    private RequestQueue queue;
    public JSONArray newResponse;

    public USDA(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    private StringRequest searchNameStringRequest(String nameSearch) {
        final String API = "&api_key=<<https://api.nal.usda.gov/fdc/v1/foods/search?query=apple&pageSize=2&api_key=bjkUHzRmbXY63rgb8WOCEghtRg7PQ59odUCieLkR>>";
        final String NAME_SEARCH = "&q=";
        final String DATA_SOURCE = "&ds=Standard Reference";
        final String FOOD_GROUP = "&fg=";
        final String SORT = "&sort=r";
        final String MAX_ROWS = "&max=25";
        final String BEGINNING_ROW = "&offset=0";
        final String URL_PREFIX = "https://api.nal.usda.gov/ndb/search/?format=json";
        String url = URL_PREFIX + API + NAME_SEARCH + nameSearch + DATA_SOURCE + FOOD_GROUP + SORT + MAX_ROWS + BEGINNING_ROW;

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result = new JSONObject(response).getJSONObject("list");
                            int maxItems = result.getInt("end");
                            newResponse = result.getJSONArray("item");
                            // catch for the JSON parsing error
                        } catch (JSONException e) {

                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                    }
                });
    }

    public void searchFood(String food) {

        // cancelling all requests about this search if in queue
        queue.cancelAll(food);

        // first StringRequest: getting items searched
        StringRequest stringRequest = searchNameStringRequest(food);
        stringRequest.setTag(food);

        // executing the request (adding to queue)
        queue.add(stringRequest);
    }

}


