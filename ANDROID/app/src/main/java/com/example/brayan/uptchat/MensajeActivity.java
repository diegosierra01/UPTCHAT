package com.example.brayan.uptchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
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

public class MensajeActivity extends AppCompatActivity {

    TextView textViewNick;
    TextView textViewConta;
    EditText editTextMensaje;
    Context context;
    String idUser;

    ListView listView;
    String JSON_STRING;
    JSONObject jsonObject;
    JSONArray jsonArray;

    BackgroundTask backgroundTask;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    String id;
    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);
        textViewNick = (TextView) findViewById(R.id.textViewNick);
        textViewNick.setTextColor(Color.rgb(63,44,43));
        textViewNick.setTextSize(20);

        textViewConta = (TextView) findViewById(R.id.viewConta);
        editTextMensaje = (EditText) findViewById(R.id.editTextMensaje);

        listView = (ListView) findViewById(R.id.listView2);
        items = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        listView.setAdapter(adapter);
        SharedPreferences sharedPreferences = getSharedPreferences("dataSession", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("idUserSession", "");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String nick = (String)extras.get("nick");
            id = (String)extras.get("id");
            tipo = (String)extras.get("tipo");
            textViewNick.setText(nick);

        }
        backgroundTask =  new BackgroundTask(context);
        backgroundTask.execute();

        context = this;
        contadorCaracteres();

    }


    private void guardarDato(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, String.valueOf("http://"+getString(R.string.ipBase)+"/uptchat/index.php/chat/enviarMensaje"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MensajeActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MensajeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })    {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("cadena",editTextMensaje.getText().toString());
                map.put("remitente",String.valueOf(idUser));
                map.put("destinatario", String.valueOf(id));
                return map;
            }

        };
        requestQueue.add(request);
        backgroundTask =  new BackgroundTask(context);
        backgroundTask.execute();
    }


    public void sendMsj(View view) {
        guardarDato();
        backgroundTask =  new BackgroundTask(context);
        backgroundTask.execute();

    }
    class BackgroundTask extends AsyncTask<Void,Void,String> {

        Context ctx;
        String URLconsulta="";
        String prueba="";

        BackgroundTask(Context ctx){
            this.ctx=ctx;

        }

        @Override
        protected void onPreExecute() {
            if(tipo.toString().equalsIgnoreCase("usuario")){
                URLconsulta="http://"+getString(R.string.ipBase)+"/uptchat/index.php/chat/listarMensajes?destinatario="+String.valueOf(id)+"&remitente="+String.valueOf(idUser);
            }else{
                URLconsulta="http://"+getString(R.string.ipBase)+"/uptchat/index.php/chat/listarMensajesGrupo?destinatario="+String.valueOf(id);
            }
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
                    if(!result.equalsIgnoreCase("FALSE")) {
                        jsonObject = new JSONObject(result);
                        jsonArray = jsonObject.getJSONArray("mensajes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject JSO = jsonArray.getJSONObject(i);
                            items.add(JSO.getString("nick") + ": " + JSO.getString("cadena") + "   " + JSO.getString("fecha"));
                        }
                        adapter.notifyDataSetChanged();
                    }

            } catch (JSONException e) {
                items.add("No hay mensajes" + "  ");
                adapter.notifyDataSetChanged();
                e.printStackTrace();
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

    }
    private void contadorCaracteres(){
        editTextMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int maximo = 20;
                int actual = editTextMensaje.length();
                textViewConta.setText(String.valueOf(maximo-actual));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public String getHora(){
        Calendar calendario = Calendar.getInstance();
        String ampm = null;

        if (calendario.get(Calendar.AM_PM) == Calendar.AM) {
            ampm="A.M";
        } else {
            ampm="P.M";
        }
        String hora_actual = calendario.get(calendario.HOUR) + ":" + calendario.get(calendario.MINUTE) + " " +ampm ;

        return hora_actual;
//        Toast.makeText(this, hora_actual, Toast.LENGTH_SHORT).show();

    }
    public String getFecha(){
        Calendar calendario = Calendar.getInstance();

        String dia = Integer.toString(calendario.get(Calendar.DATE));
        String mes = Integer.toString(calendario.get(Calendar.MONTH)+1);
        String annio = Integer.toString(calendario.get(Calendar.YEAR));

        String fh = dia+"/"+mes+"/"+annio;

        return fh;

    }
}
