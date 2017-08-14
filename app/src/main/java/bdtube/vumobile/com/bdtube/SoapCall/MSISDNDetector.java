package bdtube.vumobile.com.bdtube.SoapCall;

import android.app.AlertDialog;

import bdtube.vumobile.com.bdtube.SplashActivity;
import bdtube.vumobile.com.bdtube.VideoViewActivity;
import bdtube.vumobile.com.bdtube.serviceNotification.NetworkedService;

public class MSISDNDetector extends Thread {

	public AlertDialog ad;
	public CallSoap cs;
	public String a, b;

	public void run() {
		try {
			cs = new CallSoap();
			String resp = cs.DetectMSISDN();
			SplashActivity.resultMno_splash = resp;
			VideoViewActivity.resultMno = resp;
			NetworkedService.resultMno = resp;
			// ClubzAppExtend.rsltNumber=resp;
		} catch (Exception ex) {
			String errMsg = "Error";
			// ClubzAppExtend.rsltNumber=errMsg.toString();
		}

	}
}