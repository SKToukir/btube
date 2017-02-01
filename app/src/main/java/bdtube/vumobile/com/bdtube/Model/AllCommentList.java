package bdtube.vumobile.com.bdtube.Model;

/**
 * Created by IT-10 on 1/19/2016.
 */
public class AllCommentList {
    private String Value;
    private String MSISDN;
    private String TimeStamp;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getMSISDN() {
        return MSISDN;
    }

    public void setMSISDN(String MSISDN) {
        this.MSISDN = MSISDN;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }
}
