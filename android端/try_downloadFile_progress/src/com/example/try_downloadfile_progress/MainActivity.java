package com.example.try_downloadfile_progress;
/**
 * @author harvic
 * @date 2014-5-7
 * @address http://blog.csdn.net/harvic880925
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button m_btnCheckNewestVersion;
	long m_newVerCode; //���°�İ汾��
	String m_newVerName; //���°�İ汾��
	String m_appNameStr; //���ص�����Ҫ�����APP��������
	
	Handler m_mainHandler;
	ProgressDialog m_progressDlg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//��ʼ����ر���
		initVariable();
		
		m_btnCheckNewestVersion.setOnClickListener(btnClickListener);
	}
	private void initVariable()
	{
		m_btnCheckNewestVersion = (Button)findViewById(R.id.chek_newest_version);
		m_mainHandler = new Handler();
		m_progressDlg =  new ProgressDialog(this);
		m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 // ����ProgressDialog �Ľ������Ƿ���ȷ false ���ǲ�����Ϊ����ȷ     
		m_progressDlg.setIndeterminate(false);    
		m_appNameStr = "haha.apk";
	}
	
	OnClickListener btnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new checkNewestVersionAsyncTask().execute();
		}
	};
	
	class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean>
	{
	
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(postCheckNewestVersionCommand2Server())
			{
				int vercode = Common.getVerCode(getApplicationContext()); // �õ�ǰ���һ��д�ķ���  
		         if (m_newVerCode > vercode) {  
		             return true;
		         } else {  
		             return false;
		         }  
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {//��������°汾
				doNewVersionUpdate(); // �����°汾  
			}else {
				notNewVersionDlgShow(); // ��ʾ��ǰΪ���°汾  
			}
			super.onPostExecute(result);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	/**
	 * �ӷ�������ȡ��ǰ���°汾�ţ�����ɹ�����TURE�����ʧ�ܣ�����FALSE
	 * @return
	 */
	private Boolean postCheckNewestVersionCommand2Server()
	{
		StringBuilder builder = new StringBuilder();
		JSONArray jsonArray = null;
		try {
			// ����POST������{name:value} ������
			List<NameValuePair> vps = new ArrayList<NameValuePair>();
			// ����������post������
			vps.add(new BasicNameValuePair("action", "checkNewestVersion"));
			builder = Common.post_to_server(vps);
			Log.e("msg", builder.toString());
			jsonArray = new JSONArray(builder.toString());
			if (jsonArray.length()>0) {
				if (jsonArray.getJSONObject(0).getInt("id") == 1) {
					m_newVerName = jsonArray.getJSONObject(0).getString("verName");
					m_newVerCode = jsonArray.getJSONObject(0).getLong("verCode");
					
					return true;
				}
			}
	
			return false;
		} catch (Exception e) {
			Log.e("msg",e.getMessage());
			m_newVerName="";
			m_newVerCode=-1;
			return false;
		}
	}
		
	/**
	 * ��ʾ�����°汾
	 */
	private void doNewVersionUpdate() {
		int verCode = Common.getVerCode(getApplicationContext());  
	    String verName = Common.getVerName(getApplicationContext());  
	    
	    String str= "��ǰ�汾��"+verName+" Code:"+verCode+" ,�����°汾��"+m_newVerName+
	    		" Code:"+m_newVerCode+" ,�Ƿ���£�";  
	    Dialog dialog = new AlertDialog.Builder(this).setTitle("�������").setMessage(str)  
	            // ��������  
	            .setPositiveButton("����",// ����ȷ����ť  
	                    new DialogInterface.OnClickListener() {  
	                        @Override  
	                        public void onClick(DialogInterface dialog,  
	                                int which) { 
	                            m_progressDlg.setTitle("��������");  
	                            m_progressDlg.setMessage("���Ժ�...");  
	                            downFile(Common.UPDATESOFTADDRESS);  //��ʼ����
	                        }  
	                    })  
	            .setNegativeButton("�ݲ�����",  
	                    new DialogInterface.OnClickListener() {  
	                        public void onClick(DialogInterface dialog,  
	                                int whichButton) {  
	                            // ���"ȡ��"��ť֮���˳�����  
	                            finish();  
	                        }  
	                    }).create();// ����  
	    // ��ʾ�Ի���  
	    dialog.show();  
	}
		/**
		 *  ��ʾ��ǰΪ���°汾  
		 */
		private void notNewVersionDlgShow()
		{
			int verCode = Common.getVerCode(this);  
		    String verName = Common.getVerName(this); 
		    String str="��ǰ�汾:"+verName+" Code:"+verCode+",/n�������°�,�������!";
		    Dialog dialog = new AlertDialog.Builder(this).setTitle("�������")  
		            .setMessage(str)// ��������  
		            .setPositiveButton("ȷ��",// ����ȷ����ť  
		                    new DialogInterface.OnClickListener() {  
		                        @Override  
		                        public void onClick(DialogInterface dialog,  
		                                int which) {  
		                            finish();  
		                        }  
		                    }).create();// ����  
		    // ��ʾ�Ի���  
		    dialog.show();  
		}
		private void downFile(final String url)
		{
			m_progressDlg.show();  
		    new Thread() {  
		        public void run() {  
		            HttpClient client = new DefaultHttpClient();  
		            HttpGet get = new HttpGet(url);  
		            HttpResponse response;  
		            try {  
		                response = client.execute(get);  
		                HttpEntity entity = response.getEntity();  
		                long length = entity.getContentLength();  
		                
		                m_progressDlg.setMax((int)length);//���ý����������ֵ
		                
		                InputStream is = entity.getContent();  
		                FileOutputStream fileOutputStream = null;  
		                if (is != null) {  
		                    File file = new File(  
		                            Environment.getExternalStorageDirectory(),  
		                            m_appNameStr);  
		                    fileOutputStream = new FileOutputStream(file);  
		                    byte[] buf = new byte[1024];  
		                    int ch = -1;  
		                    int count = 0;  
		                    while ((ch = is.read(buf)) != -1) {  
		                        fileOutputStream.write(buf, 0, ch);  
		                        count += ch;  
		                        if (length > 0) {  
		                        	 m_progressDlg.setProgress(count);
		                        }  
		                    }  
		                }  
		                fileOutputStream.flush();  
		                if (fileOutputStream != null) {  
		                    fileOutputStream.close();  
		                }  
		                down();  //����HANDER�Ѿ���������ˣ����԰�װ��
		            } catch (ClientProtocolException e) {  
		                e.printStackTrace();  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }  
		        }  
		    }.start();  
		}
		/**
		 * ����HANDER�Ѿ���������ˣ����԰�װ��
		 */
		private void down() {
	        m_mainHandler.post(new Runnable() {
	            public void run() {
	                m_progressDlg.cancel();
	                update();
	            }
	        });
	}
		/**
		 * ��װ����
		 */
	    void update() {
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.fromFile(new File(Environment
	                .getExternalStorageDirectory(), m_appNameStr)),
	                "application/vnd.android.package-archive");
	        startActivity(intent);
	    }


}
