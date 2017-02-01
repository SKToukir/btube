package bdtube.vumobile.com.bdtube.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;


public class NetworkChecker {
	
	 private Context mContext;
	
     public boolean isConnectingToInternet(Context context) {
    	this.mContext = context;
    	
    	@SuppressWarnings("unused")
        String myDeviceModel = android.os.Build.ID;
    	TelephonyManager telephonyManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
    	//telephonyManager.getDeviceId();
    	Log.i("DEVICE_MODEL_IMEI", "" + telephonyManager.getDeviceId());
    	
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      
        if (connectivity != null)
        {
            NetworkInfo[] networkInfo = connectivity.getAllNetworkInfo();
            if (networkInfo != null)
                for (NetworkInfo aNetworkInfo : networkInfo) {
                    if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        
        return false;
     }
     
    /* public String getLocalIpAddress() {
     	String ipv4=null;
         try {
             for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                 NetworkInterface intf = en.nextElement();
                 for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                     InetAddress inetAddress = enumIpAddr.nextElement();
//                     System.out.println("ip1--:" + inetAddress);
//                     System.out.println("ip2--:" + inetAddress.getHostAddress());

           // for getting IPV4 format
           if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) {

                         String ip = inetAddress.getHostAddress().toString();
                         System.out.println("ip---::" + ip);
                         return ipv4;
                     }
                 }
             }
         } catch (Exception ex) {
             Log.e("IP Address", ex.toString());
         }
         return null;
     }*/
     
     
     
}
