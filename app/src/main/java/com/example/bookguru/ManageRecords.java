package com.example.bookguru;

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
    TextView textView,txtDefault,txtDefault_gender,txtDefault_civilStatus,txtDefault_ID;
    private static EditText edtitemcode;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.254.102/ancuin3/SelectItemDetails.php";
    private static String urlHostDelete = "http://192.168.254.102/ancuin3/delete.php";
    private static String urlHostGender = "http://192.168.254.102/ancuin3/selectGender.php";
    private static String urlHostCivilStatus = "http://192.168.254.102/ancuin3/selectCivilStatus.php";
    private static String urlHostID = "http://192.168.254.102/ancuin3/selectid.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String cItemcode = "";

    public static String wew = "";
    public static String gender = "";
    public static String civilstats = "";

    private String ems,gen,civ,aydi;

    String cItemSelected, cItemSelected_gender, cItemSelected_civilStatus, cItemSelected_ID;
    ArrayAdapter <String> adapter_fnames;
    ArrayAdapter <String> adapter_gender;
    ArrayAdapter <String> adapter_civilStatus;
    ArrayAdapter <String> adapter_ID;
    ArrayList <String> list_fnames;
    ArrayList <String> list_gender;
    ArrayList <String> list_civilStatus;
    ArrayList <String> list_ID;
    AdapterView.OnItemLongClickListener longItemListener_fnames;
    Context context = this;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_records);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtitemcode = (EditText) findViewById(R.id.inputBookName);
        txtDefault = (TextView) findViewById(R.id.tv_default);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        txtDefault_gender = (TextView) findViewById(R.id.txt_gender);
        txtDefault_civilStatus = (TextView) findViewById(R.id.txt_civilStatus);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);

        txtDefault.setVisibility(View.GONE);
        txtDefault_gender.setVisibility(View.GONE);
        txtDefault_civilStatus.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);

        Toast.makeText(ManageRecords.this,"Nothing Selected", Toast.LENGTH_SHORT).show();
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cItemcode = edtitemcode.getText().toString();

                new uploadDataToURL().execute();
                new Gender().execute();
                new Civil().execute();
                new id().execute();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                cItemSelected = adapter_fnames.getItem(position);
                cItemSelected_gender = adapter_gender.getItem(position);
                cItemSelected_civilStatus = adapter_civilStatus.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                alert_confirm.setMessage("Edit the records of" + " "+ cItemSelected);
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefault.setText(cItemSelected);
                        txtDefault_gender.setText(cItemSelected_gender);
                        txtDefault_civilStatus.setText(cItemSelected_civilStatus);
                        txtDefault_ID.setText(cItemSelected_ID);

                        ems = txtDefault.getText().toString().trim();
                        gen = txtDefault_gender.getText().toString().trim();
                        civ = txtDefault_civilStatus.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();

                        Intent intent = new Intent(ManageRecords.this,EditRecords.class);
                        intent.putExtra(EditRecords.EMAIL,ems);
                        intent.putExtra(EditRecords.GENDER,gen);
                        intent.putExtra(EditRecords.CIVIL,civ);
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
                cItemSelected = adapter_fnames.getItem(position);
                cItemSelected_gender = adapter_gender.getItem(position);
                cItemSelected_civilStatus = adapter_civilStatus.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                alert_confirm.setMessage("Are you sure you want to delete" + " " + cItemSelected + "?");
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtDefault_ID.setText(cItemSelected_ID);
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
                cPostSQL = cItemcode;
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
                list_fnames = new ArrayList<String>(Arrays.asList(fnames));
                adapter_fnames = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_fnames);

                listView.setAdapter(adapter_fnames);
                textView.setText(listView.getAdapter().getCount() + " " + "record(s) found");
            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }

    private class Gender extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

        public Gender(){}

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
                cPostSQL = cItemcode;
                cv.put("code",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostGender, "POST", cv);
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
                String gender = Gender;
                String str = gender;
                final String Genders[] = str.split("-");
                list_gender = new ArrayList<String>(Arrays.asList(Genders));
                adapter_gender = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_gender);

            } else{
                alert.setMessage("Query interrupted... \n Please Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Civil extends AsyncTask<String, String, String> {
        String cPost = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageRecords.this);

        public Civil(){}

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
                cPostSQL = cItemcode;
                cv.put("code",cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHostCivilStatus, "POST", cv);
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
        protected void onPostExecute(String CS){
            super.onPostExecute(CS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ManageRecords.this);
            if(CS != null){
                if (isEmpty.equals("") && !CS.equals("HTTPSERVER_ERROR")){ }
                String CivilStat = CS;
                String str = CivilStat;
                final String Civs[] = str.split("-");
                list_civilStatus = new ArrayList<String>(Arrays.asList(Civs));
                adapter_civilStatus = new ArrayAdapter<String>(ManageRecords.this, android.R.layout.simple_list_item_1,list_civilStatus);

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
                cPostSQL = cItemcode;
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
                cPostSQL = cItemSelected_ID;
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