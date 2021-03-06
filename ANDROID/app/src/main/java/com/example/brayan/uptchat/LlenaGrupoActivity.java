package com.example.brayan.uptchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class LlenaGrupoActivity extends AppCompatActivity {

    List itemsUser = new ArrayList();
    UsuariosAdapter usuariosAdapter;
    ListView listView;
    String JSON_STRING;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TextView textViewNg;
    Context context;

    BackgroundTask backgroundTask;
    String  idGrupo;
    String idUsuario;
    List<String> idSeleccionados = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llena_grupo);
        textViewNg = (TextView) findViewById(R.id.textViewNombreGr);
        listView = (ListView) findViewById(R.id.listViewUsuariosGr);
        textViewNg.setTextColor(Color.rgb(63,44,43));
        textViewNg.setTextSize(20);
        context = this;
        itemsUser = new ArrayList<>();

        usuariosAdapter = new UsuariosAdapter(this, itemsUser);
        listView.setAdapter(usuariosAdapter);

        setupActionBar();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
             //   view.setSelected(true);
                Usuario user = (Usuario) listView.getAdapter().getItem(position);
                idUsuario = user.getId();
                idSeleccionados.add(idUsuario);

            }
        });
        /*listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //cont++;
                mode.setTitle("Seleccionados");
                Usuario user = (Usuario) listView.getAdapter().getItem(position);
                idSeleccionados.add(user.getId());
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
*/

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String nick = (String)extras.get("nick");
            idGrupo = (String)extras.get("id");
            textViewNg.setText(nick);

        }

        backgroundTask =  new BackgroundTask(context);
        backgroundTask.execute();



    }
    private void setupActionBar() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {

                if (checked) {
                    usuariosAdapter.setNewSelection(position);
                } else {
                    usuariosAdapter.removeSelection(position);
                }

                mode.setTitle(usuariosAdapter.getSelectionCount() + " items selected");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return  true;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                // CAB is initialized
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void,Void,String> {

        Context ctx;
        String URLconsulta="";

        BackgroundTask(Context ctx){
            this.ctx=ctx;
        }
        @Override
        protected void onPreExecute() {
            URLconsulta="http://"+getString(R.string.ipBase)+"/uptchat/index.php/chat/listarOpcionados?idgrupo="+String.valueOf(idGrupo);
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
                    itemsUser.add(new Usuario(JSO.getString("idusuario"),JSO.getString("nick")));
                }
                usuariosAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

    }
    private void guardarDato(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, String.valueOf("http://"+getString(R.string.ipBase)+"/uptchat/index.php/chat/addUserToGroup"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(LlenaGrupoActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LlenaGrupoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })    {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray json = new JSONArray(idSeleccionados);
                HashMap<String,String> map = new HashMap<>();
                map.put("usuarios",json.toString());
                map.put("idgrupo",String.valueOf(idGrupo));
                return map;
            }

        };
        requestQueue.add(request);
    }

    public void addToGroup(View view) {
        for (int i=0; i < idSeleccionados.size();i++){
            Toast.makeText(LlenaGrupoActivity.this,"Seleccionados: "+ idSeleccionados.get(i), Toast.LENGTH_SHORT).show();
        }
            guardarDato();
    }
}
