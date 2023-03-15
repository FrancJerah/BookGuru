package com.example.bookguru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateNewRecord extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText functionBookName;
    private static EditText functionAuthorName;
    private static EditText functionPublisherName;
    private static EditText functionPublishingDate;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.254.102/ancuin3/InsertTrans.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";

    private static String bookName = "";
    private static String authorName = "";
    private static String publisherName = "";
    private static String publishingDate  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_customer);

        btnQuery = (Button) findViewById(R.id.btnQuery);
        functionBookName = (EditText) findViewById(R.id.inputBookName);
        functionAuthorName = (EditText) findViewById(R.id.inputAuthor);
        functionPublisherName = (EditText) findViewById(R.id.inputPublisher);
        functionPublishingDate= (EditText) findViewById(R.id.inputPublishingDate);


        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookName = functionBookName.getText().toString();
                authorName = functionAuthorName.getText().toString();
                publisherName = functionPublisherName.getText().toString();
                publishingDate = functionPublishingDate.getText().toString();
                new uploadDataToURL().execute();
            }
        });

    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPOSTSQL = "", cMessage = "Querying Data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(CreateNewRecord.this);

        public uploadDataToURL(){ }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params){
            int nSuccess;
            try{
                ContentValues cv = new ContentValues();
                cPOSTSQL = "'"+ bookName +"','" + authorName + "','" + publisherName + "','" + publishingDate + "'";
                cv.put("code", cPOSTSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if(json != null){
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if(nSuccess == 1){
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else{
                        return json.getString(TAG_MESSAGE);
                    }
                } else{
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(CreateNewRecord.this);
            if(s != null){
                if(isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")){ }
                Toast.makeText(CreateNewRecord.this, s, Toast.LENGTH_SHORT).show();
            }
            else{
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
