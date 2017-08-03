package vn.momo.momo_partner.utils;

/**
 * Created by hungdo on 5/8/17.
 */

public class MoMoConfig {
    public static final String MOMO_APP_PAKAGE_CLASS_PRODUCTION = "com.mservice.momotransfer"; //production
    public static final String MOMO_APP_PAKAGE_CLASS_DEBUG = "com.mservice.debug";//debug
    public static final String MOMO_APP_PAKAGE_CLASS_DEVELOPER = "com.mservice";//dev


    public static final String ACTION_SDK = "com.android.momo.SDK";//action mapping
    public static final String ACTION_PAYMENT = "com.android.momo.PAYMENT";//action payment
    public static final int ENVIRONMENT_DEBUG = 0;//Debug
    public static final int ENVIRONMENT_DEVELOPER = 1;//developer
    public static final int ENVIRONMENT_PRODUCTION = 2;//production

    public static final String ACTION_TYPE_GET_TOKEN = "gettoken";//action mapping
    public static final String ACTION_TYPE_LINK = "link";//action payment

    public static final String INTENT_URL_WEB = "url";//dev
    public static final String INTENT_JSON_DATA = "INTENT_JSON_DATA";//dev
    public static final String INTENT_URL_REQUEST = "INTENT_URL_REQUEST";//dev

    public static final String MOMO_WEB_SDK_PRODUCTION= "http://10.10.10.171:8085/api/v1/payment/request"; //production

    public static final String MOMO_WEB_SDK_PAYONE_BILL = "http://172.16.43.22:8082/paygamebill";//debug

    public static final int MOMO_TIME_OUT = 60000;

    public static final String MOMO_API_QUERY_URL_DEVELOPMENT   = "http://apptest2.momo.vn:8091/queryorder";
    public static final String MOMO_PUBLIC_KEY_DEVELOPMENT      = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkpa+qMXS6O11x7jBGo9W3yxeHEsAdyDE40UoXhoQf9K6attSIclTZMEGfq6gmJm2BogVJtPkjvri5/j9mBntA8qKMzzanSQaBEbr8FyByHnf226dsLt1RbJSMLjCd3UC1n0Yq8KKvfHhvmvVbGcWfpgfo7iQTVmL0r1eQxzgnSq31EL1yYNMuaZjpHmQuT24Hmxl9W9enRtJyVTUhwKhtjOSOsR03sMnsckpFT9pn1/V9BE2Kf3rFGqc6JukXkqK6ZW9mtmGLSq3K+JRRq2w8PVmcbcvTr/adW4EL2yc1qk9Ec4HtiDhtSYd6/ov8xLVkKAQjLVt7Ex3/agRPfPrNwIDAQAB";
}
