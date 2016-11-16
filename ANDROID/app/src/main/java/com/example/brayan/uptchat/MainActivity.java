package com.example.brayan.uptchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    ListView listViewGroup;
    String JSON_STRING;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Context context;

    BackgroundTask backgroundTask;
    BackgroundTask2 backgroundTask2;
    List itemsUser = new ArrayList();
    UsuariosAdapter usuariosAdapter;
    List itemsGr = new ArrayList();
    GruposAdapter gruposAdapter;
    EditText editTextNewPassword;
    TextView textViewNick;
    String idUser;
    String idGroup;
    ArrayList<String> list_idUsers;
    int cont;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
       // setSupportActionBar(myToolbar);
        context=this;

        Resources res = getResources();

        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_account_circle_black_24dp));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_people_black_24dp));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_border_color_black_24dp));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
        backgroundTask =  new BackgroundTask(context);
        backgroundTask.execute();
        //itemsUser.clear();
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
               // Toast.makeText(context,tabId, Toast.LENGTH_SHORT).show();
                switch (tabId){
                    case "mitab1":
                        backgroundTask =  new BackgroundTask(context);
                        backgroundTask.execute();
                        itemsUser.clear();
                        break;
                    case "mitab2":
                        backgroundTask2 =  new BackgroundTask2(context);
                        backgroundTask2.execute();
                        itemsGr.clear();
                        break;
                }

            }
        });

        listView = (ListView) findViewById(R.id.listView);
        usuariosAdapter = new UsuariosAdapter(this, itemsUser);
        listView.setAdapter(usuariosAdapter);

        list_idUsers = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                view.setSelected(true); //****new

                Usuario user = (Usuario) listView.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, MensajeActivity.class );
                intent.putExtra("id",user.getId());
                intent.putExtra("nick",user.getNick());
                startActivity(intent);

            }
        });


        listViewGroup= (ListView) findViewById(R.id.listViewGrupos);
        gruposAdapter = new GruposAdapter(this, itemsGr);
        listViewGroup.setAdapter(gruposAdapter);

        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Grupo grupo = (Grupo) listViewGroup.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, MensajeActivity.class );
                intent.putExtra("id",grupo.getId());
                intent.putExtra("nick",grupo.getNombre());
                startActivity(intent);

            }
        });

    //aqui bakcgrTAask

        SharedPreferences sharedPreferences = getSharedPreferences("dataSession", Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString("idUserSession", "");
        String nickUser = sharedPreferences.getString("nickSession", "");
        idGroup = sharedPreferences.getString("idGrupoSession", "");
        String nombreGrupo = sharedPreferences.getString("nombreGrupoSession", "");

        editTextNewPassword = (EditText) findViewById(R.id.editextPassword);
        textViewNick = (TextView)  findViewById(R.id.txtNickEdit);
        textViewNick.setText(nickUser);
        textViewNick.setTextColor(Color.rgb(63,44,43));
        textViewNick.setTextSize(24);


    }
    public void goCrearGrupo(View view) {
        Intent intent = new Intent(MainActivity.this, RegistroGrupoActivity.class );
        startActivity(intent);

    }
    class BackgroundTask extends AsyncTask<Void,Void,String> {

        Context ctx;
        String URLconsulta="";

        BackgroundTask(Context ctx){
            this.ctx=ctx;
        }
        @Override
        protected void onPreExecute() {
            URLconsulta="http://192.168.43.23/uptchat/index.php/chat/listarUsuarios";
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
                jsonArray=jsonObject.getJSONArray("usuarios");
                for (int i=0; i < jsonArray.length();i++ ){
                    JSONObject JSO = jsonArray.getJSONObject(i);
                    itemsUser.add(new Usuario(JSO.getString("id"),JSO.getString("nick")));
                }
                usuariosAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

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


    private void guardarDato(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, String.valueOf("http://192.168.1.4/uptchat/index.php/chat/editarPerfil"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })    {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",String.valueOf(idUser));
                map.put("password",editTextNewPassword.getText().toString());
                return map;
            }

        };
        requestQueue.add(request);
    }
    public void cambiarPassword(View view) {
            guardarDato();
    }
}
