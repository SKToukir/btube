package bdtube.vumobile.com.bdtube.CustomDialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by IT-10 on 12/31/2015.
 */
public class CustomDialog {

    public void ProgressDialog(Context context, ProgressDialog progressDialog){

        progressDialog = new ProgressDialog(
                context);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void hideProgressDialog(Context context, ProgressDialog progressDialog) {

        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
