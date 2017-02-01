package bdtube.vumobile.com.bdtube.SoapCall;


import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


//import org.ksoap2.serialization.PropertyInfo;

public class CallSoap {
    //Operation name with name space
    public final String SOAP_ACTION = "http://tempuri.org/CGW";
    public final String SOAP_ACTION_GP = "http://tempuri.org/CGWProcess";
    public final String SOAP_ACTION_BL = "http://tempuri.org/BLinkSDP6624_CGWRequest";
    public final String SOAP_ACTION_FOR_USER = "http://tempuri.org/StatusChk";
    public final String SOAP_ACTION_FOR_NEW_USER_SMS = "http://tempuri.org/newUserSMS";


    //Operation name declare
    public final String OPERATION_NAME = "CGW";
    public final String OPERATION_NAME_GP = "CGWProcess";
    public final String OPERATION_NAME_FOR_USER = "StatusChk";
    public final String OPERATION_NAME_FOR_NEW_USER_SMS = "newUserSMS";
    public final String OPERATION_NAME_BLINK = "BLinkSDP6624_CGWRequest";


    //Name space
    public final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    public final String WSDL_TARGET_NAMESPACE_FOR_USER = "http://tempuri.org/";
    public final String WSDL_TARGET_NAMESPACE_FOR_NEW_USER_SMS = "http://tempuri.org/";


    //Soap Address
    public final String SOAP_ADDRESS = "http://wap.shabox.mobi/chargingapi/Service.asmx";
    public final String SOAP_ADDRESS_GP = "http://wap.shabox.mobi/appcharging/Service1.asmx";
    public final String SOAP_ADDRESS_BL = "http://wap.shabox.mobi/BLSDPCGW/Service.asmx";
    public final String SOAP_ADDRESS_FOR_USER = "http://wap.shabox.mobi/CZStatus/";
    public final String SOAP_ADDRESS_FOR_NEW_USER_SMS = "http://wap.shabox.mobi/CZStatus/";

    public CallSoap() {

    }

    //MSISDN Detection web service
    public String DetectMSISDN() {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, "MSISDN");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE("http://wap.shabox.mobi/czapp/Service1.asmx");
        Object response = null;

        try {
            httpTransport.call("http://tempuri.org/MSISDN", envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            //response=exception.toString();
            response = "ERROR";
        }

        return response.toString();
    }


    //=====================Function for charging===============
    public String CallCharging(String MSISDN, String ChargingType, String contentCode) {
        Object response = null;
        if (MSISDN.startsWith("88017")
                || MSISDN.startsWith("017")
                || MSISDN.startsWith("+88017")) {

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_GP);
            request.addProperty("MSISDN", MSISDN);
            request.addProperty("ContentCode", contentCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            Log.d("requestSoapObjectGP", request.toString());

            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_GP);
            // Object response = null;

            try {
                httpTransport.call(SOAP_ACTION_GP, envelope);
                response = envelope.getResponse();
                Log.d("requestResponseGP", response.toString());
            } catch (Exception exception) {
                response = exception.toString();
                // response = "ERROR";
                // Log.d("requestSoapObject ", response.toString());

            }
            Log.d("requestResponseGP", response.toString());
            return response.toString();

        } else if (MSISDN.startsWith("88019")
                || MSISDN.startsWith("019")
                || MSISDN.startsWith("+88019")) {

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_BLINK);
            request.addProperty("MSISDN", MSISDN);
            request.addProperty("ServiceID", contentCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            Log.d("contentCode", contentCode + "   " + request.toString());

            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS_BL);
            // Object response = null;

            try {
                httpTransport.call(SOAP_ADDRESS_BL, envelope);
                response = envelope.getResponse();
                Log.d("response Blink", response.toString());
            } catch (Exception exception) {
                response = exception.toString();
                // response = "ERROR";
                // Log.d("requestSoapObject ", response.toString());

            }
            Log.d("response", response.toString());
            return response.toString();

        } else {
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);
            request.addProperty("msisdn", MSISDN);
            request.addProperty("ChargingType", ChargingType);
            request.addProperty("ContentCode", contentCode);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            Log.d("requestSoapObject ", request.toString());

            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            // Object response = null;

            try {
                httpTransport.call(SOAP_ACTION, envelope);
                response = envelope.getResponse();
                Log.d("requestResponse", response.toString());
            } catch (Exception exception) {
                //response=exception.toString();
                response = "ERROR";
                // Log.d("requestSoapObject ", response.toString());

            }


            return response.toString();
        }

    }

    // =================Web services forgot pin, get robi limit, pincode check etc.========================
    public String Call_WS(String msisdn, String q, String pin) {
        Object response = null;

        if (q.equals("new_user_sms")) {
            SoapObject request = new SoapObject(
                    WSDL_TARGET_NAMESPACE_FOR_NEW_USER_SMS,
                    OPERATION_NAME_FOR_NEW_USER_SMS);
            request.addProperty("msisdn", msisdn);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(
                    SOAP_ADDRESS_FOR_NEW_USER_SMS);

            try {
                httpTransport.call(SOAP_ACTION_FOR_NEW_USER_SMS, envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                // response=exception.toString();
                response = "ERROR";
                Log.w("New User Sms Error", "" + response);
            }
        } else if (q.equals("forget_pin_code")) {

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE_FOR_NEW_USER_SMS, "ForgetPin");
            request.addProperty("msisdn", msisdn);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(
                    SOAP_ADDRESS_FOR_USER);
            Log.i("FORGET_PIN", "" + msisdn);

            try {
                httpTransport.call("http://tempuri.org/ForgetPin", envelope);
                response = envelope.getResponse();
                Log.i("FORGET_PIN_RESPONSE ", "" + response);
            } catch (Exception exception) {
                // response=exception.toString();
                response = "ERROR PINCODE FORGET PIN";
            }
        } else if (q.equals("pincode_check")) {

            SoapObject request = new SoapObject(
                    WSDL_TARGET_NAMESPACE_FOR_NEW_USER_SMS, "chkPin");
            request.addProperty("msisdn", msisdn);
            request.addProperty("pincode", pin);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(
                    SOAP_ADDRESS_FOR_USER);
            Log.i("PIN_CODE_CHECK", "" + msisdn);

            try {
                httpTransport.call("http://tempuri.org/chkPin", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = "ERROR PINCODE  CHECKING";
            }
        } else if (q.equals("get_robi_message")) {

            SoapObject request = new SoapObject(
                    WSDL_TARGET_NAMESPACE_FOR_NEW_USER_SMS,
                    "ChkDwonloadCountforROBI");
            request.addProperty("msisdn", msisdn);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(
                    SOAP_ADDRESS_FOR_USER);
            Log.i("GET_ROBI_APP", "" + msisdn);

            try {
                httpTransport.call(
                        "http://tempuri.org/ChkDwonloadCountforROBI", envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                // response=exception.toString();
                response = "ERROR PINCODE  CHECKING";
            }

        }

        return response.toString();

    }

}
