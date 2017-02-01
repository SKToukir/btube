package bdtube.vumobile.com.bdtube.SoapCall;

import android.app.AlertDialog;

import bdtube.vumobile.com.bdtube.SplashActivity;

public class MSISDNDetector extends Thread {

	public AlertDialog ad;
	public CallSoap cs;
	public String a, b;

	public void run() {
		try {
			cs = new CallSoap();
			String resp = cs.DetectMSISDN();
			SplashActivity.resultMno_splash = resp;
			
			// ClubzAppExtend.rsltNumber=resp;
		} catch (Exception ex) {
			String errMsg = "Error";
			// ClubzAppExtend.rsltNumber=errMsg.toString();
		}

	}
}