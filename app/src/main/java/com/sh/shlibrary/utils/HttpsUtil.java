package com.sh.shlibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by admin on 2018/1/19.
 */

public class HttpsUtil {

    private String gethttps2(String token){
        String line = null;
        Map<String, String> comment = new HashMap<String, String>();
//        comment.put("Content-Type", "application/json");

        comment.put("Content-Type", "application/json;charset=UTF-8");
//        comment.put("Accept", "application/json;charset=UTF-8");
        comment.put("Authorization", "zxj001");
        Map<String,Object> body=new HashMap<>();
//        body.put("bindNbr","+8678889000000");
//        body.put("displayNbr","+862868501100");
//        body.put("displayCalleeNbr","+862868501100");
        body.put("callerNbr","+8613880404716");
        body.put("calleeNbr","+8618502821781");

//        String https = "https://apiopen2.scadc.com/rest/httpsessions/tts2Note/v1.0?app_key=sOCko1Wvwuz6QkF5IOVr2A4uCQka&access_token="+token+"&format=json";
        String https ="https://223.87.15.133/rest/httpsessions/click2Call/v2.0?app_key=QDuba64T3Z7ShQB5V7ldYhFnoh1G&access_token="+token+"&format=json";
        try{
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
            HttpsURLConnection conn = (HttpsURLConnection)new URL(https).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization","zxj001");

//            conn.setRequestProperty("Connection", "close");

            if(comment!=null){
                for(String key : comment.keySet()){
                    conn.setRequestProperty(key, comment.get(key));
                }
            }

            StringBuilder sb = new StringBuilder();

            if (body!=null) {
                // 首先组拼文本类型的参数
                sb.append(new Gson().toJson(body));

            }
//            conn.setRequestProperty("userData",sb.toString());

            conn.connect();
            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            int code=conn.getResponseCode();
            if (!TextUtils.isEmpty(sb.toString())) {
                outStream.write(sb.toString().getBytes());
                outStream.close();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb1 = new StringBuffer();
            while ((line = br.readLine()) != null)
                sb1.append(line);

        }catch(Exception e){
            Log.e(this.getClass().getName(), e.getMessage());
        }
        return line.toString();
    }


    private class MyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    private class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
            // TODO Auto-generated method stub
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
