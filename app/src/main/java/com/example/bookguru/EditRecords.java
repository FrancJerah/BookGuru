package com.example.bookguru;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class EditRecords extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText edtitemcode,names;
    private static TextView tv_civ;
    private static String cItemcode = "";
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.254.102/ancuin3/UpdateQty.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    String[] StringStatus = new String[] {"Single","Married","Widow","Divorced"};
    public static final String EMAIL = "EMAIL";
    public static final String GENDER = "GENDER";
    public static final String CIVIL = "CIVIL";
    public static final String ID = "ID";
    private String ems,gen,civ,aydi;

    public static String fullname = "";
    public static String Gender = "";
    public static String StatusofUser = "";

    RadioButton male,female;
    RadioGroup RDgroup;
    View.OnClickListener MaleandFemale;

    Spinner status;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_records);
        names = (EditText) findViewById(R.id.inputBookName);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        edtitemcode = (EditText) findViewById(R.id.inputBookName);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        RDgroup = (RadioGroup) findViewById(R.id.inputAuthor);
        status = (Spinner) findViewById(R.id.inputPublisher);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        tv_civ = (TextView) findViewById(R.id.textView3);

        Intent i = getIntent();
        ems = i.getStringExtra(EMAIL);
        gen = i.getStringExtra(GENDER);
        civ = i.getStringExtra(CIVIL);
        aydi = i.getStringExtra(ID);
        names.setText(ems);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = edtitemcode.getText().toString();
                new uploadDataToURL().execute();
            }
        });
        if("Male".equals(gen)){
            RDgroup.check(R.id.male);
        }
        else{
            RDgroup.check(R.id.female);
        }
        MaleandFemale = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rdoList = (RadioButton) view;
                switch (rdoList.getId()){
                    case R.id.male:
                        Gender = "Male";
                        break;
                    case R.id.female:
                        Gender = "Female";
                        break;
                }
            }
        };
        male.setOnClickListener(MaleandFemale);
        female.setOnClickListener(MaleandFemale);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,StringStatus);
        status.setAdapter(adapter);

        status.setSelection(adapter.getPosition(civ));
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int stats, long l) {
                String text = adapterView.getItemAtPosition(stats).toString().trim();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                switch (stats){
                    case 0:
                        StatusofUser = "Single";
                        break;
                    case 1:
                        StatusofUser = "Married";
                        break;
                    case 2:
                        StatusofUser = "Widow";
                        break;
                    case 3:
                        StatusofUser = "Divorced";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                cPostSQL = " '" + fullname + "' ";
                cv.put("fname",cPostSQL);

                cPostSQL = " '" + Gender + "' ";
                cv.put("gen",cPostSQL);

                cPostSQL = " '" + StatusofUser + "' ";
                cv.put("civstats",cPostSQL);

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