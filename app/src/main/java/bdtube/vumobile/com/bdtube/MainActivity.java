package bdtube.vumobile.com.bdtube;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.adplaybind.AdPlayBind;
import com.flurry.android.FlurryAgent;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bdtube.vumobile.com.bdtube.Config.Api;
import bdtube.vumobile.com.bdtube.CustomAdopter.HomeVideoAdopter;
import bdtube.vumobile.com.bdtube.CustomDialog.ContactUs;
import bdtube.vumobile.com.bdtube.CustomDialog.FAQDialog;
import bdtube.vumobile.com.bdtube.CustomDialog.HelpDialog;
import bdtube.vumobile.com.bdtube.CustomDialog.NeedMSISDN;
import bdtube.vumobile.com.bdtube.Holder.AllHomeVideo;
import bdtube.vumobile.com.bdtube.Holder.AllNavigationList;
import bdtube.vumobile.com.bdtube.Model.HomeVideoList;
import bdtube.vumobile.com.bdtube.Model.NavigationList;
import bdtube.vumobile.com.bdtube.app.AppController;
import bdtube.vumobile.com.bdtube.app.CommonString;
import bdtube.vumobile.com.bdtube.serviceNotification.MyReceiver;
import bdtube.vumobile.com.bdtube.serviceNotification.NetworkedService;
import bdtube.vumobile.com.bdtube.util.SharedPreferencesHelper;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    Context context;

    public String urlForRobi = "http://wap.shabox.mobi/bdnewapi/Data/Categorywise";
    public String urlForOther = "http://wap.shabox.mobi/bdnewapi/DataOther/Categorywise";

    public String url = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=00";
    public ProgressDialog progressDialog;
    PullToRefreshListView pullToRListView;
    private ArrayList<HomeVideoList> homeVideoLists;
    private HomeVideoAdopter homeVideoAdopter;

    public ArrayList<NavigationList> navigationList;
    private DrawerLayout mDrawerLayout;

    private PendingIntent pendingIntent;
    public String MenuTitle = "";
    String MobileNumber = "";

    public String updateReasonString;
    private String mAdSpaceName = "NATIVE_ADSPACE";
    String FLurry_SDK_API_KEY = "43HCSRKGVFHVJ6JVWK2X";

    public static String AppsVersion = "";
    public static String webVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.bdtube_icon);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.RED));


        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
// enable status bar tint
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setTintColor(getResources().getColor(R.color.red));

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        init();


        MobileNumber = SplashActivity.resultMno_splash;
        if (MobileNumber.equalsIgnoreCase("ERROR")) {
            MobileNumber = "wifi";
        } else {
            MobileNumber = SplashActivity.resultMno_splash;
        }

        if (MobileNumber.startsWith("wifi")) {

            String catCode = "1";
            getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_MAIN_PAGE_OTHER, "free", "MainScreenOther", catCode);
            //makeJsonArrayRequest(context, "http://wap.shabox.mobi/bdnewapi/Data/Fulldata", "free", "NewVideo00");
        } else {

            String catCode = "2";
            getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_MAIN_PAGE_ROBI, "free", "MainScreenRobi", catCode);
            //makeJsonArrayRequest(context, "http://wap.shabox.mobi/bdnewapi/DataOther/Fulldata", "free", "NewVideo00");
        }

        // user access log
        String url = "http://wap.shabox.mobi/BDTubeAPI/AccessSuccess.aspx?type=Access&msisdn=" + MobileNumber + "&HS_MOD=" + SplashActivity.HS_MOD_1 + "&HS_MANUFAC=" + SplashActivity.HS_MANUFAC_1 + "&HS_DIM=" + SplashActivity.HS_DIM_1 + "";
        WebView webView = new WebView(MainActivity.this);
        webView.loadUrl(url);

        Log.d("AccessSuccess", url);

       /* String URLCharging="http://wap.shabox.mobi/BDTubeAPI/RegAPI.aspx?type=ChkDownloads&msisdn="+ MobileNumber +"";
        ChargingJsonArrayRequest(context, URLCharging);

        Log.d("urlStatusURLCharging", URLCharging);*/

        if (CommonString.isUserLearn = true) {
            // The user manually opened the drawer; store this flag to
            // prevent auto-showing
            // the navigation drawer automatically in the future.
            mDrawerLayout.closeDrawers();
        }

        RelativeLayout RL = (RelativeLayout) findViewById(R.id.RL);

        RL.setVisibility(View.VISIBLE);
        AdPlayBind addPlay = new AdPlayBind();
        RL.addView(addPlay.BannerAd("BE30E9C1-D7F2-481F-A1FF-38EAEC102445", "4088796A-01BD-4506-9AD1-31FD5E34DDB5", "1", MainActivity.this));


        // FlurryAgent sdk implement

        FlurryAgent.onPageView();
        ChargingJsonArrayRequestVersion(MainActivity.this, "http://www.vumobile.biz/bdtube/test.php");

        try {

            Intent serviceIntent = new Intent(MainActivity.this, NetworkedService.class);
            startService(serviceIntent);
            Intent myIntent = new Intent(MainActivity.this, MyReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 90 * 1000, pendingIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onStart() {
        super.onStart();

        FlurryAgent.onStartSession(this, FLurry_SDK_API_KEY);

        // fetch and prepare ad for this ad space. won’t render one yet

    }

    public void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);

    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void ReloadSong() {

        try {
            if (MenuTitle.equals("Home")) {
                String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=1";
                makeJsonArrayRequest(context, urlNewMovieHome, "free", "Home");
            } else if (MenuTitle.equals("BanglaMusic5")) {
                String urlNewMovie = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=5";
                makeJsonArrayRequest(context, urlNewMovie, "free", "BanglaMusic5");
            } else if (MenuTitle.equals("EnglishMusic7")) {
                String urlNewMusic = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=7";
                makeJsonArrayRequest(context, urlNewMusic, "free", "EnglishMusic7");
            } else if (MenuTitle.equals("EnglishMovie6")) {
                String txtEnglishMovie = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=6";
                makeJsonArrayRequest(context, txtEnglishMovie, "free", "EnglishMovie6");
            } else if (MenuTitle.equals("BanglaMovie4")) {
                String urlNewFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=4";
                makeJsonArrayRequest(context, urlNewFunny, "free", "BanglaMovie4");
            } else if (MenuTitle.equals("SCBanglaMusic11")) {
                String urlFullMovies = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=11";
                makeJsonArrayRequest(context, urlFullMovies, "free", "SCBanglaMusic11");
            } else if (MenuTitle.equals("SCEnglishMusic13")) {
                String urlFullMusicVideo = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=13";
                makeJsonArrayRequest(context, urlFullMusicVideo, "free", "SCEnglishMusic13");
            } else if (MenuTitle.equals("SCEnglishMovies12")) {
                String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=12";
                makeJsonArrayRequest(context, urlNewMovieHome, "free", "SCEnglishMovies12");
            } else if (MenuTitle.equals("SCBanglaMovies10")) {
                String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=10";
                makeJsonArrayRequest(context, urlNewMovieHome, "free", "SCBanglaMovies10");
            } else if (MenuTitle.equals("SCBollywoodCelebrityNews2")) {
                String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=2";
                makeJsonArrayRequest(context, urlNewMovieHome, "free", "SCBollywoodCelebrityNews2");
            } else if (MenuTitle.equals("SCBollywoodMovieReview10")) {
                String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=10";
                makeJsonArrayRequest(context, urlNewMovieHome, "free", "SCBollywoodMovieReview10");
            } else if (MenuTitle.equals("HollywoodMovieReview11")) {
                String urlNewMovieHome = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=11";
                makeJsonArrayRequest(context, urlNewMovieHome, "free", "HollywoodMovieReview11");
            } else if (MenuTitle.equals("FMHindiMovie3")) {
                String urlVClipsFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=3";
                makeJsonArrayRequest(context, urlVClipsFunny, "FullMovie", "FMHindiMovie3");
            } else if (MenuTitle.equals("NewVideo00")) {
                String urlNewVideo = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=00";
                makeJsonArrayRequest(context, urlNewVideo, "free", "NewVideo00");
            } else if (MenuTitle.equals("Favorites")) {
                String urlFavorites = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaGet.aspx?type=FavouriteList&msisdn=" + MobileNumber + "";
                makeJsonArrayRequest(context, urlFavorites, "free", "Favorites");
            } else {
                makeJsonArrayRequest(context, url, "free", "NewVideo00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        pullToRListView = (PullToRefreshListView) findViewById(R.id.pullToRListView);
        homeVideoLists = new ArrayList<HomeVideoList>();
        navigationList = new ArrayList<NavigationList>();

        NavigationList homeVideoList = new NavigationList();
        AllNavigationList allHomeVideo = new AllNavigationList();

        String ContentTitle = "New Video";
        homeVideoList.setPosition(ContentTitle);
        Log.d("ContentTitle data", ContentTitle);
        allHomeVideo.setNavigationList(homeVideoList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:

                Toast.makeText(getApplicationContext(), "test Apps", Toast.LENGTH_LONG).show();

                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void TxtClickView(View view) {
        switch (view.getId()) {

            case R.id.custom_na:
                mDrawerLayout.closeDrawers();
                break;

            case R.id.preview_section_Icon:

                mDrawerLayout.closeDrawers();
                // Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                break;

            case R.id.txtHome:
                mDrawerLayout.closeDrawers();
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
                if (MobileNumber.startsWith("wifi")) {

                    String catCode = "1";
                    getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_MAIN_PAGE_OTHER, "free", "MainScreenOther", catCode);
                    //makeJsonArrayRequest(context, "http://wap.shabox.mobi/bdnewapi/Data/Fulldata", "free", "NewVideo00");
                } else {

                    String catCode = "2";
                    getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_MAIN_PAGE_ROBI, "free", "MainScreenRobi", catCode);
                    //makeJsonArrayRequest(context, "http://wap.shabox.mobi/bdnewapi/DataOther/Fulldata", "free", "NewVideo00");
                }
                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }

                break;

            // Bangla Music for other operator
            case R.id.txtBanglaMusic:
                mDrawerLayout.closeDrawers();
                //    Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
                String catCode = "E8E4F496-9CA9-4B35-BADD-9B6470BE2F74";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "banglaMusicOther", catCode);
                //String urlNewMovie = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=5";
                //makeJsonArrayRequest(context, urlNewMovie, "free", "BanglaMusic5");
                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            // English Music for other operator
            case R.id.txtEnglishMusic:
                mDrawerLayout.closeDrawers();

                String catCodeEnglishMusicOther = "74D847C2-4E98-44DA-B7A3-61C1EAE8938F";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, catCodeEnglishMusicOther, "englishMusicOther", catCodeEnglishMusicOther);
                //  Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlNewMusic = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=7";
//                makeJsonArrayRequest(context, urlNewMusic, "free", "EnglishMusic7");
                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;

            case R.id.txtEnglishMovie:
                mDrawerLayout.closeDrawers();
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
                String txtEnglishMovie = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=6";
                makeJsonArrayRequest(context, txtEnglishMovie, "free", "EnglishMovie6");
                Log.d("txtEnglishMovie", txtEnglishMovie);
                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtBanglaMovie:
                mDrawerLayout.closeDrawers();
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
                String catCodeBanglaMovieSong = "A5D68929-8921-4ECD-8151-E36A3871EB95";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "banglaMovieSong", catCodeBanglaMovieSong);
//                String urlNewFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=4";
//                makeJsonArrayRequest(context, urlNewFunny, "free", "BanglaMovie4");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtBanglaNatok:
                mDrawerLayout.closeDrawers();
                String catCodeBanglaDramaHD = "4781C5FB-0F16-4892-877D-F2F73DD4DE92";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaDramaHDOther", catCodeBanglaDramaHD);
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlBanglaNatok = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=21";
//                makeJsonArrayRequest(context, urlBanglaNatok, "free", "BanglaNatok21");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;

//Short clips other
            // Bangla Music other
            case R.id.txtSCBanglaMusic:
                mDrawerLayout.closeDrawers();
                    String catCodeBanglaMusicShortClipsOther = "5DCA7C64-F342-434A-A934-750F37D74AEC";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaMusicShortClipsOther", catCodeBanglaMusicShortClipsOther);
                //  Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlFullMovies = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=11";
//                makeJsonArrayRequest(context, urlFullMovies, "free", "SCBanglaMusic11");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
// add new
            case R.id.txtBanglaDramaClips:
                mDrawerLayout.closeDrawers();
                String catCodeBanglaDramaClipsOther = "EAB3B615-9942-462C-B531-97F255C6041D";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaDramaClipsOther",catCodeBanglaDramaClipsOther);
                //  Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String BanglaDramaClips = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=20";
//                makeJsonArrayRequest(context, BanglaDramaClips, "free", "BanglaDramaClips");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtSCEnglishMusic:
                mDrawerLayout.closeDrawers();
                String catCodeEnglishMusic = "502902E6-36D9-49AA-AF31-6C722E95C000";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "englishMusicShortClipsOther", catCodeEnglishMusic);
//                String urlFullMusicVideo = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=13";
//                makeJsonArrayRequest(context, urlFullMusicVideo, "free", "SCEnglishMusic13");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;

            case R.id.txtSCEnglishMovies:
                mDrawerLayout.closeDrawers();
                String catCodeEnglishMovieClipsOther = "C450D2D6-58BB-478C-BE83-32DC3CA9690A";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "EnglishMovieClipsOther", catCodeEnglishMovieClipsOther);
                //Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlFullDrama = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=12";
//                makeJsonArrayRequest(context, urlFullDrama, "free", "SCEnglishMovies12");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtSCBanglaMovies:
                mDrawerLayout.closeDrawers();
                String catCodeBanglaMoviecClips = "C4C85FA7-7021-4BB4-B7EB-E793D26963B3";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaMoviecClips", catCodeBanglaMoviecClips);
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlFullFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=10";
//                makeJsonArrayRequest(context, urlFullFunny, "free", "SCBanglaMovies10");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtHindiMovie:
                mDrawerLayout.closeDrawers();
                String catCodeHindiMovieClips = "5EAF33AB-0A57-4D80-9392-F212E5D209FF";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HindiMovieClips", catCodeHindiMovieClips);
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String HindiMovie = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=14";
//                makeJsonArrayRequest(context, HindiMovie, "free", "HindiMovie");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtSCBollywoodCelebrityNews:
                mDrawerLayout.closeDrawers();
                String catCodeBollywoodCelebNews = "5C5778B0-BDD2-4751-8835-A84988E9D09D";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BollywoodCelebNews", catCodeBollywoodCelebNews);
                //  Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlVClipsMovieclips = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=16";
//                makeJsonArrayRequest(context, urlVClipsMovieclips, "free", "SCBollywoodCelebrityNews16");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtSCBollywoodMovieReview:
                mDrawerLayout.closeDrawers();
                String catCodeBollyMovieReview = "104CDD74-51AA-416E-93B0-7F3931AE60BD";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BollyMovieReview", catCodeBollyMovieReview);
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlVClipsMusicvideo = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=17";
//                makeJsonArrayRequest(context, urlVClipsMusicvideo, "free", "SCBollywoodMovieReview17");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtHollywoodMovieReview:
                mDrawerLayout.closeDrawers();
                String catCodeHollywoodMovieReview = "021AB351-3182-49DF-894C-888FA66EA59F";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HollywoodMovieReview", catCodeHollywoodMovieReview);
                //Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlVClipsDrama = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=18";
//                makeJsonArrayRequest(context, urlVClipsDrama, "free", "HollywoodMovieReview18");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtHollywoodGossip:
                mDrawerLayout.closeDrawers();
                String catCodeHollywoodGossip = "C1104876-012B-4B85-8E51-F84FA6CD6DBA";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HollywoodGossip", catCodeHollywoodGossip);
                //Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlHollywoodGossip = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=19";
//                makeJsonArrayRequest(context, urlHollywoodGossip, "free", "urlHollywoodGossip");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;

            //Full Movie
            case R.id.txtFMHindiMovie:
                mDrawerLayout.closeDrawers();
                String catCodeHondiMovie = "C857EB0C-CF68-42D1-A532-7BC309F986E7";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "HondiMovie", catCodeHondiMovie);
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlVClipsFunny = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=3";
//                makeJsonArrayRequest(context, urlVClipsFunny, "FullMovie", "FMHindiMovie3");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtFMBanglaMovie:
                mDrawerLayout.closeDrawers();
                String catCodeBanglaMovie = "A5D68929-8921-4ECD-8151-E36A3871EB95";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_CATEGORYWISE_OTHER, "free", "BanglaMovie", catCodeBanglaMovie);
                //  Toast.makeText(getApplicationContext(), "Content not available", Toast.LENGTH_LONG).show();
//                String urlFMBanglaMovie = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=1";
//                makeJsonArrayRequest(context, urlFMBanglaMovie, "FullMovie", "FMBanglaMovie1");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.txtFMEnglishMovie:
                mDrawerLayout.closeDrawers();
                //Toast.makeText(getApplicationContext(), "Content not available", Toast.LENGTH_LONG).show();
                String urlFMEnglishMovie = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=2";
                makeJsonArrayRequest(context, urlFMEnglishMovie, "FullMovie", "FMEnglishMovie2");

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;

            case R.id.btnNewVideo:
                mDrawerLayout.closeDrawers();
                String catCodeNewVideo = "1";
                getCatGoryWiseContentForRobiNother(MainActivity.this, Api.URL_MAIN_PAGE_OTHER, "free", "newVideo",catCodeNewVideo);
                // Toast.makeText(getApplicationContext(), "txtNewMovies", Toast.LENGTH_LONG).show();
//                String urlNewVideo = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=list&sequent=00";
//                makeJsonArrayRequest(context, urlNewVideo, "free", "NewVideo00");
                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;

            case R.id.btnFavorites:
                mDrawerLayout.closeDrawers();


                if (MobileNumber.equals("") || MobileNumber.equals("ERROR")) {
                    NeedMSISDN needMSISDN = new NeedMSISDN();
                    needMSISDN.NeedMsisdnDialog(MainActivity.this);
                } else {
//                    String urlFavorites1 = "http://wap.shabox.mobi/BDTubeAPI/ContentMetaGet.aspx?type=FavouriteList&msisdn=" + MobileNumber + "";
//                    String urlFavorites = urlFavorites1.replace(" ", "");
//
//                    makeJsonArrayRequest(context, urlFavorites, "free", "Favorites");
                    //Log.d("urlFavorites", urlFavorites);

                    getFavourateListForRobiNother(MainActivity.this, Api.URL_FAV_LIST_FOR_OTHER,"free","favListOther","8801611137743");
                }

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;

            case R.id.LLContactUs:
                mDrawerLayout.closeDrawers();
                // Toast.makeText(getApplicationContext(), "LLFavourite", Toast.LENGTH_LONG).show();

                String MobileNumber1 = SplashActivity.resultMno_splash;
                if (MobileNumber1.equalsIgnoreCase("ERROR")) {
                    MobileNumber1 = "wifi";
                } else {
                    MobileNumber1 = SplashActivity.resultMno_splash;
                }
                ContactUs contactUs = new ContactUs();
                contactUs.Contact(MainActivity.this, MobileNumber1);

                if (!MainActivity.AppsVersion.equalsIgnoreCase(MainActivity.webVersion)) {

                    Update();

                }
                break;
            case R.id.btnHelp:
                mDrawerLayout.closeDrawers();
                // Toast.makeText(getApplicationContext(), "LLFavourite", Toast.LENGTH_LONG).show();
                HelpDialog helpDialog = new HelpDialog();
                helpDialog.Help(MainActivity.this);

                break;

            case R.id.btnFAQ:
                //Toast.makeText(getApplicationContext(), "LLFavourite", Toast.LENGTH_LONG).show();
                mDrawerLayout.closeDrawers();
                FAQDialog faqDialog = new FAQDialog();
                faqDialog.FUCK(MainActivity.this);
                break;


        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_list) {
            return true;
        } else if (id == R.id.action_search) {
            Search(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Search(final Context context) {

        final Dialog dialogOther = new Dialog(context);
        dialogOther.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  dialogOther.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogOther.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogOther.setContentView(R.layout.dialog_search);
        Window dialogWindow = dialogOther.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);

        final ImageButton IBSearch = (ImageButton) dialogOther.findViewById(R.id.IBSearch);

        IBSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                EditText etSearch = (EditText) dialogOther.findViewById(R.id.etSearch);

                if (!etSearch.getText().toString().isEmpty()) {
                    String Search = etSearch.getText().toString().trim();
                    String url = "http://wap.shabox.mobi/bdtubeapi/default.aspx?type=search&sequent=1&sString=" + Search + "";   //  http://wap.shabox.mobi/bdtubeapi/default.aspx?type=search&sequent=1&sString=
                    makeJsonArrayRequest(context, url, "free", "Search");
                    Log.d("JSONurl", url);
                }

                dialogOther.dismiss();

            }
        });

        dialogOther.show();

    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    public void makeJsonArrayRequest(final Context context, String url, final String MenuFree, final String MenuTitle) {

        AllHomeVideo.removeHomeVideoList();
        ProgressDialog();
        Log.d("url mainActivity", url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON response", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object

                            for (int i = 0; i < response.length(); i++) {

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
                                homeVideoList.setArtist(Artist);
                                homeVideoList.setContentZedCode(ContentZedCode);
                                homeVideoList.setMenuFree(MenuFree);
                                homeVideoList.setMenuTitle(MenuTitle);

                                // Log.d("JSON data", PreviewURL + "    ContentCategoryCode: " + PreviewURLserch);

                                allHomeVideo.setHomeVideoList(homeVideoList);
                                listCustomize();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        hideProgressDialog();
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

    public void listCustomize() {

        homeVideoAdopter = new HomeVideoAdopter(this, homeVideoLists);
        pullToRListView.setAdapter(homeVideoAdopter);

        homeVideoAdopter.notifyDataSetChanged();
        pullToRListView.onRefreshComplete();

    }

    public void ProgressDialog() {

        progressDialog = new ProgressDialog(
                MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
        mDrawerLayout.closeDrawers();
    }

    public void hideProgressDialog() {

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }


    public void ChargingJsonArrayRequestVersion(final Context context, String url) {

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
                                String version = homeURL.getString("vesion");

                                webVersion = version;
                                Log.d("version", version);

                                getPackage(version);
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


    public void getPackage(String UpdateString) {


        try {
            PackageInfo pinfo;
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            // Google play problem versionName can be define publicly line 858

            String versionName = "";
            versionName = pinfo.versionName;

            AppsVersion = versionName;
            Log.d("versionName package", versionName + " version web " + UpdateString);

            if (!versionName.equalsIgnoreCase(UpdateString)) {

                Update();

            }
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }


    }

    public void Update() {


        final Dialog updateDialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
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


                String url = "http://203.76.126.210/shaboxbuddy/All_AppUpdateLog.php?Email=" + SharedPreferencesHelper.getSelectedFBOREMAIL(MainActivity.this) + "&MNO=" + MobileNumber + "&AppName=bdtube&AppVersion=" + AppsVersion + "";
                WebView webView = new WebView(MainActivity.this);
                webView.loadUrl(url);
                Log.d("UpdateLog", url + appPackageName);
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

    int length;

    private void getCatGoryWiseContentForRobiNother(Context applicationContext, String url, final String free, final String banglaMusicHD, String catCode) {

        AllHomeVideo.removeHomeVideoList();
        ProgressDialog();

        JSONObject js = new JSONObject();
        try {
            js.put("CatCode", catCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = js.toString();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("JSON response", response.toString());

                try {
                    // Parsing json array response
                    // loop through each json object

                    if (response.length() > 100) {

                        length = 100;
                    } else {
                        length = response.length();
                    }

                    for (int i = 0; i < length; i++) {

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
                hideProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", "error"+error.getMessage());
            }
        });

        //StringRequest request = new StringRequest()

        Volley.newRequestQueue(applicationContext).add(request);

    }

    int lengthFav;

    private void getFavourateListForRobiNother(Context applicationContext, String url, final String free, final String banglaMusicHD, String msisdn) {

        AllHomeVideo.removeHomeVideoList();
        ProgressDialog();

        JSONObject js = new JSONObject();
        try {
            js.put("MSISDN", msisdn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = js.toString();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("JSON response", response.toString());

                try {
                    // Parsing json array response
                    // loop through each json object

                    if (response.length() > 100) {

                        lengthFav = 100;
                    } else {
                        lengthFav = response.length();
                    }

                    for (int i = 0; i < lengthFav; i++) {

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
                hideProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response", error.getMessage());
            }
        });

        //StringRequest request = new StringRequest()

        Volley.newRequestQueue(applicationContext).add(request);

    }

}


