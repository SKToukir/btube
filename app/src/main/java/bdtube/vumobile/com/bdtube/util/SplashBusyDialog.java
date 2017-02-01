package bdtube.vumobile.com.bdtube.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import bdtube.vumobile.com.bdtube.R;


public class SplashBusyDialog {
	private final Dialog dialog;
	private TextView busyText;

	public SplashBusyDialog(Context c, boolean cancelable, String text) {
		dialog = new Dialog(c, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// here we set layout of progress dialog
		dialog.setContentView(R.layout.custom_progress_dialog);
		dialog.setCancelable(cancelable);
//		busyText = (TextView) dialog.findViewById(R.id.busytextview);
//		busyText.setText(text + "");
	}

	public SplashBusyDialog(Context c, boolean cancelable) {
		dialog = new Dialog(c, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// here we set layout of progress dialog
		dialog.setContentView(R.layout.splashdialog);
		dialog.setCancelable(true);
		
		
//		ProgressBar pB = (ProgressBar)dialog.findViewById(R.id.sProg);
//		pB.setIndeterminate(true);
		
		
	}

	public void show() {
		dialog.show();
	}

	public void dismis() {
		dialog.cancel();
	}
}
