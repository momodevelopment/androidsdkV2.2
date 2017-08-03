/**
 * Created By Bao.Nguyen on Sep 24, 2015
 * Edited by Lanh.Luu,Hung.Do on 2/24/17.
 * https://github.com/lanhmomo/MoMoPaySDK
 */


package momo.momo_partner.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import vn.momo.momo_partner.utils.ClientProgressBar;
import vn.momo.momo_partner.utils.MoMoConfig;
import vn.momo.momo_partner.utils.MoMoUtils;

public class ServerHttpAsyncTask extends AsyncTask<Void, Void, String> {
    private Activity activity;
    private ClientProgressBar progressBar;
    private RequestToServerListener listener;
//    private String jsonParam = "";
    private String urlEndPoint = "";
    Bundle dataServer = null;


    public ServerHttpAsyncTask(Activity activity, RequestToServerListener listener, Bundle dataServer, String urlEndPoint) {
        this.activity = activity;
        this.listener = listener;
        this.dataServer = dataServer;
        this.urlEndPoint = urlEndPoint;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if(this.progressBar == null) {
            this.progressBar = new ClientProgressBar();
        }
        Log.d("Đang thực hiện","");

        this.progressBar.showProgessDialog(this.activity, "Đang thực hiện");
        this.progressBar.forceDimissProgessDialog(MoMoConfig.MOMO_TIME_OUT);
        this.progressBar.setCancelable(true);
    }

    protected String doInBackground(Void... params) {
        String response = "";
        String token = dataServer.getString("data");
        String phoneNumber = dataServer.getString("phonenumber");
        JSONObject jsonServerPost = new JSONObject();
        JSONObject merchantPakage = new JSONObject();
        //your uniqueue id here
        final String tranId_test = UUID.randomUUID().toString().replaceAll("-", "");
        final String billId_test = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            jsonServerPost.put("merchantcode", "CGV19072017");  //Require field
            jsonServerPost.put("ipaddress", "10.10.10");     //Require field - server IP address
            jsonServerPost.put("phonenumber", phoneNumber); //Require field
            jsonServerPost.put("data", token);                  //Require field
            jsonServerPost.put("env",  dataServer.getString("env"));
            //Merchant create pakage json before hash
            merchantPakage.put("merchantcode", "CGV19072017");   //Require field
            merchantPakage.put("phonenumber",phoneNumber);   //Require field
            merchantPakage.put("amount",10000);            //Require field
            merchantPakage.put("fee",0);                 //Require field
            merchantPakage.put("transid", tranId_test);          //Require field
            merchantPakage.put("username", "abc");               //Require field
            merchantPakage.put("billid", billId_test);           //Require field
            merchantPakage.put("extra",dataServer.getString("extra"));           //Optional
//            String rsa_encrypted = ServerUtils.encryptRSA(merchantPakage.toString(), public_key);
            String rsa_encrypted = MoMoUtils.encryptRSA(merchantPakage.toString(), MoMoConfig.MOMO_PUBLIC_KEY_DEVELOPMENT);
//            dev_public_key
            jsonServerPost.put("hash", rsa_encrypted);   //Require field

            Log.d("merchantPakage ", merchantPakage.toString());

            URL url = new URL(this.urlEndPoint);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setReadTimeout(MoMoConfig.MOMO_TIME_OUT);
            httpURLConnection.setConnectTimeout(MoMoConfig.MOMO_TIME_OUT);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();
            Writer writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
            writer.write(jsonServerPost.toString());
            writer.flush();
            writer.close();
            if (httpURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                response = readStream(httpURLConnection.getInputStream());
            }
            else {
                response="";
            }
            httpURLConnection.disconnect();

        } catch (JSONException e) {
            Log.d("JSONException ", e.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(this.progressBar != null) {
            this.progressBar.dimissProgessDialog();
        }

        Log.d("pPostExecute ",result);
        this.listener.receiveResultFromServer(result);
    }

    public interface RequestToServerListener {
        void receiveResultFromServer(String var1);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
