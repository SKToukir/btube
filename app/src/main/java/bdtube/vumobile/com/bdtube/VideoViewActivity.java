package bdtube.vumobile.com.bdtube;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bdtube.vumobile.com.bdtube.Config.Api;
import bdtube.vumobile.com.bdtube.CustomAdopter.HomeVideoAdopter;
import bdtube.vumobile.com.bdtube.Holder.AllHomeVideo;
import bdtube.vumobile.com.bdtube.Model.HomeVideoList;
import bdtube.vumobile.com.bdtube.SoapCall.MSISDNDetector;
import bdtube.vumobile.com.bdtube.SoapCall.RequiredUserInfo;
import bdtube.vumobile.com.bdtube.app.AppController;
import bdtube.vumobile.com.bdtube.util.PHPRequest;
import bdtube.vumobile.com.bdtube.util.SharedPreferencesHelper;

public class VideoViewActivity extends ActionBarActivity {
    public String urlForRobi = "http://wap.shabox.mobi/bdnewapi/Data/Categorywise";
    public String urlForOther = "http://wap.shabox.mobi/bdnewapi/DataOther/Categorywise";

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    int orientation;
    String ContentTitle;
    // Insert your Video URL
    String VideoURL = "http://wap.shabox.mobi/CMS/Content/Graphics/Video Clips/D800x600/Ami_Jare_by_DJ_Rahat_N_Reshmi.mp4";
    TextView txtSongName, tvArtistName;
    String ArtistName;
    private ArrayList<HomeVideoList> homeVideoLists;
    private HomeVideoAdopter homeVideoAdopter;
    PullToRefreshListView pullToRListView;
    ImageButton btnFullScreen;
    private String path = "http://dl.dropbox.com/u/145894/t/rabbits.3gp";
    public String MenuTitle, MenuFree, MobileNumber = "";
    public static String resultMno = null;
    public String pushResponseUrl = "http://203.76.126.210/sticker_gcm_server_bdtube/push_response.php";

    PHPRequest phpRequest = new PHPRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from video_main.xml
        setContentView(R.layout.videoview_main);


        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.bdtube_icon);


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0000")));
        bar.setDisplayShowTitleEnabled(false);

        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
        // enable status bar tint
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setTintColor(getResources().getColor(R.color.red));
        txtSongName = (TextView) findViewById(R.id.txtSongName);
        tvArtistName = (TextView) findViewById(R.id.txtArtistName);
        String value = getIntent().getStringExtra("videoURL");
        ContentTitle = getIntent().getStringExtra("getContentTitle");
        ArtistName = getIntent().getStringExtra("ArtistName");
        Log.d("artistName",ArtistName);
        MenuTitle = getIntent().getStringExtra("MenuTitle");
        MenuFree = getIntent().getStringExtra("MenuFree");
        MobileNumber = getIntent().getStringExtra("MobileNumber");

        if (MenuFree == null) {
           /* txtSongName.setText("Movie name:  " + ContentTitle);
            tvArtistName.setVisibility(View.INVISIBLE);*/
        } else if (MenuFree.equals("FullMovie")) {
            txtSongName.setText("Movie name:  " + ContentTitle);
            tvArtistName.setVisibility(View.INVISIBLE);
        } else if (MenuFree.equals("notification")) {

            SendLaunchPushRes();
            txtSongName.setText("Song name:  " + ContentTitle);
            tvArtistName.setText("Artist name: " + ArtistName.replaceAll("_"," "));
        } else {
            txtSongName.setText("Song name:  " + ContentTitle);
            tvArtistName.setText("Artist name: " + ArtistName.replaceAll("_"," "));
        }
        // btnFullScreen=(ImageButton)findViewById(R.id.btnFullScreen);

        VideoURL = value.replaceAll(" ", "%20");
        //init();
        PlaySong(VideoURL);
        pullToRListView = (PullToRefreshListView) findViewById(R.id.pullToRListView);
        homeVideoLists = new ArrayList<HomeVideoList>();

     /*   String urlVClipsFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=3";
        makeJsonArrayRequest(VideoViewActivity.this, urlVClipsFunny,MenuFree, MenuTitle);*/

        suggestionURL();
        listCustomize();

        // videoview = (VideoView)findViewById(R.id.VideoView);


     /*   String URLCharging="http://wap.shabox.mobi/BDTubeAPI/RegAPI.aspx?type=ChkDownloads&msisdn="+ MobileNumber +"";
        ChargingJsonArrayRequest(VideoViewActivity.this, URLCharging);
           Log.d("urlStatusURLCharging", URLCharging);
        */


        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

            Update();

        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void suggestionURL() {

        if (MenuTitle == null) {

            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=00";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "Home");
        } else if (MenuTitle.equals("Home")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=00";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "Home");
        } else if (MenuTitle.equals("BanglaMusic5")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=5";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "BanglaMusic5");
        } else if (MenuTitle.equals("EnglishMusic7")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=7";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "EnglishMusic7");
        } else if (MenuTitle.equals("EnglishMovie6")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=6";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "EnglishMovie6");
        } else if (MenuTitle.equals("BanglaMovie4")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=4";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "BanglaMovie4");
        } else if (MenuTitle.equals("BanglaNatok21")) {
            String BanglaNatok21 = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=21";
            makeJsonArrayRequest(VideoViewActivity.this, BanglaNatok21, "free", "BanglaNatok21");
        } else if (MenuTitle.equals("SCBanglaMusic11")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=11";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "SCBanglaMusic11");
        } else if (MenuTitle.equals("BanglaDramaClips")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=20";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "BanglaDramaClips");
        } else if (MenuTitle.equals("SCEnglishMusic13")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=13";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "SCEnglishMusic13");
        } else if (MenuTitle.equals("SCEnglishMovies12")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=12";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "SCEnglishMovies12");
        } else if (MenuTitle.equals("SCBanglaMovies10")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=10";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "SCBanglaMovies10");
        } else if (MenuTitle.equals("SCBollywoodCelebrityNews16")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=16";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "SCBollywoodCelebrityNews16");
        } else if (MenuTitle.equals("HindiMovie")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=14";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "HindiMovie");
        } else if (MenuTitle.equals("SCBollywoodMovieReview17")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=17";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "SCBollywoodMovieReview17");
        } else if (MenuTitle.equals("HollywoodMovieReview18")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=18";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "HollywoodMovieReview18");
        } else if (MenuTitle.equals("urlHollywoodGossip")) {
            String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=19";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewMovieHome, "free", "urlHollywoodGossip");
        } else if (MenuTitle.equals("FMHindiMovie3")) {
            String urlVClipsFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=3";
            makeJsonArrayRequest(VideoViewActivity.this, urlVClipsFunny, "FullMovie", "FMHindiMovie3");
        } else if (MenuTitle.equals("FMBanglaMovie1")) {
            String urlVClipsFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=1";
            makeJsonArrayRequest(VideoViewActivity.this, urlVClipsFunny, "FullMovie", "FMBanglaMovie1");
        } else if (MenuTitle.equals("FMHindiMovie2")) {
            String urlVClipsFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=2";
            makeJsonArrayRequest(VideoViewActivity.this, urlVClipsFunny, "FullMovie", "FMHindiMovie2");
        } else if (MenuTitle.equals("NewVideo00")) {
            String urlNewVideo = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=00";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewVideo, "free", "NewVideo00");
        } else if (MenuTitle.equals("Favorites")) {

            String urlFavorites = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaGet.aspx?type=FavouriteList&msisdn=" + MobileNumber + "";
            makeJsonArrayRequest(VideoViewActivity.this, urlFavorites, "free", "Favorites");
        } else if (MenuTitle.equals("MainScreenRobi")){
            String catCode = "2";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_MAIN_PAGE_ROBI, "free","MainScreenRobi",catCode);
        }else if (MenuTitle.equals("MainScreenOther")){
            String catCode = "1";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_MAIN_PAGE_OTHER, "free","MainScreenOther",catCode);
        }else if (MenuTitle.equals("banglaMusicOther")){
            String catCode = "E8E4F496-9CA9-4B35-BADD-9B6470BE2F74";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER,"free","banglaMusicOther",catCode);
        }else if (MenuTitle.equals("englishMusicOther")){
            String catCodeEnglishMusicOther = "74D847C2-4E98-44DA-B7A3-61C1EAE8938F";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this,Api.URL_CATEGORYWISE_OTHER,catCodeEnglishMusicOther,"englishMusicOther",catCodeEnglishMusicOther);
        }else if (MenuTitle.equals("banglaMovieSong")){
            String catCodeBanglaMovieSong = "A5D68929-8921-4ECD-8151-E36A3871EB95";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this,Api.URL_CATEGORYWISE_OTHER,"free","banglaMovieSong",catCodeBanglaMovieSong);
        }else if (MenuTitle.equals("BanglaDramaHDOther")){

            String catCodeBanglaDramaHD = "4781C5FB-0F16-4892-877D-F2F73DD4DE92";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this,Api.URL_CATEGORYWISE_OTHER,"free","BanglaDramaHDOther",catCodeBanglaDramaHD);
        }else if (MenuTitle.equals("BanglaMusicShortClipsOther")){

            String catCodeBanglaMusicShortClipsOther = "5DCA7C64-F342-434A-A934-750F37D74AEC";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaMusicShortClipsOther", catCodeBanglaMusicShortClipsOther);
        }else if (MenuTitle.equals("BanglaDramaClipsOther")){

            String catCodeBanglaDramaClipsOther = "EAB3B615-9942-462C-B531-97F255C6041D";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaDramaClipsOther",catCodeBanglaDramaClipsOther);
        }else if (MenuTitle.equals("englishMusicShortClipsOther")){

            String catCodeEnglishMusic = "502902E6-36D9-49AA-AF31-6C722E95C000";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "englishMusicShortClipsOther", catCodeEnglishMusic);
        }else if (MenuTitle.equals("EnglishMovieClipsOther")){

            String catCodeEnglishMovieClipsOther = "C450D2D6-58BB-478C-BE83-32DC3CA9690A";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "EnglishMovieClipsOther", catCodeEnglishMovieClipsOther);
        }else if (MenuTitle.equals("BanglaMoviecClips")){

            String catCodeBanglaMoviecClips = "C4C85FA7-7021-4BB4-B7EB-E793D26963B3";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaMoviecClips", catCodeBanglaMoviecClips);
        }else if (MenuTitle.equals("HindiMovieClips")){

            String catCodeHindiMovieClips = "5EAF33AB-0A57-4D80-9392-F212E5D209FF";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HindiMovieClips", catCodeHindiMovieClips);
        }else if (MenuTitle.equals("BollywoodCelebNews")){

            String catCodeBollywoodCelebNews = "5C5778B0-BDD2-4751-8835-A84988E9D09D";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BollywoodCelebNews", catCodeBollywoodCelebNews);
        }else if (MenuTitle.equals("BollyMovieReview")){

            String catCodeBollyMovieReview = "104CDD74-51AA-416E-93B0-7F3931AE60BD";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BollyMovieReview", catCodeBollyMovieReview);
        }else if (MenuTitle.equals("HollywoodMovieReview")){

            String catCodeHollywoodMovieReview = "021AB351-3182-49DF-894C-888FA66EA59F";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HollywoodMovieReview", catCodeHollywoodMovieReview);
        }else if (MenuTitle.equals("HollywoodGossip")){

            String catCodeHollywoodGossip = "C1104876-012B-4B85-8E51-F84FA6CD6DBA";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HollywoodGossip", catCodeHollywoodGossip);
        }else if (MenuTitle.equals("HondiMovie")){

            String catCodeHondiMovie = "C857EB0C-CF68-42D1-A532-7BC309F986E7";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HondiMovie", catCodeHondiMovie);
        }else if (MenuTitle.equals("newVideo")){

            String catCodeNewVideo = "1";
            getCatGoryWiseContentForRobiNother(VideoViewActivity.this, Api.URL_MAIN_PAGE_OTHER, "free", "newVideo",catCodeNewVideo);
        }else {
            String urlNewVideo = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=random&sequent=00";
            makeJsonArrayRequest(VideoViewActivity.this, urlNewVideo, "free", "NewVideo00");
        }

    }

    private void PlaySong(String url) {

        Log.d("Vedio url", url);

        // Find your VideoView in your video_main.xml layout

        // Execute StreamVideo AsyncTask
        videoview = (VideoView) findViewById(R.id.VideoView);
        // Create a progressbar
        pDialog = new ProgressDialog(VideoViewActivity.this);
        // Set progressbar title
        // pDialog.setTitle("BDTube Video Streaming");
        // Set progressbar message
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {


            getWindow().clearFlags(WindowManager
                    .LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            videoview.setVideoURI(Uri.parse(url));
            videoview.setMediaController(new MediaController(this));
            videoview.requestFocus();
            videoview.start();

            // Start the MediaController
        /*       MediaController mediacontroller = new MediaController(
                    VideoViewActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);*/

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title

        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_back:


                Intent intent = new Intent(VideoViewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VideoViewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void listCustomize() {

        homeVideoAdopter = new HomeVideoAdopter(this, homeVideoLists);
        pullToRListView.setAdapter(homeVideoAdopter);

        homeVideoAdopter.notifyDataSetChanged();
        pullToRListView.onRefreshComplete();
        final int old_pos = pullToRListView.getRefreshableView().getFirstVisiblePosition() + 1;
        Log.d("old_pos", String.valueOf(old_pos));

        pullToRListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                Log.d("scrollState", String.valueOf(scrollState));
                txtSongName.setVisibility(View.GONE);
                tvArtistName.setVisibility(View.GONE);
                //btnFullScreen.setVisibility(View.GONE);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("firstVisibleItem", String.valueOf(firstVisibleItem) + "visibleItemCount" + String.valueOf(visibleItemCount));

                if (firstVisibleItem == 0) {

                    txtSongName.setVisibility(View.VISIBLE);
                    tvArtistName.setVisibility(View.VISIBLE);
                    //btnFullScreen.setVisibility(View.VISIBLE);
                }
            }
        });

            /*    if (old_pos==1){
            txtSongName.setVisibility(View.VISIBLE);
            tvArtistName.setVisibility(View.VISIBLE);
            btnFullScreen.setVisibility(View.VISIBLE);
        }else{

        }*/


    }


    public void makeJsonArrayRequest(final Context context, String url, final String MenuFree, final String MenuTitle) {

        AllHomeVideo.removeHomeVideoList();
        // ProgressDialog();

        Log.d("url video view", url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON response", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < 5; i++) {

                                JSONObject homeURL = (JSONObject) response
                                        .get(i);
                                HomeVideoList homeVideoList = new HomeVideoList();
                                AllHomeVideo allHomeVideo = new AllHomeVideo();
                                String TimeStamp = homeURL.getString("TimeStamp");
                                String ContentCode = homeURL.getString("ContentCode");
                                String ContentCategoryCode = homeURL.getString("ContentCategoryCode");
                                String ContentTitle = homeURL.getString("ContentTitle");
                                String PreviewURL = homeURL.getString("BigPreview");
                                String VideoURL = homeURL.getString("PhysicalFileName");
                                String ContentType = homeURL.getString("ContentType");
                                String Value = homeURL.getString("Value");
                                String Artist = homeURL.getString("Artist");
                                String ContentZedCode = homeURL.getString("ContentZedCode");


                                homeVideoList.setContentCategoryCode(ContentCategoryCode);
                                homeVideoList.setContentCode(ContentCode);
                                homeVideoList.setTimeStamp(TimeStamp);
                                homeVideoList.setContentTitle(ContentTitle);
                                homeVideoList.setPreviewURL(PreviewURL);
                                homeVideoList.setVideoURL(VideoURL);
                                homeVideoList.setContentType(ContentType);
                                homeVideoList.setValue(Value);
                                homeVideoList.setMenuFree(MenuFree);
                                homeVideoList.setMenuTitle(MenuTitle);
                                homeVideoList.setArtist(Artist);
                                homeVideoList.setContentZedCode(ContentZedCode);
                                // Log.d("JSON data", PreviewURL + "    ContentCategoryCode: " + PreviewURLserch);


                                allHomeVideo.setHomeVideoList(homeVideoList);
                                listCustomize();

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

    public void ChargingJsonArrayRequest(final Context context, String url) {

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
                                String CanDownload1 = homeURL.getString("CanDownload");


                                Log.d("JSONSubStatus", CanDownload1);


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

    private void getCatGoryWiseContentForRobiNother(Context applicationContext, String url, final String free, final String banglaMusicHD, String catCode) {

        AllHomeVideo.removeHomeVideoList();

        JSONObject js = new JSONObject();
        try {
            js.put("CatCode",catCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = js.toString();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url,requestBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                Log.d("JSON response", response.toString());

                try {
                    // Parsing json array response
                    // loop through each json object

                    for (int i = 0; i < 5; i++) {

                        JSONObject homeURL = (JSONObject) response
                                .get(i);
                        HomeVideoList homeVideoList = new HomeVideoList();
                        AllHomeVideo allHomeVideo = new AllHomeVideo();
                        String TimeStamp = homeURL.getString("TimeStamp");
                        String ContentCode = homeURL.getString("ContentCode");
                        String ContentCategoryCode = homeURL.getString("ContentCategoryCode");
                        String ContentTitle = homeURL.getString("ContentTitle");
                        String PreviewURL = homeURL.getString("BigPreview");
                        String VideoURL = homeURL.getString("PhysicalFileName");
                        String ContentType = homeURL.getString("ContentType");
                        String Value = homeURL.getString("Value");
                        String Artist = homeURL.getString("Artist");
                        String ContentZedCode = homeURL.getString("ContentZedCode");


                        homeVideoList.setContentCategoryCode(ContentCategoryCode);
                        homeVideoList.setContentCode(ContentCode);
                        homeVideoList.setTimeStamp(TimeStamp);
                        homeVideoList.setContentTitle(ContentTitle);
                        homeVideoList.setPreviewURL(PreviewURL);
                        homeVideoList.setVideoURL(VideoURL);
                        homeVideoList.setContentType(ContentType);
                        homeVideoList.setValue(Value);
                        homeVideoList.setMenuFree(free);
                        homeVideoList.setMenuTitle(banglaMusicHD);
                        homeVideoList.setArtist(Artist);
                        homeVideoList.setContentZedCode(ContentZedCode);
                        // Log.d("JSON data", PreviewURL + "    ContentCategoryCode: " + PreviewURLserch);


                        allHomeVideo.setHomeVideoList(homeVideoList);
                        listCustomize();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response",error.getMessage());
            }
        });

        //StringRequest request = new StringRequest()

        Volley.newRequestQueue(applicationContext).add(request);

    }


    public void Update() {


        final Dialog updateDialog = new Dialog(VideoViewActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateDialog.setContentView(R.layout.update_dialog_activity);

        TextView update_text = (TextView) updateDialog.findViewById(R.id.update_text);
        update_text.setText("Critical version update available! \n" +
                "Update NOW to continue using.");
        Button update_app = (Button) updateDialog.findViewById(R.id.update_app);
        ImageView img = (ImageView) updateDialog.findViewById(R.id.imageView1);

        update_app.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                updateDialog.dismiss();

                /**
                 * if this button is clicked, close current
                 * activity
                 */
                String url = "http://203.76.126.210/shaboxbuddy/All_AppUpdateLog.php?Email=" + SharedPreferencesHelper.getSelectedFBOREMAIL(VideoViewActivity.this) + "&MNO=" + MobileNumber + "&AppName=bdtube&AppVersion=" + MainActivity.AppsVersion + "";
                WebView webView = new WebView(VideoViewActivity.this);
                webView.loadUrl(url);
                Log.d("UpdateLog", url);

                final String appPackageName = getPackageName();


                try {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + appPackageName)));
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateDialog.dismiss();
            }
        });

        updateDialog.show();
    }

    public void SendLaunchPushRes() {
        new SendLaunchPushResponse().execute();
    }

    private class SendLaunchPushResponse extends AsyncTask<String, String, String> {


        RequiredUserInfo userinfo = new RequiredUserInfo();
        String HS_MANUFAC_ = userinfo.deviceMANUFACTURER(VideoViewActivity.this);
        String HS_MOD_ = userinfo.deviceModel(VideoViewActivity.this);
        String user_email = userinfo.userEmail(VideoViewActivity.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


            // detect MSISDN when notification launched
            // detect MSISDN when notification launched
            try {
                Thread.sleep(1000);
                resultMno = "START";
                MSISDNDetector cws = new MSISDNDetector();
                cws.join();
                cws.start();

                while (resultMno == "START") {
                    try {
                        Thread.sleep(100);
                    } catch (Exception ex) {
                    }
                }

                if(resultMno.equalsIgnoreCase("ERROR"))
                {
                    resultMno="wifi";
                }

                Log.i("MSISDNs", "" + resultMno);

            } catch (Exception ex) {
                Log.i("EXCEPTION EXIST USER", "" + ex.toString());

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
}

