package com.example.brayan.uptchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText editTextNick;
    EditText editTextPassword;
    String JSON_STRING;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Context context;

    BackgroundTask backgroundTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextNick = (EditText) findViewById(R.id.editNick);
        editTextPassword = (EditText) findViewById(R.id.editPassword);
        context=this;

    }
    class BackgroundTask extends AsyncTask<Void,Void,String> {

        Context ctx;
        String URLconsulta="";

        BackgroundTask(Context ctx){
            this.ctx=ctx;

        }

        @Override
        protected void onPreExecute() {
            URLconsulta="http://192.168.43.23/uptchat/index.php/chat/autenticar?nick="+editTextNick.getText().toString()+"&password="+editTextPassword.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url=new URL(URLconsulta);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                jsonObject=new JSONObject(result);
                jsonArray=jsonObject.getJSONArray("usuario");
                if(jsonArray.length() > 0){
                    Toast.makeText(ctx, "LOGUEO EXITOSO", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences("dataSession", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    JSONObject JSO = jsonArray.getJSONObject(0);
                    editor.putString("idUserSession",JSO.getString("id"));
                    editor.putString("nickSession",JSO.getString("nick"));
                    editor.apply();

                    String id = sharedPreferences.getString("idUserSession","");
                   // Toast.makeText(ctx, id, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class );
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ctx, "Invalido", Toast.LENGTH_SHORT).show();
               // Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void goRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class );
        startActivity(intent);

    }
    public void login(View view) {

        backgroundTask= new BackgroundTask(context);
        backgroundTask.execute();

    }
}
