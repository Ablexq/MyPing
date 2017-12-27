package example.com.myping.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import example.com.myping.MyApplication;
import example.com.myping.util.NetUtil;
import example.com.myping.util.ToastUtil;

/**
 *
 */

public class HttpURLConnectionUtil {

    public static String httpURLConectionGET(String path) {

        if (!NetUtil.checkNetworkInfo(MyApplication.getContext())) {
            ToastUtil.showToast("网络异常，请查看网络设置");
            return "";
        }

//        BufferedReader br = null;
        HttpURLConnection connection = null;
        ByteArrayOutputStream baos=null;
        try {
            URL url = new URL(path);

            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            setConnection(connection);
            connection.connect();

//            StringBuilder sb = null;
            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = inputStream.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                return baos.toString("utf-8");
//                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line;
//                sb = new StringBuilder();
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//            return sb != null ? sb.toString() : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap httpURLConectionGETBitmap(String path) {

        if (!NetUtil.checkNetworkInfo(MyApplication.getContext())) {
            ToastUtil.showToast("网络异常，请查看网络设置");
            return null;
        }

        HttpURLConnection connection = null;
        InputStream is=null;
        try {
            URL url = new URL(path);

            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }

            setConnection(connection);
            connection.connect();

            is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


    private static void setConnection(HttpURLConnection connection) {
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(30 * 1000);
            connection.setReadTimeout(30 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
