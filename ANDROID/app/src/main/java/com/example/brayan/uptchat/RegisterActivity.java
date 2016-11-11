package com.example.brayan.uptchat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextNick;
    EditText editTextPassword;
    EditText editTextConfPassword;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextNick = (EditText) findViewById(R.id.txtNickEdit);
        editTextPassword = (EditText) findViewById(R.id.txtPassword);
        editTextConfPassword = (EditText) findViewById(R.id.txtConfiPasswd);
        context = this;


    }
    private void guardarDato(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, String.valueOf("http://192.168.1.10/uptchat/index.php/chat/registrar"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })    {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("nick",editTextNick.getText().toString());
                map.put("password",editTextPassword.getText().toString());

                return map;
            }

        };
        requestQueue.add(request);
    }

    public void register(View view) {
        if(editTextPassword.getText().toString().equals(editTextConfPassword.getText().toString())){
            guardarDato();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class );
            startActivity(intent);
        }else{
            Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }

    }
}
