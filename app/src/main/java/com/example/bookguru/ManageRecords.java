package com.example.bookguru;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ManageRecords extends AppCompatActivity {
    private static Button btnQuery;
    TextView searchResult, txtDefault, txtDefault_pubDate, txtDefault_book, txtDefault_author, txtDefault_publisher, textDefault_date, txtDefault_ID;
    private static EditText querySearch;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://172.22.26.81/ancuin3/SelectItemDetails.php";
    private static String urlHostDelete = "http://172.22.26.81/ancuin3/delete.php";
    private static String urlHostBookTitle = "http://172.22.26.81/ancuin3/selcetBookTitle.php";
    private static String urlHostAuthor = "http://172.22.26.81/ancuin3/selectAuthor.php";
    private static String urlHostPublisher = "http://172.22.26.81/ancuin3/selectPublisher.php";
    private static String urlHostPublishingDate = "http://172.22.26.81/ancuin3/selectPublishingDate.php";
    private static String urlHostID = "http://172.22.26.81/ancuin3/selectid.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String bItemcode = "";

    public static String wew = "";
    public static String Author = "";
    public static String Publisher = "";

    private String book, auth, pub, date, aydi;

    String bItemSelected, bItemSelected_author, bItemSelected_publisher, bItemSelected_publishingDate, bItemSelected_ID;
    ArrayAdapter <String> adapter_book;
    ArrayAdapter <String> adapter_author;
    ArrayAdapter <String> adapter_publisher;
    ArrayAdapter <String> adapter_date;
    ArrayAdapter <String> adapter_ID;
    ArrayList <String> list_book;
    ArrayList <String> list_author;
    ArrayList <String> list_publisher;
    ArrayList <String> list_date;
    ArrayList <String> list_ID;
    AdapterView.OnItemLongClickListener longItemListener_book;
    Context context = this;

    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_records);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        querySearch = (EditText) findViewById(R.id.querySearchBar);
        txtDefault = (TextView) findViewById(R.id.txt_ID);
        listView = (ListView) findViewById(R.id.listview);
        searchResult = (TextView) findViewById(R.id.SearchResult);
        txtDefault_author = (TextView) findViewById(R.id.txt_author);
        txtDefault_publisher = (TextView) findViewById(R.id.txt_publisher);
        txtDefault_book = (TextView) findViewById(R.id.txt_bookname);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);
        txtDefault_pubDate = (TextView) findViewById(R.id.txt_pubDate);

        txtDefault.setVisibility(View.GONE);
        txtDefault_author.setVisibility(View.GONE);
        txtDefault_publisher.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);
        txtDefault_book.setVisibility(View.GONE);
        txtDefault_pubDate.setVisibility(View.GONE);

        Toast.makeText(ManageRecords.this,"Nothing Selected", Toast.LENGTH_SHORT).show();
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bItemcode = querySearch.getText().toString();
                new uploadDataToURL().execute();
                new Author().execute();
                new Publisher().execute();
                new PubDate().execute();
                new id().execute();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                bItemSelected = adapter_book.getItem(position);
                bItemSelected_author = adapter_author.getItem(position);
                bItemSelected_publisher = adapter_publisher.getItem(position);
                bItemSelected_ID = adapter_ID.getItem(position);

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                alert_confirm.setMessage("Edit the records of" + " "+ bItemSelected);
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefault.setText(bItemSelected);
                        txtDefault_author.setText(bItemSelected_author);
                        txtDefault_publisher.setText(bItemSelected_publisher);
                        txtDefault_ID.setText(bItemSelected_ID);

                        book = txtDefault.getText().toString().trim();
                        auth = txtDefault_author.getText().toString().trim();
                        pub = txtDefault_publisher.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();
                        date = txtDefault_pubDate.getText().toString().trim();

                        Intent intent = new Intent(ManageRecords.this,EditRecords.class);
                        intent.putExtra(EditRecords.BOOK_NAME, book);
                        intent.putExtra(EditRecords.AUTHOR_NAME, auth);
                        intent.putExtra(EditRecords.PUBLISHER_NAME, pub);
                        intent.putExtra(EditRecords.PUBLISHING_DATE, date);
                        intent.putExtra(EditRecords.ID,aydi);

                        startActivity(intent);
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                bItemSelected = adapter_book.getItem(position);
                bItemSelected_author = adapter_author.getItem(position);
                bItemSelected_publisher = adapter_publisher.getItem(position);
                bItemSelected_ID = adapter_ID.getItem(position);
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                alert_confirm.setMessage("Are you sure you want to delete" + " " + bItemSelected + "?");
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefault_ID.setText(bItemSelected_ID);
                        aydi = txtDefault_ID.getText().toString().trim();
                        new delete().execute();
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

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
                cPostSQL = bItemcode;
                cv.put("code",cPostSQL);

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
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ManageRecords.this);
            if(s != null){
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")){ }
                String wew = s;
                String str = wew;
                final String fnames[] = str.split("-");
                list_book = new ArrayList<String>(Arrays.asList(fnames));
                adapter_book = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_book);

                listView.setAdapter(adapter_book);
                searchResult.setText(listView.getAdapter().getCount() + " " + "record(s) found");
            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class Author extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

        public Author(){}

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
                cPostSQL = bItemcode;
                cv.put("code",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostAuthor, "POST", cv);
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
        protected void onPostExecute(String Gender){
            super.onPostExecute(Gender);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ManageRecords.this);
            if(Gender != null){
                if (isEmpty.equals("") && !Gender.equals("HTTPSERVER_ERROR")){ }
                String author = Author;
                String str = author;
                final String Authors[] = str.split("-");
                list_author = new ArrayList<String>(Arrays.asList(Authors));
                adapter_author = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_author);

            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Publisher extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

        public Publisher(){}

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
                cPostSQL = bItemcode;
                cv.put("code",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostPublisher, "POST", cv);
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
        protected void onPostExecute(String pub){
            super.onPostExecute(pub);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ManageRecords.this);
            if(pub != null){
                if (isEmpty.equals("") && !pub.equals("HTTPSERVER_ERROR")){ }
                String Publisher = pub;
                String str = Publisher;
                final String Pubs[] = str.split("-");
                list_publisher = new ArrayList<String>(Arrays.asList(Pubs));
                adapter_publisher = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_publisher);

            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class PubDate extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

        public PubDate(){}

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
                cPostSQL = bItemcode;
                cv.put("code",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostPublishingDate, "POST", cv);
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
        protected void onPostExecute(String pubDate){
            super.onPostExecute(pubDate);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ManageRecords.this);
            if(pubDate != null){
                if (isEmpty.equals("") && !pubDate.equals("HTTPSERVER_ERROR")){ }
                String Publisher = pubDate;
                String str = Publisher;
                final String PubsDate[] = str.split("-");
                list_date = new ArrayList<String>(Arrays.asList(PubsDate));
                adapter_date = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_publisher);

            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class id extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

        public id(){}

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
                cPostSQL = bItemcode;
                cv.put("code",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostID, "POST", cv);
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
        protected void onPostExecute(String aydi){
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ManageRecords.this);
            if(aydi != null){
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")){ }
                String AYDI = aydi;
                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_ID);

            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class delete extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

        public delete(){}

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
                cPostSQL = bItemSelected_ID;
                cv.put("id",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostDelete, "POST", cv);
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
        protected void onPostExecute(String del){
            super.onPostExecute(del);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ManageRecords.this);
            if(aydi != null){
                if (isEmpty.equals("") && !del.equals("HTTPSERVER_ERROR")){ }
                Toast.makeText(ManageRecords.this, "Data Deleted", Toast.LENGTH_SHORT).show();
            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}