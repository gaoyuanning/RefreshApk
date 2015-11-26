package com.example.try_downloadfile_progress;
/**
 * @author harvic
 * @date 2014-5-7
 * @address http://blog.csdn.net/harvic880925
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Common {
	public static final String SERVER_IP="http://192.168.0.114:5001/";
	public static final String SERVER_ADDRESS=SERVER_IP+"try_downloadFile_progress_server/index.php";//锟斤拷锟斤拷锟斤拷掳锟斤拷锟街�
	public static final String UPDATESOFTADDRESS=SERVER_IP+"try_downloadFile_progress_server/update_pakage/baidu.apk";//锟斤拷锟斤拷锟斤拷掳锟斤拷锟街�

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷筒锟窖�锟斤拷锟襟，凤拷锟截查到锟斤拷StringBuilder锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param ArrayList
	 *            <NameValuePair> vps POST锟斤拷锟斤拷锟侥诧拷值锟斤拷
	 * @return StringBuilder builder 锟斤拷锟截查到锟侥斤拷锟�
	 * @throws Exception
	 */
	public static StringBuilder post_to_server(List<NameValuePair> vps) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = null;
			// 锟斤拷锟斤拷httpost.锟斤拷锟绞憋拷锟截凤拷锟斤拷锟斤拷锟斤拷址
			HttpPost httpost = new HttpPost(SERVER_ADDRESS);
			StringBuilder builder = new StringBuilder();

			httpost.setEntity(new UrlEncodedFormEntity(vps, HTTP.UTF_8));
			response = httpclient.execute(httpost); // 执锟斤拷

			if (response.getEntity() != null) {
				// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�JSON没写锟皆ｏ拷锟斤拷锟斤拷腔锟斤拷锟届常锟斤拷锟斤拷执锟叫诧拷锟斤拷去锟斤拷
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				String s = reader.readLine();
				for (; s != null; s = reader.readLine()) {
					builder.append(s);
				}
			}
			return builder;

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("msg",e.getMessage());
			return null;
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();// 锟截憋拷锟斤拷锟斤拷
				// 锟斤拷锟斤拷锟斤拷锟酵凤拷锟斤拷锟接的凤拷锟斤拷锟斤拷锟斤拷锟斤拷
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("msg",e.getMessage());
			}
		}
	}
	
	/**
	 * 锟斤拷取锟斤拷锟斤拷姹撅拷锟�
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context) {
        int verCode = -1;
        try {
        	//注锟解："com.example.try_downloadfile_progress"锟斤拷应AndroidManifest.xml锟斤拷锟�package="锟斤拷锟斤拷"锟斤拷锟斤拷
            verCode = context.getPackageManager().getPackageInfo(
                    "com.example.try_downloadfile_progress", 0).versionCode;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verCode;
    }
   /**
    * 锟斤拷取锟芥本锟斤拷锟斤拷
    * @param context
    * @return
    */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.example.try_downloadfile_progress", 0).versionName;
        } catch (NameNotFoundException e) {
        	Log.e("msg",e.getMessage());
        }
        return verName;   
}
	
	
	
	
	
	
}
