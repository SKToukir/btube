package bdtube.vumobile.com.bdtube.CustomAdopter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bdtube.vumobile.com.bdtube.CustomDialog.CommentDialog;
import bdtube.vumobile.com.bdtube.CustomDialog.ShareDialog;
import bdtube.vumobile.com.bdtube.Holder.AllComment;
import bdtube.vumobile.com.bdtube.Holder.AllFavouriteList;
import bdtube.vumobile.com.bdtube.Holder.AllHomeVideo;
import bdtube.vumobile.com.bdtube.Model.AllCommentList;
import bdtube.vumobile.com.bdtube.Model.HomeVideoList;
import bdtube.vumobile.com.bdtube.Model.IsFavouriteChecklist;
import bdtube.vumobile.com.bdtube.R;
import bdtube.vumobile.com.bdtube.SplashActivity;
import bdtube.vumobile.com.bdtube.VideoViewActivity;
import bdtube.vumobile.com.bdtube.app.AppController;
import bdtube.vumobile.com.bdtube.util.SharedPreferencesHelper;

/**
 * Created by IT-10 on 12/31/2015.
 */

public class HomeVideoAdopter extends ArrayAdapter<HomeVideoList> {

    Context context;
    com.android.volley.toolbox.ImageLoader vImageLoader; //Volley Network ImageLoader
    public String videoURL;
    public String fullVideoURL;

    public String ImagePreviewURL;
    String getContentTitle;
    RatingBar ratingBar;

    String SubStatus1 = "";

    private ArrayList<AllCommentList> allCommentLists;
    private CommentAdopter commentAdopter;
    PullToRefreshListView pullToRListView;
    public String MenuTitle = "";
    public String MenuFree = "";
    public String isFavourite = "No";
    int TitlePos = 0;
    int TitlePos1 = 0;
    public String Title = "";

    public HomeVideoAdopter(Context context, ArrayList<HomeVideoList> homeVideoLists) {
        super(context, R.layout.custom_content_list, AllHomeVideo.getHomeVideoList());
        this.context = context;
        //	imageLoader = new ImageLoader(context);

    }

    static class ViewHolder {
        RelativeLayout videoImagePreview;
        ImageButton VideoViewClick;
        TextView updateTime;
        TextView songTitle;
        NetworkImageView thumb;
        ImageButton IBShare;
        ImageButton IBComment;
        ImageButton IBFavourite;

        RatingBar ratingBar;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View v = convertView;

        if (v == null) {
            final LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.custom_content_list, null);

            holder = new ViewHolder();
            // holder.videoImagePreview = (RelativeLayout) v.findViewById(R.id.videoImagePreview);

            holder.VideoViewClick = (ImageButton) v.findViewById(R.id.VideoViewClick);

            holder.updateTime = (TextView) v.findViewById(R.id.updateTime);
            holder.songTitle = (TextView) v.findViewById(R.id.songTitle);
            holder.thumb = (NetworkImageView) v.findViewById(R.id.music_preview_urlTop);
            holder.IBShare = (ImageButton) v.findViewById(R.id.IBShare);
            holder.IBComment = (ImageButton) v.findViewById(R.id.IBComment);
            holder.IBFavourite = (ImageButton) v.findViewById(R.id.IBFavourite);
            holder.ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

            v.setTag(holder);

        } else {

            holder = (ViewHolder) v.getTag();

        }

        if (position < AllHomeVideo.getHomeVideoList().size()) {

            final HomeVideoList query = AllHomeVideo.getHomeVideoList().elementAt(
                    position);

            notifyDataSetChanged();
            String TimeStamp = query.getTimeStamp().toString().trim();
            final String ContentCode = query.getContentCode().toString().trim();
            String ContentType = query.getContentType().toString().trim();
            getContentTitle = query.getContentTitle().toString().trim();
            String PhysicalFileName = query.getVideoURL().toString().trim();
            String PreviewURL = query.getPreviewURL().toString().trim();
            String ratingValue = query.getValue().toString().trim();
            MenuTitle = query.getMenuTitle().toString().trim();
            MenuFree = query.getMenuFree().toString().trim();

            Log.d("MenuTitle", MenuTitle);

            if (ContentType.equalsIgnoreCase("FV")) {
                videoURL = "http://wap.shabox.mobi/CMS/Content/Graphics/FullVideo/D480x320/" + PhysicalFileName + ".mp4";
                ImagePreviewURL = "http://wap.shabox.mobi/CMS/GraphicsPreview/FullVideo/" + PreviewURL;
            } else {

                videoURL = "http://wap.shabox.mobi/CMS/Content/Graphics/Video Clips/D480x320/" + PhysicalFileName + ".mp4";
                ImagePreviewURL = "http://wap.shabox.mobi/CMS/GraphicsPreview/Video%20Clips/" + PreviewURL;
            }
            holder.updateTime.setText("" + TimeStamp.replaceAll("_", " "));
            holder.songTitle.setText("" + getContentTitle.replaceAll("_", " "));

            fullVideoURL = videoURL.replaceAll(" ", "%20");
            Log.d("fullVideoURL", fullVideoURL);
            Log.d("ImagePreviewURL", ImagePreviewURL.toString().replaceAll(" ", "%20"));
            vImageLoader = AppController.getInstance().getImageLoader();
            holder.thumb.setImageUrl(ImagePreviewURL.toString().replaceAll(" ", "%20"), vImageLoader);
            String MobileNumber = SplashActivity.resultMno_splash;
            if (MobileNumber.equalsIgnoreCase("ERROR")) {
                MobileNumber = "";
            } else {
                MobileNumber = SplashActivity.resultMno_splash;
            }
            Log.d("urlStatus", MobileNumber);
            String url = "http://wap.shabox.mobi/BDTubeAPI/RegAPI.aspx?type=ChkSubs&msisdn=" + MobileNumber + "";
            Log.d("urlStatus", url);
            makeJsonArrayRequest(context, url);

            // only for favourite check
            String urlFavourite = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaGet.aspx?type=Favourite&msisdn=" + MobileNumber + "&content=" + ContentCode + "";
            makeFavouriteJsonArrayRequest(context, urlFavourite, ContentCode, MobileNumber);

            Log.d("JSONisFavourite", urlFavourite);
            // Log.d("JSONisFavouriteisFavourite",CommonString.isFavourite+"  "+CommonString.ContentCode);
            IsFavouriteChecklist favouriteChecklist = new IsFavouriteChecklist();
            String favouriteContent = favouriteChecklist.getContentCode();
            String isFavourite = favouriteChecklist.getIsFavourite();

            Log.d("favouriteContent", favouriteContent + "  " + isFavourite);

            if (isFavourite.equals("Yes") || favouriteContent.equals(ContentCode)) {

                ImageButton imageButton = (ImageButton) v.findViewById(R.id.IBFavourite);
                imageButton.setImageResource(R.drawable.fav1);

            } else {
                ImageButton imageButton = (ImageButton) v.findViewById(R.id.IBFavourite);
                imageButton.setImageResource(R.drawable.fav2);
            }

            final String finalMobileNumber = MobileNumber;

            final String finalMobileNumber1 = MobileNumber;
            holder.VideoViewClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final HomeVideoList query = AllHomeVideo.getHomeVideoList().elementAt(
                            position);
                    String ContentType = query.getContentType().toString().trim();
                    String PhysicalFileName = query.getVideoURL().toString().trim();
                    String ContentTitle = query.getContentTitle().toString().trim().replaceAll("_", " ");
                    if (ContentTitle.contains("by")) {
                        TitlePos = ContentTitle.indexOf("by");
                    } else {
                        TitlePos1 = 1;
                    }
                    if (TitlePos1 == 1) {
                        Title = ContentTitle;
                    } else {
                        Title = ContentTitle.substring(0, TitlePos);
                    }
                    String ArtistName = query.getArtist().toString().trim().replaceAll("_", " ");
                    String ContentCategoryCode = query.getContentCategoryCode().toString().toString();
                    String ContentZedCode = query.getContentZedCode().toString().toString();

                    if (ContentType.equalsIgnoreCase("FV")) {
                        videoURL = "http://wap.shabox.mobi/CMS/Content/Graphics/FullVideo/D480x320/" + PhysicalFileName + ".mp4";
                    } else {
                        videoURL = "http://wap.shabox.mobi/CMS/Content/Graphics/Video Clips/D480x320/" + PhysicalFileName + ".mp4";
                    }
                    Intent myIntent = new Intent(context,
                            VideoViewActivity.class);
                    myIntent.putExtra("videoURL", videoURL);
                    myIntent.putExtra("getContentTitle", Title);
                    myIntent.putExtra("ArtistName", ArtistName);
                    myIntent.putExtra("MenuTitle", MenuTitle);
                    myIntent.putExtra("MenuFree", MenuFree);
                    myIntent.putExtra("MobileNumber", finalMobileNumber);
                    context.startActivity(myIntent);

                    Log.d("fullVideoURL", videoURL);

                    String SuccessLog = "http://wap.shabox.mobi/BDTubeAPI/AccessSuccess.aspx?type=Success&msisdn=wifi&HS_MOD=" + SplashActivity.HS_MOD_1 + "&HS_MANUFAC=" + SplashActivity.HS_MANUFAC_1 + "&HS_DIM=" + SplashActivity.HS_DIM_1 + "&sMasterCat=free&content_code=" + ContentCode + "&ContentTitle=" + ContentTitle + "&sContentType=" + ContentType + "&ZedID=" + ContentZedCode + "&CategoryCode=" + ContentCategoryCode + "&email=" + SharedPreferencesHelper.getSelectedFBOREMAIL(context) + "";

                    WebView webView = new WebView(context);
                    webView.loadUrl(SuccessLog);

                    Log.d("SuccessLog", SuccessLog);


                }
            });

            holder.IBShare.setOnClickListener(litchener);
            holder.IBComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentDialog commentDialog = new CommentDialog();
                    //shareDialog.Dialog(ctx);
                    commentDialog.Comment(context, finalMobileNumber, ContentCode);

                    String urlComment = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaGet.aspx?type=Comment&content=" + ContentCode + "";
                    makeCommentJsonArrayRequest(context, urlComment);

                    Log.d("urlComment", urlComment);

                }
            });

            holder.IBFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaPost.aspx?type=Favourite&msisdn=" + finalMobileNumber + "&content=" + ContentCode + "";
                    WebView webView = new WebView(context);
                    webView.loadUrl(url);
                    ImageButton imageButton = (ImageButton) v.findViewById(R.id.IBFavourite);
                    imageButton.setImageResource(R.drawable.fav1);

                    Log.d("JSONisFavourite", url);
                }
            });
            ///holder.ratingBar.setOnClickListener(litchener);
            if (ratingValue.equals("") || ratingValue.equals("null") || ratingValue.equals(null)) {
                holder.ratingBar.setRating(Float.parseFloat("0"));
            } else {
                holder.ratingBar.setRating(Float.parseFloat(ratingValue));
            }

            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                    String url = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaPost.aspx?type=Rating&msisdn=" + finalMobileNumber + "&content=" + ContentCode + "&value=" + String.valueOf(rating) + "";
                    WebView webView = new WebView(context);
                    webView.loadUrl(url);


                }
            });

        }
        notifyDataSetChanged();
        return v;
    }

    View.OnClickListener litchener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.IBShare:
                    ShareDialog shareDialog = new ShareDialog();
                    //shareDialog.Dialog(ctx);
                    shareDialog.Share(context);
                    break;


            }
        }
    };


    public void makeJsonArrayRequest(final Context context, String url) {

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
                                String SubStatus = homeURL.getString("SubStatus");

                                SubStatus1 = SubStatus;


                                Log.d("JSONSubStatus", SubStatus);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        // hideProgressDialog();
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

    public void makeFavouriteJsonArrayRequest(final Context context, String url, final String ContentCode, final String MobileNumber) {

        AllFavouriteList.removeIsFavouriteChecklist();
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


                                IsFavouriteChecklist isFavouriteChecklist = new IsFavouriteChecklist();
                                AllFavouriteList allFavouriteList = new AllFavouriteList();

                                String Favourite = homeURL.getString("Favourite");

                                isFavouriteChecklist.setIsFavourite(Favourite);
                                isFavouriteChecklist.setContentCode(ContentCode);
                                isFavouriteChecklist.setNumber(MobileNumber);


                                Log.d("JSON data", Favourite + "    MSISDN: " + ContentCode);


                                allFavouriteList.setIsFavouriteChecklist(isFavouriteChecklist);
                                // CommonString.isFavourite=Favourite;

                                //CommonString.ContentCode=ContentCode;

                                Log.d("JSONisFavourite", isFavourite);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        // hideProgressDialog();
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

    public void makeCommentJsonArrayRequest(final Context context, String url) {

        AllComment.removeAllCommentList();
        // ProgressDialog();

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
                                AllCommentList allCommentList = new AllCommentList();
                                AllComment allComment = new AllComment();

                                String Value = homeURL.getString("Value");
                                String MSISDN = homeURL.getString("MSISDN");
                                String TimeStamp = homeURL.getString("TimeStamp");

                                allCommentList.setValue(Value);
                                allCommentList.setMSISDN(MSISDN);
                                allCommentList.setTimeStamp(TimeStamp);


                                Log.d("JSON data", Value + "    MSISDN: " + MSISDN);


                                allComment.setAllCommentList(allCommentList);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        //  hideProgressDialog();
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
