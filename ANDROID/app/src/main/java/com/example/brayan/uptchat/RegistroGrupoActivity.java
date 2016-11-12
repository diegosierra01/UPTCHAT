package com.example.brayan.uptchat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroGrupoActivity extends AppCompatActivity {

    EditText editTextNombre;
    String JSON_STRING;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Context context;
    List itemsGr = new ArrayList();
    GruposAdapter gruposAdapter;
    ListView listViewGroup;
    BackgroundTask2 backgroundTask2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_grupo);
        editTextNombre = (EditText) findViewById(R.id.nombreGrupo);
        context = this;
        backgroundTask2 =  new BackgroundTask2(context);
        backgroundTask2.execute();
        itemsGr.clear();
        listViewGroup= (ListView) findViewById(R.id.listViewMisGrupos);
        gruposAdapter = new GruposAdapter(this, itemsGr);
        listViewGroup.setAdapter(gruposAdapter);

        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Grupo grupo = (Grupo) listViewGroup.getAdapter().getItem(position);
                Intent intent = new Intent(RegistroGrupoActivity.this, LlenaGrupoActivity.class );
                intent.putExtra("id",grupo.getId());
                intent.putExtra("nick",grupo.getNombre());
                startActivity(intent);

            }
        });
    }
    private void guardarDato(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, String.valueOf("http://192.168.43.23/uptchat/index.php/chat/registrarGrupo"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegistroGrupoActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroGrupoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })    {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("nombre",editTextNombre.getText().toString());
                return map;
            }

        };
        requestQueue.add(request);
    }

    public void registerGrupo(View view) {
            guardarDato();
        backgroundTask2 =  new BackgroundTask2(context);
        backgroundTask2.execute();
        itemsGr.clear();

    }
    class BackgroundTask2 extends AsyncTask<Void,Void,String> {

        Context ctx;
        String URLconsulta="";

        BackgroundTask2(Context ctx){
            this.ctx=ctx;
        }
        @Override
        protected void onPreExecute() {
            URLconsulta="http://192.168.43.23/uptchat/index.php/chat/listarGrupos";
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
                jsonArray=jsonObject.getJSONArray("grupos");
                for (int i=0; i < jsonArray.length();i++ ){
                    JSONObject JSO = jsonArray.getJSONObject(i);
                    itemsGr.add(new Grupo(JSO.getString("id"),JSO.getString("nombre")));
                }
                gruposAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}
