package com.example.wisata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
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

import model.ArrayUser;
import model.User;


public class RegisterPage extends AppCompatActivity implements TextWatcher {

    EditText email_register, password_register;
    Button signup_register;
    TextView signin_register;
    CheckBox checkBox_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        getSupportActionBar().hide();

        email_register = findViewById(R.id.email_register);
        password_register = findViewById(R.id.password_register);
        signin_register = findViewById(R.id.signin_register);
        signup_register = findViewById(R.id.signup_register);
        checkBox_register = findViewById(R.id.checkBox_register);

        email_register.addTextChangedListener(this);
        password_register.addTextChangedListener(this);

        signin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_user = email_register.getText().toString().trim();
                String password_user = password_register.getText().toString().trim();

                if (email_user.isEmpty()) {
                    email_register.setError("Please Fill the Email Column!");
                } else {
                    email_register.setError("");
                }

                if (password_user.isEmpty()) {
                    password_register.setError("Please Fill the Password Column!");
                } else {
                    password_register.setError("");
                }

                if (!email_user.isEmpty() && !password_user.isEmpty()) {
                    Intent intent = new Intent(getBaseContext(), LoginPage.class);
                    User user = new User(email_user, password_user);
                    for (int i = 0; i < ArrayUser.saveuserlist.size(); i++) {
                        if (user.getEmail_user().equalsIgnoreCase(ArrayUser.saveuserlist.get(i).getEmail_user())) {
                            Toast.makeText(getBaseContext(), "Email is already Registered!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    intent.putExtra("IDuser", user);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    ArrayUser.saveuserlist.add(user);
                    postData(user);
                }
            }
        });

        checkBox_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (CompoundButton compoundButton,boolean isChecked){
                if (isChecked) {
                    password_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password_register.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void postData(User temp) {
        String url = "http://192.168.1.4/Tourdes_webservice/createuser.php";
        RequestQueue myRequest = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(getBaseContext(), LoginPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("email", temp.getEmail_user());
                data.put("password", String.valueOf(temp.getPassword_user()));

                return data;
            }
        };

        myRequest.add(request);
    }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email_user = email_register.getText().toString().trim();
            String password_user = password_register.getText().toString().trim();
            if(!email_user.isEmpty() && !password_user.isEmpty()){
                signup_register.setEnabled(true);
            } else {
                signup_register.setEnabled(false);
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    }

