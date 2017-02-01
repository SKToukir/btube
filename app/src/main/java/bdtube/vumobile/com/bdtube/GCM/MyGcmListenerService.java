/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bdtube.vumobile.com.bdtube.GCM;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.adplaybind.ServiceHandler;
import com.google.android.gms.gcm.GcmListenerService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bdtube.vumobile.com.bdtube.R;
import bdtube.vumobile.com.bdtube.SoapCall.RequiredUserInfo;
import bdtube.vumobile.com.bdtube.SplashActivity;
import bdtube.vumobile.com.bdtube.VideoViewActivity;
import bdtube.vumobile.com.bdtube.util.PHPRequest;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private static final String TAG_CONTACTS = "Table";
    public String image_title = "";
    public String ImageURL = "";
    JSONArray contacts = null;
    public String message1="";

    private static String url2 = "http://wap.shabox.mobi/GCMPanel/PushResponsebdtube.aspx";
    public static String replaceAllurl="";
    public static String ArtistName="";
    public static String getContentTitle="";
    private static NotificationManager mNotificationManager;

    public String pushResponseUrl = "http://203.76.126.210/sticker_gcm_server_bdtube/push_response.php";
    public static String resultMno = null;
    PHPRequest phpRequest = new PHPRequest();
    /**
     *
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("price");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        message1=message;
        if (!message1.equals("")) {
            new GetContacts().execute();
        }

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        //sendNotification(message);
        // [END_EXCLUDE]
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url2, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.d("Response object: ", "> " + jsonObj);
                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                    Log.d("Response array: ", "> " + contacts);
                    // looping through All Contacts

                    JSONObject c = contacts.getJSONObject(0);
                    Log.d("Response next object: ", "> " + c);
                    String Image_url = c.getString("ContentImage");
                    Log.d("Response sample_url ", "> " + Image_url);
                    String image_title1 = c.getString("ContentName");
                    Log.d("Response image_title ", "> " + image_title1);
                    image_title=image_title1.replaceAll("_", " ");

                    String PfileName = c.getString("PfileName");
                    String artist1 = c.getString("artist");
                   String artist=artist1.replaceAll("_", " ");
                    String SongURL="http://202.164.213.242/CMS/Content/Graphics/Video%20Clips/D480x320/"+PfileName+""+".mp4"+"";
                    replaceAllurl=SongURL.replaceAll(" ", "");

                    Log.d("replaceAllurl",replaceAllurl);

                    setCustomViewNotification(MyGcmListenerService.this, message1, Image_url,replaceAllurl,image_title,artist);

                    URL url = null;


                    try {
                        url = new URL(Image_url);
                    } catch (MalformedURLException e) {
                        System.out.println("The URL is not valid.");
                        System.out.println(e.getMessage());
                    }

                    if (url != null) {
                        ImageURL=url.toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // setCustomViewNotification(context, message1);
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("Response onPostExecute ", "> " + "onPostExecute");
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setCustomViewNotification(Context context, String sms, String Image,String replaceAllurl,String image_title, String artist) {

        Bitmap remote_picture = null;

        try {
            //Log.d("Response Tariqul sample_url", ImageURL.toString());
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(Image).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        String strDate = sdf.format(c.getTime());

        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an explicit intent for an ResultActivity to receive.
        Intent resultIntent = new Intent(context, VideoViewActivity.class);

        resultIntent.putExtra("DoDownload", "4");
        resultIntent.putExtra("ArtistName", artist);
        resultIntent.putExtra("videoURL",replaceAllurl );
        resultIntent.putExtra("getContentTitle",image_title );
        resultIntent.putExtra("MenuFree","" );
        resultIntent.putExtra("MobileNumber","" );
        resultIntent.putExtra("MenuTitle","Home" );
     /*   DownloadTask downloadTask = new DownloadTask();

        *//** Starting the task created above *//*
        downloadTask.execute(Image);*/
        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(VideoViewActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.notification_custom_remote);
        SendLaunchPushRes();
        Intent volume = new Intent(context, VideoViewActivity.class);
        volume.putExtra("do_action", "play");
        volume.putExtra("ArtistName", artist);
        volume.putExtra("videoURL",replaceAllurl );
        volume.putExtra("getContentTitle",image_title );
        volume.putExtra("MenuFree","notification" );
        volume.putExtra("MobileNumber",resultMno );
        volume.putExtra("MenuTitle","Home" );
        PendingIntent pVolume = PendingIntent.getActivity(context, 1, volume, 0);
        expandedView.setOnClickPendingIntent(R.id.MainlayoutCustom, pVolume);


        // new serverAccessAsynTask().execute("");

        expandedView.setTextViewText(R.id.text_view, sms);

        expandedView.setTextViewText(R.id.notificationTime, strDate);

        try {
            expandedView.setImageViewBitmap(R.id.imageViewTest, remote_picture );
        }catch (Exception e){

            e.printStackTrace();
        }

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(remote_picture)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(sms)

                        //  .setDeleteIntent(pendintIntent)
                .build();

        notification.bigContentView = expandedView;

        notification.defaults |= Notification.DEFAULT_SOUND;
        mNotificationManager.notify(0, notification);
    }


    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.notification_logo : R.drawable.logo;
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(message)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public  void SendLaunchPushRes(){
        new SendLaunchPushResponse().execute();
    }

    private class SendLaunchPushResponse extends AsyncTask<String, String, String> {


        RequiredUserInfo userinfo = new RequiredUserInfo();
        String HS_MANUFAC_ = userinfo.deviceMANUFACTURER(MyGcmListenerService.this);
        String HS_MOD_ = userinfo.deviceModel(MyGcmListenerService.this);
        String user_email=userinfo.userEmail(MyGcmListenerService.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();




        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email",user_email));
            params.add(new BasicNameValuePair("action", "received"));
            params.add(new BasicNameValuePair("handset_name", HS_MANUFAC_));
            params.add(new BasicNameValuePair("handset_model", HS_MOD_));
            params.add(new BasicNameValuePair("msisdn",resultMno));

            // getting JSON Object
            // Note that create product url accepts POST method
            phpRequest.makeHttpRequest(pushResponseUrl, "POST", params);
            Log.d("Tariqul", pushResponseUrl+"params: "+ params);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
    }
}
