package example.com.myping.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessUitl implements Runnable {

    public static void main(String[] args) {
        String command = "ping 192.168.6.61";
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            final InputStream is2 = p.getErrorStream();
            final InputStream is1 = p.getInputStream();

            new Thread() {
                public void run() {
                    try {
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
                        String line1;
                        while ((line1 = br1.readLine()) != null) {
                            System.out.println("输出流==" + line1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            is1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                public void run() {
                    try {
                        BufferedReader br2 = new BufferedReader(new InputStreamReader(is2, "utf-8"));
                        String line2;
                        while ((line2 = br2.readLine()) != null) {
                            System.out.println("错误流===" + line2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            is2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            p.waitFor();
            p.destroy();
            System.out.println("我想被打印...");
        } catch (Exception e) {
            try {
                if (p != null) {
                    p.getErrorStream().close();
                    p.getInputStream().close();
                    p.getOutputStream().close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }


    @Override
    public void run() {

    }
}