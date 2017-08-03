# MoMo Android SDK

At a minimum, this SDK is designed to work with Android SDK 14.


## Installation

To use the MoMo Android SDK, add the compile dependency with the latest version of the MoMo SDK.

### Gradle

Step 1. Import SDK
Add the JitPack repository to your `build.gradle`:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:
```
dependencies {
    compile 'com.github.momodevelopment:androidSdkV2.2-Beta:v2.2'
}
```
 
Step 2. Config AndroidMainfest
```
<uses-permission android:name="android.permission.INTERNET" />
```
Step 3. Build Layout
Confirm order Activity
```
import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNameMap;

void onCreate(Bundle savedInstanceState) 
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
```
- Display MoMo button label language (required): English = "MoMo e-wallet", Vietnamese = "Ví MoMo"
- Display icon or button color (optional): title color #b0006d , icon http://app.momo.vn:81/momo_app/logo/MoMo.png 

Step 4. Get token & request payment
```
//Get token through MoMo app 
void requestToken() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
            amount = edAmount.getText().toString().trim();

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, amount);
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);
        //client Optional
        eventValue.put(MoMoParameterNamePayment.FEE, fee);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);

        //client call webview
        eventValue.put(MoMoParameterNamePayment.REQUEST_ID, "uber-req-28nyli"+ System.currentTimeMillis());
        eventValue.put(MoMoParameterNamePayment.PARTNER_CLIENT_ID, "Uberbjf7k9" +System.currentTimeMillis());
        eventValue.put(MoMoParameterNamePayment.RETURN_URL, "https://momo.vn");
        eventValue.put(MoMoParameterNamePayment.NOTIFY_URL, "https://momo.vn");
        eventValue.put(MoMoParameterNamePayment.PARTNERT_CODE, "CGV19072017");

        //client info custom parameter
        JSONObject objExtra = new JSONObject();
        try {
            objExtra.put("mobile", "+840966132647");
            objExtra.put("email", "momo-email@gmail.com");
            objExtra.put("fullname", "SDK Team");
            objExtra.put("lang", "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        eventValue.put(MoMoParameterNamePayment.EXTRA, objExtra);
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    } 
//Get token callback from MoMo app an submit to server side 
void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE 
                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response 
                    String phoneNumber = data.getStringExtra("phonenumber");
                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL 
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    tvMessage.setText("message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                } else {
                    //TOKEN FAIL
                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                }
            } else {
                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
            }
        } else {
            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }
    }
```


