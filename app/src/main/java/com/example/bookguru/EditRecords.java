package com.example.bookguru;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class EditRecords extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText editBookName, editAuthorName, editPublisherName, editPublishingDate;
    private static TextView tv_civ;
    private static String cItemcode = "";
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.43.153/ancuin3/UpdateQty.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    public static final String BOOK_NAME = "BOOK NAME";
    public static final String AUTHOR_NAME = "AUTHOR";
    public static final String PUBLISHER_NAME = "PUBLISHER";
    public static final String PUBLISHING_DATE = "PUBLISHING DATE";
    public static final String ID = "ID";
    private String book_title, author_name, publisher_name, publishing_date, aydi;

    public static String BookTitle = "";
    public static String Author = "";
    public static String Publisher = "";
    public static String PublicationDate = "";

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_records);
        editBookName = (EditText) findViewById(R.id.inputBookName);
        editAuthorName = (EditText) findViewById(R.id.inputAuthorName);
        editPublisherName = (EditText) findViewById(R.id.inputAuthorName);
        editPublishingDate = (EditText) findViewById(R.id.inputPublishingDate);
        btnQuery = (Button) findViewById(R.id.btnQuery);


        Intent i = getIntent();
        BookTitle = i.getStringExtra(BOOK_NAME);
        author_name = i.getStringExtra(AUTHOR_NAME);
        publisher_name = i.getStringExtra(PUBLISHER_NAME);
        publishing_date = i.getStringExtra(PUBLISHING_DATE);
        aydi = i.getStringExtra(ID);
        editBookName.setText(book_title);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book_title = editBookName.getText().toString();
                author_name = editAuthorName.getText().toString();
                publisher_name = editPublisherName.getText().toString();
                publishing_date = editPublishingDate.getText().toString();
                new uploadDataToURL().execute();
            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(EditRecords.this);

        public uploadDataToURL(){}

        @Override
        protected void onPreExecute(){
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

                cPostSQL = aydi;
                cv.put("id",cPostSQL);

                cPostSQL = " '" + book_title + "' ";
                cv.put("book_title",cPostSQL);

                cPostSQL = " '" + author_name + "' ";
                cv.put("author_name",cPostSQL);

                cPostSQL = " '" + publisher_name + "' ";
                cv.put("publisher_name",cPostSQL);

                cPostSQL = " '" + publishing_date + "' ";
                cv.put("publication_date",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if(json != null){
                    nSuccess = json.getInt(TAG_SUCCESS);

                    if(nSuccess == 1){
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                }
                else{
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
            AlertDialog.Builder alert = new AlertDialog.Builder(EditRecords.this);
            if(s != null){
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")){ }
                Toast.makeText(EditRecords.this, s , Toast.LENGTH_SHORT).show();
            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}