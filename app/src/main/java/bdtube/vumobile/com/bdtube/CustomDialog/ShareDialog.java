package bdtube.vumobile.com.bdtube.CustomDialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by IT-10 on 12/8/2015.
 */
public class ShareDialog {
    public void Dialog(Context context){


        Intent sharingIntent = new Intent(
                Intent.ACTION_SEND);
        sharingIntent.setType("image/png");
        String imageUrl = "file:///";
        imageUrl = imageUrl.replaceAll(" ", "%20");
        Log.w("Share Image imageUrl", imageUrl);
        sharingIntent.putExtra(Intent.EXTRA_STREAM,
                Uri.parse(imageUrl));
        // Add Flag
        sharingIntent
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(
                sharingIntent, "Share Via"));
    }

    public void Share(Context context){
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Share Via...");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=bdtube.vumobile.com.bdtube&hl=en");
        context.startActivity(Intent.createChooser(share, "Share link!"));

    }
}
