package bdtube.vumobile.com.bdtube.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import bdtube.vumobile.com.bdtube.CustomAdopter.CommentAdopter;
import bdtube.vumobile.com.bdtube.Model.AllCommentList;
import bdtube.vumobile.com.bdtube.R;

/**
 * Created by IT-10 on 1/4/2016.
 */
public class CommentDialog {


    private ArrayList<AllCommentList> homeVideoLists;
    private CommentAdopter homeVideoAdopter;

    PullToRefreshListView pullToRListView;
    public void Comment(final Context context, String finalMobileNumber, final String ContentCode) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.comment_dialog);

        pullToRListView= (PullToRefreshListView) dialog.findViewById(R.id.pullToRListView);
        homeVideoLists= new ArrayList<AllCommentList>();

       // Toast.makeText(context,  allCommentLists.get(0).getValue(), Toast.LENGTH_LONG).show();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        if (finalMobileNumber.equals("")) {
            finalMobileNumber = "wifi";
        }
        //Log.d("urlComment", urlComment);

        listCustomize(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        Button btnCommentSubmit=(Button) dialog.findViewById(R.id.btnCommentSubmit);
        final String finalMobileNumber1 = finalMobileNumber;
        btnCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtComment= (EditText)dialog.findViewById(R.id.edtComment);
                String comment=edtComment.getText().toString().trim();
                if (comment.equals("")){
                    Toast.makeText(context, "Input comment", Toast.LENGTH_LONG).show();
                }else {

                    String url = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaPost.aspx?type=Comment&msisdn=" + finalMobileNumber1 + "&content=" + ContentCode + "&value=" + comment + "";
                    WebView webView = new WebView(context);
                    webView.loadUrl(url);

                    Log.d("CommentUrl", url);

                    TextView txtComment=(TextView)dialog.findViewById(R.id.txtcomment);
                    txtComment.setText(comment);
                    listCustomize(context);

                }
            }
        });

        lp.x = ViewGroup.LayoutParams.MATCH_PARENT; // The new position of the X coordinates
        lp.y = 0; // The new position of the Y coordinates
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT; // Width
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Height
        lp.alpha = 0.9f; // Transparency

        dialogWindow.setAttributes(lp);

        dialog.show();

    }



    public void listCustomize(Context context){

        homeVideoAdopter=new CommentAdopter(context, homeVideoLists);
        pullToRListView.setAdapter(homeVideoAdopter);

        homeVideoAdopter.notifyDataSetChanged();
        pullToRListView.onRefreshComplete();

    }
}
