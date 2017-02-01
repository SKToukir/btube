package bdtube.vumobile.com.bdtube;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import bdtube.vumobile.com.bdtube.AlartMessage.AlertMessage;
import bdtube.vumobile.com.bdtube.GCM.RegistrationIntentService;
import bdtube.vumobile.com.bdtube.SoapCall.MSISDNDetector;
import bdtube.vumobile.com.bdtube.SoapCall.RequiredUserInfo;
import bdtube.vumobile.com.bdtube.app.AppController;
import bdtube.vumobile.com.bdtube.app.CommonString;
import bdtube.vumobile.com.bdtube.util.NetworkChecker;
import bdtube.vumobile.com.bdtube.util.SharedPreferencesHelper;


public class SplashActivity extends Activity {

	private Context context;
	private ProgressBar mProgressBar;
	protected static final int TIMER_RUNTIME = 10; // in ms --> 10s

	private String applicationPushId = "";
	private TimePicker timepicker;
	AlarmManager am;
	public String response_result;
	public String text_response_result;
	String popup_rul = "";

	public String rul = "";
	public String adplayResult = "";
	public String Text_url = "";
	public static boolean flag = true;
	public static String resultMno_splash = "";
	int i = 3;
	Intent intentService;
	PendingIntent pendingIntent;
	public NetworkChecker internetConnection = new NetworkChecker();
	//ImageDownload download = new ImageDownload();
	public static String model, HS_MANUFAC_1;
    public static String HS_MOD_1, brand, name, email, HS_DIM_1;
	AsyncTask<Void, Void, Void> mRegisterTask;
//	BusyDialog busy;
//	SplashBusyDialog sDialog;
	ProgressBar pG;
	// AlertMessage alert = new AlertMessage();

    public String urlJsonObj="http://ipinfo.io/json";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String country="";


    private static final String TAG = "MainActivity";

  //  public BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean isReceiverRegistered;

    private static final int REQUEST_GET_ACCOUNT = 112;
ImageView ImageviewSplash;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Window window = SplashActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(SplashActivity.this, R.color.white));
        }

		setContentView(R.layout.activity_splash);
        CommonString.isUserLearn=true;


        ImageviewSplash=(ImageView)findViewById(R.id.ImageviewSplash);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ImageviewSplash);
        Glide.with(SplashActivity.this).load(R.drawable.splashupdate).into(imageViewTarget);



        makeJsonObjectRequest(urlJsonObj);

		context = this;



		if (!SharedPreferencesHelper.isOnline(this)) {


			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setIcon(R.drawable.wireless);

			alert.setTitle("Attention !");
			alert.setMessage("To Use This Application Please Connect To Internet");

			alert.setPositiveButton("Settings", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Intent intent = new Intent(
							Settings.ACTION_WIRELESS_SETTINGS);
					startActivity(intent);

				}
			});
			alert.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					dialog.dismiss();
					finish();

				}
			});

			alert.show();

		} else {

//			pG.setIndeterminate(true);
//			sDialog.show();

			//onContinue();

			//initPushNotification();
            if(android.os.Build.VERSION.SDK_INT > 22){
                if(isReadStorageAllowed()){
                    initUi();
                    initPushNotification();
                    return;
                }else{
                    requestStoragePermission();
                }

            }else {

                initUi();
                initPushNotification();

            }

		}
       // mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);

/*
        try {

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                      //  mInformationTextView.setText(getString(R.string.gcm_send_message));
                    } else {
                      //  mInformationTextView.setText(getString(R.string.token_error_message));
                    }
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        // Registering BroadcastReceiver
        try {
            registerReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.GET_ACCOUNTS)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }


        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.GET_ACCOUNTS},REQUEST_GET_ACCOUNT);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == REQUEST_GET_ACCOUNT){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(this,"Thanks You For Permission Granted ",Toast.LENGTH_LONG).show();
                initUi();
                initPushNotification();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
       // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
         //   LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                   // new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */



    private void initUi() {

		final Thread timerThread = new Thread() {
			@Override
			public void run() {
//				sDialog.show();
				try {

					sleep(5*960);
                    init();

				} catch (InterruptedException e) {
					// do nothing
				} finally {
					onContinue();
				}
			}
		};
		timerThread.start();
	}
    private void init() {

        try {
            resultMno_splash = "START";
            MSISDNDetector cws = new MSISDNDetector();

            cws.join();
            cws.start();

            while (resultMno_splash == "START") {
                try {
                    Thread.sleep(10);
                } catch (Exception ex) {
                }
            }

            Log.i("MSISDN  Splash", "" + resultMno_splash);

        } catch (Exception ex) {
            Log.i("EXCEPTION EXIST USER", "" + ex.toString());
            // rslt= "Exception";

        }
        DisplayMetrics dms_ = new DisplayMetrics();
        RequiredUserInfo userinfo = new RequiredUserInfo();
        String HS_MANUFAC_ = userinfo
                .deviceMANUFACTURER(SplashActivity.this);
        HS_MANUFAC_1=HS_MANUFAC_;
        String HS_MOD_ = userinfo.deviceModel(SplashActivity.this);
        HS_MOD_1=HS_MOD_;

        String HS_DIM_ = dms_.widthPixels + "x" + dms_.heightPixels;
        HS_DIM_1=HS_DIM_;
       // String IP_ = internetConnection.getLocalIpAddress();
        String MobileNumber = SplashActivity.resultMno_splash;
        if (MobileNumber.equalsIgnoreCase("ERROR")) {
            MobileNumber = "wifi";
        } else {
            MobileNumber = SplashActivity.resultMno_splash;
        }
        String possibleEmail = null;

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccountsByType(
                "com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                Log.i("MY_EMAIL_count", "" + possibleEmail);
            }
        }

        SharedPreferencesHelper.setSelectedFBOREMAIL(SplashActivity.this, possibleEmail);

       // Toast.makeText(getApplicationContext(), MobileNumber, Toast.LENGTH_LONG).show();
    }



    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
               // Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }





	public void onContinue() {
		// perform any final actions here
		
		runOnUiThread(new Runnable() {

			public void run() {
//				sDialog.dismis();
//				busy.dismis();
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
				finish();

			}
		});

	}

	String results = "";


	/**
	 * Push Notification Using GCM Initialization
	 * 
	 */



      private void makeJsonObjectRequest(String urlJsonObj ) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest( urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object

                    String message = response.getString("country");
                    country=message;
                    //Toast.makeText(context, message, Toast.LENGTH_LONG).show();




                } catch (JSONException e) {
                    e.printStackTrace();
                 /*   Toast.makeText(context,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();*/
                }
                //  hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
              /*  Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
                // hide the progress dialog
                // hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void initPushNotification() {

        model = Build.MODEL;
        brand = Build.BRAND; // like SEMC

        Log.i("MODEL ", "" + model + brand + " ");

        String possibleEmail = null;

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccountsByType(
                "com.google");
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                Log.i("MY_EMAIL_count", "" + possibleEmail);
            }
        }

       // SharedPreferencesHelper.set(SplashActivity.this, possibleEmail);
        Log.i("MY_EMAIL", "" + possibleEmail);
        /**
         * Start Push Notification Intitialization
         */

        try {
            if (!SharedPreferencesHelper.isOnline(context)) {

                AlertMessage.showErrorMessage(context,
                        "Internet Connection Error",
                        "Please connect to working Internet connection");
                return;
            }

            /**
             * Getting handset brand name, email
             */
            name = brand;
            email = possibleEmail;




            /**
             * Check if regid already presents
             */


            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }



        } catch (Exception e) {
            Log.w("GET_XCEP", "" + e.getMessage());
        }

    }


    @Override
    protected void onDestroy() {

        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
         //   unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();

    }
}
