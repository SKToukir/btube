package bdtube.vumobile.com.bdtube.NetworkOpration;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bdtube.vumobile.com.bdtube.Holder.AllHomeVideo;
import bdtube.vumobile.com.bdtube.Model.HomeVideoList;
import bdtube.vumobile.com.bdtube.app.AppController;

/**
 * Created by IT-10 on 12/31/2015.
 */
public class ParseDataJson {


    public void makeJsonArrayRequest(final Context context, String url) {
       /* CustomDialog customDialog = new CustomDialog();
        customDialog.ProgressDialog(context, progressDialog);*/

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject homeURL = (JSONObject) response
                                        .get(i);
                                HomeVideoList homeVideoList = new HomeVideoList();
                                AllHomeVideo allHomeVideo = new AllHomeVideo();


                                String ContentTitle = homeURL.getString("ContentTitle");
                                String PreviewURL = homeURL.getString("PreviewURL");
                                String VideoURL = homeURL.getString("VideoURL");

                                homeVideoList.setContentTitle(ContentTitle);
                                homeVideoList.setPreviewURL(PreviewURL);
                                homeVideoList.setVideoURL(VideoURL);

                                Log.d("JSON data", ContentTitle + "CategoryCode: " + PreviewURL);


                                allHomeVideo.setHomeVideoList(homeVideoList);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                 /*       CustomDialog customDialog = new CustomDialog();
                     customDialog.hideProgressDialog(context, progressDialog);*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());

                //hideProgressDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
}