package bdtube.vumobile.com.bdtube.serviceNotification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bdtube.vumobile.com.bdtube.MainActivity;
import bdtube.vumobile.com.bdtube.SoapCall.RequiredUserInfo;


/**
 * Created by IT-10 on 9/22/2015.
 */
public class NotificationActivityHandler extends Activity {

    private NotificationActivityHandler ctx;
    Context context;
    public String PushMessage = "";
    public String pushResponseUrl = "http://www.vumobile.biz/sticker_gcm_server/push_response.php";
    public static String resultMno = null;
    PHPRequest phpRequest = new PHPRequest();

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    public static String StickerTitle = "";
    private final String PATH = "/data/data/pack.coderzheaven/";
    // public static String sample_url = GCMIntentService.sample_url;

    JSONObject jsonobject;
    JSONArray jsonarray;
    private static String url2 = "http://wap.shabox.mobi/GCMPanel/Amar_stickerInPush.aspx";

    // JSON Node names
    private static final String TAG_CONTACTS = "Table";


    public static String image_title = "";
    public static String sample_url = "";
    JSONArray contacts = null;
    public static String action;
    public static int NotificationId;
    public static String notificationstickerShare = "";
    public static boolean notiClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);

        String action = (String) getIntent().getExtras().get("DoDownload");
        NotificationId = getIntent().getIntExtra("NotificationId", 0);
        image_title = (String) getIntent().getExtras().get("NotificationTitle");
        StickerTitle = image_title + ".jpg";
        sample_url = (String) getIntent().getExtras().get("NotificationImage");

        Log.i("LOG", "lauching action: " + action + " " + NotificationId + "" + sample_url + " " + StickerTitle);


        // Utils.clickCount1= Utils.clickCount1+1;

        if (NotificationId == 0) {
            try {
                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // int DownloadPosition= (Integer)getIntent().getExtras().get("DownloadPosition");
        try {
            NotificationActivityHandler.this.finish();
        } catch (Exception e) {
            e.getStackTrace();
        }
        try {

            Thread.sleep(1000);
            resultMno = "START";
//            CallerMSISDNDetector cws = new CallerMSISDNDetector();
//            cws.join();
//            cws.start();

            while (resultMno == "START") {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
            }
            Log.i("MSISDN ", "" + resultMno);

        } catch (Exception ex) {
            Log.i("EXCEPTION EXIST USER", "" + ex.toString());
        }

        ctx = this;
        try {

            //Log.i("LOG", "lauching action: " + action);
            if (action != null) {
                if (action.equals("4")) {

                    Intent i = new Intent(NotificationActivityHandler.this, MainActivity.class);
                    i.putExtra("StickerTitle", StickerTitle);
                    startActivity(i);
                    notiClick = true;
                    notificationstickerShare = StickerTitle;
                    SendLaunchPushRes();

                    //DownloadFromUrl(imageUrl);

                    if (isNetworkAvailable()) {
                        /** Getting a reference to Edit text containing url */

                        /** Creating a new non-ui thread task */
                        DownloadTask downloadTask = new DownloadTask();

                        /** Starting the task created above */
                        downloadTask.execute(sample_url);
                        //Toast.makeText(getBaseContext(), "action"+action , Toast.LENGTH_SHORT).show();
                        // cancelNotification(ctx, MyGcmPushReceiver.NOTIF_ID);
                        clearNotification();
                    } else {
                        Toast.makeText(getBaseContext(), "Network is not Available", Toast.LENGTH_SHORT).show();
                    }

                } else if (action.equals("3")) {
                    Toast.makeText(getApplicationContext(), "working", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                clearNotification();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //StickerTabActivity.serverAccessAsynTask innerObject = new StickerTabActivity.serverAccessAsynTask();


        //  new  StickerTabActivity.serverAccessAsynTask().execute("");
    }


    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.
                        NOTIFICATION_SERVICE);

        notificationManager.cancel(NotificationId);
        // NotifActivityHandler.this.finish();
    }

/*    public class NotificationDismissedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int notificationId = intent.getExtras().getInt("com.my.app.notificationId");
            Toast.makeText(getApplicationContext(), "Successful"+""+ notificationId, Toast.LENGTH_SHORT).show();

        }
    }*/

    private boolean isNetworkAvailable() {
        boolean available = false;
        /** Getting the system's connectivity service */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /** Getting active network interface  to get the network's status */
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable())
            available = true;

        /** Returning the status of the network */
        return available;
    }

    private Bitmap downloadUrl(String strUrl) throws IOException {
        Bitmap bitmap = null;
        InputStream iStream = null;
        try {
            URL url = new URL(strUrl);
            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        } catch (Exception e) {
            Log.d("Exception download url", e.toString());
        } finally {
            iStream.close();
        }
        return bitmap;
    }


    /**
     * Download task class
     */
    private class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... url) {
            try {
                bitmap = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            /** Getting a reference to ImageView to display the
             * downloaded image
             */
         /*   ImageView iView = (ImageView) findViewById(R.id.iv_image);

            *//** Displaying the downloaded image *//*
            iView.setImageBitmap(result);*/

            // skipping ???? value, if bangla input with error
            StickerTitle = StickerTitle.replaceAll("\\u003F", "v"); // \\u003F means ? character


//            MainActivity.dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(sample_url));
//            request.setDestinationInExternalPublicDir("/Amar_Sticker/",
//                    StickerTitle);
//            MainActivity.enqueueid = StickerTabActivity.dm
//                    .enqueue(request);

            Log.d("sample_url", sample_url + " " + StickerTitle);

            /** Showing a message, on completion of download process */
            Toast.makeText(getBaseContext(), "Sticker downloaded successfully", Toast.LENGTH_SHORT).show();

            action = "-1";
        }
    }


    public void SendLaunchPushRes() {
        new SendLaunchPushResponse().execute();
    }

    private class SendLaunchPushResponse extends AsyncTask<String, String, String> {

        RequiredUserInfo userinfo = new RequiredUserInfo();
        String HS_MANUFAC_ = userinfo.deviceMANUFACTURER(NotificationActivityHandler.this);
        String HS_MOD_ = userinfo.deviceModel(NotificationActivityHandler.this);
        String user_email = userinfo.userEmail(NotificationActivityHandler.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            if (resultMno.equalsIgnoreCase("ERROR")) {
                resultMno = "wifi";
            }
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", user_email));
            params.add(new BasicNameValuePair("action", "launch"));
            params.add(new BasicNameValuePair("handset_name", HS_MANUFAC_));
            params.add(new BasicNameValuePair("handset_model", HS_MOD_));
            params.add(new BasicNameValuePair("msisdn", resultMno));

            // getting JSON Object
            // Note that create product url accepts POST method
            phpRequest.makeHttpRequest(pushResponseUrl, "POST", params);
            Log.d("Tariqul", pushResponseUrl + "params: " + params);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
    }
// Download image from web in-up-push


}