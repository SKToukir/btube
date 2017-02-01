package bdtube.vumobile.com.bdtube.GCM;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    // give your server registration url here

    static final String SERVER_URL = "http://203.76.126.210/sticker_gcm_server_bdtube/register.php";

    // Google project id
    public static final String SENDER_ID = "28400850174";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "StickerApp GCM";

    public static final String DISPLAY_MESSAGE_ACTION = "bdtube.vumobile.com.bdtube.GCM.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}