package bdtube.vumobile.com.bdtube.serviceNotification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import bdtube.vumobile.com.bdtube.VideoViewActivity;

/**
 * Created by IT-10 on 9/22/2015.
 */
public class NotifActivityHandler extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);

        String action = (String) getIntent().getExtras().get("do_action");

        try {
            NotifActivityHandler.this.finish();
        }catch (Exception e){
            e.getStackTrace();
        }

        if (action != null) {
            if (action.equals("play")) {
                // for example play a music
                Intent intent = new Intent(NotifActivityHandler.this, VideoViewActivity.class);

                startActivity (intent);

               // clearNotification();
                clearNotification();
            } else if (action.equals("close")) {
                // close current notification
                clearNotification();
            }
        }

    }

    public void clearNotification(){
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.
                        NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
        // NotifActivityHandler.this.finish();
    }
}





