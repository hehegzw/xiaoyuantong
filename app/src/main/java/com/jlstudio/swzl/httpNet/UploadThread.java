package com.jlstudio.swzl.httpNet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joe on 2015/10/31.
 */
public class UploadThread extends Thread{
    private String fileNname;

    /**
     * 只需要传入一个文件名即可上传
     * @param fileNname
     */
    public UploadThread(String fileNname){
        this.fileNname = fileNname;
    }
    @Override
    public void run() {
        String boundary = "********************";
        String prefix = "--";
        String end = "\r\n";

        try {
            URL httpUrl = new URL("");
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置请求方式
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(prefix + boundary + end);
            out.writeBytes("Content-Disposition:form-data:"+"name=\"file\";filename=\""+"Sky.jpg"+"\""+end);
            out.writeBytes(end);
            FileInputStream fileInputStream = new FileInputStream(new File(fileNname));
            byte[] b = new byte[1024*4];
            int len;
            while ((len = fileInputStream.read(b) )!= -1){
                out.write(b,0,len);
            }
            out.writeBytes(end);
            out.writeBytes(prefix + boundary + prefix + end);
            out.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuffer sb = new StringBuffer();
            String str;
            while((str = reader.readLine())!=null){
                sb.append(str);
            }
            System.out.println("response:" + sb.toString());
            if(out!=null){
                out.close();
            }
            if (reader!=null){
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.run();
    }
}
