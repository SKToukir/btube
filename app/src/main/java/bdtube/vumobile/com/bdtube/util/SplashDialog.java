package bdtube.vumobile.com.bdtube.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import bdtube.vumobile.com.bdtube.R;


public class SplashDialog {
	private final Dialog dialog;
	private TextView busyText;

	public SplashDialog(Context c, boolean cancelable, String text) {
		dialog = new Dialog(c, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// here we set layout of progress dialog
		dialog.setContentView(R.layout.splashdialog);
		dialog.setCancelable(cancelable);
		//busyText = (TextView) dialog.findViewById(R.id.splashbusytextview);
		busyText.setText(text + "");
	}

	

	public SplashDialog(Context c, boolean cancelable) {
		// TODO Auto-generated constructor stub
		dialog = new Dialog(c, android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// here we set layout of progress dialog
		dialog.setContentView(R.layout.splashdialog);
		dialog.setCancelable(true);
	}

	public void show() {
		dialog.show();
	}

	public void dismis() {
		dialog.cancel();
	}

}
