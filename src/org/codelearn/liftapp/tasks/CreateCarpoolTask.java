package org.codelearn.liftapp.tasks;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codelearn.liftapp.CreateCarpoolActivity;
import org.codelearn.liftapp.models.Cid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import com.google.gson.Gson;

public class CreateCarpoolTask extends AsyncTask<String, Void, String> {
	 private ProgressDialog dialog;
	 SharedPreferences prefs;
	 Editor edit;
	 Context ac;
	 public CreateCarpoolTask(Context activity) {
		 	ac = activity;
		 	dialog = new ProgressDialog(activity);
	        prefs = activity.getSharedPreferences("codelearn_carpool", Context.MODE_PRIVATE);
	        edit = prefs.edit();
	 }
	 	@Override
       protected String doInBackground(String... params) {

	 		HttpClient httpclient = new DefaultHttpClient();
 
            
            HttpPost httpPost = new HttpPost("http://codelearn-carpool.herokuapp.com/api/create");
            Cid result = new Cid();
            StringEntity se;
			try {
				se = new StringEntity(params[0]);
	            httpPost.setEntity(se);

	            httpPost.setHeader("Accept", "application/json");
	            httpPost.setHeader("Content-type", "application/json");
	            HttpResponse httpResponse = httpclient.execute(httpPost);
	           InputStream inputstream = httpResponse.getEntity().getContent();
	
	            if(inputstream != null)
	            {
	            	InputStreamReader isr = new InputStreamReader ( inputstream );
	            	Gson g = new Gson();
	            	result = g.fromJson(isr, Cid.class);
	            	
	            }
			} catch (UnsupportedEncodingException e) {
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
 
           
           return result.id;
            
       }

       @Override
       protected void onPostExecute(String result) {
    	   edit.putString("pref_carpool_id", result);
    	   edit.commit();
    	   if (dialog.isShowing()) {
               dialog.dismiss();
           }
    	   
    	   ((CreateCarpoolActivity)ac).finish();
       }

       @Override
       protected void onPreExecute() {
    	   dialog.setMessage("Creating carpool");
           dialog.show();
       }
   }
