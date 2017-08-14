package bdtube.vumobile.com.bdtube.serviceNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import bdtube.vumobile.com.bdtube.GCM.MyGcmListenerService;
import bdtube.vumobile.com.bdtube.MainActivity;
import bdtube.vumobile.com.bdtube.R;
import bdtube.vumobile.com.bdtube.VideoViewActivity;


public class Utils {
    private static NotificationManager mNotificationManager;
    public static NotificationManager mManager;

    public static int clickCount = 0;
    public static int clickCount1 = 0;
    public static String resultMno = null;
    PHPRequest phpRequest = new PHPRequest();

    public static void setmNotificationManager(NotificationManager mNotificationManager) {
        Utils.mNotificationManager = mNotificationManager;
    }

    @SuppressWarnings("static-access")


    public static void sendNotification(Context context, String messageTitle, String messageBody) {
        int id = (int) System.currentTimeMillis();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500};

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Amar Sticker")
                .setContentText(messageBody)
                .setColor(context.getResources().getColor(android.R.color.primary_text_light))
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());


    }

    public static void setCustomViewNotification(Context context, String replaceAllurl, String title, String sms, String Image, int id, String contentCode, String categoryCode, String sContentType, String sPhysicalFileName, String zedID, String artist) {
        clickCount = clickCount + 1;
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

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an explicit intent for an ResultActivity to receive.

        Intent resultIntent = new Intent(context, VideoViewActivity.class);
        resultIntent.putExtra("DoDownload", "4");
        resultIntent.putExtra("ArtistName", artist);
        resultIntent.putExtra("videoURL",replaceAllurl );
        resultIntent.putExtra("getContentTitle",title );
        resultIntent.putExtra("MenuFree","notification" );
        resultIntent.putExtra("MobileNumber","" );
        resultIntent.putExtra("MenuTitle","Home" );


        Log.d("contentCode", contentCode);
        Log.d("categoryCode", categoryCode);
        Log.d("contentName", title);
        Log.d("sContentType", sContentType);
        Log.d("sPhysicalFileName", sPhysicalFileName);
        Log.d("contentImg", Image);
        Log.d("ZedID", zedID);
     /*   DownloadTask downloadTask = new DownloadTask();

        *//** Starting the task created above *//*
        downloadTask.execute(Image);*/
        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);



        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.notification_custom_remote);

        new MyGcmListenerService().SendLaunchPushRes();
        Intent volume = new Intent(context, VideoViewActivity.class);
        volume.putExtra("do_action", "play");
        volume.putExtra("ArtistName", artist);
        volume.putExtra("videoURL",replaceAllurl );
        volume.putExtra("getContentTitle",title );
        volume.putExtra("MenuFree","notification" );
        volume.putExtra("MobileNumber",resultMno );
        volume.putExtra("MenuTitle","Home" );
        PendingIntent pVolume = PendingIntent.getActivity(context, 1, resultIntent, 0);
        expandedView.setOnClickPendingIntent(R.id.MainlayoutCustom, pVolume);
        expandedView.setTextViewText(R.id.text_view, sms);

        //expandedView.setTextViewText(R.id.notificationTime, strDate);

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



    private static int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.bd : R.drawable.logo;
    }


}

