package bdtube.vumobile.com.bdtube.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bdtube.vumobile.com.bdtube.R;

/**
 * Created by IT-10 on 1/19/2016.
 */
public class ContactUs {




    public void Contact(final Context context, final String MobileNumber) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.contact_us);


        // Toast.makeText(context,  allCommentLists.get(0).getValue(), Toast.LENGTH_LONG).show();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        Button   buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button   buttonSend = (Button) dialog.findViewById(R.id.buttonSend);


        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText textMessage = (EditText) dialog.findViewById(R.id.editTextMessage);

                String message = textMessage.getText().toString();

                String url="http://wap.shabox.mobi/BDTubeAPI/ContentMetaPost.aspx?type=message&msisdn="+MobileNumber+"&value="+message+"";
                WebView webView= new WebView(context);
                webView.loadUrl(url);
                dialog.dismiss();

                Toast.makeText(context,"Your message send successfully", Toast.LENGTH_LONG).show();


            }
        });

        lp.x = ViewGroup.LayoutParams.MATCH_PARENT; // The new position of the X coordinates
        lp.y = 0; // The new position of the Y coordinates
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT; // Width
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Height
        lp.alpha = 0.9f; // Transparency

        dialogWindow.setAttributes(lp);

        dialog.show();

    }
}
