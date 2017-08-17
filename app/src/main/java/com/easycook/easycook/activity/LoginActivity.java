package com.easycook.easycook.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.easycook.easycook.R;
import com.easycook.easycook.model.Usuario;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class LoginActivity extends AppCompatActivity {

    private Collection<String> permissions = new ArrayList<>();
    private String TAG = "LoginActivity";
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        permissions.add("email");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void logInFacebook(View view) {
        if (ParseUser.getCurrentUser() == null) {
            ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (user == null) {
                        Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        ParseUser.logOut();
                    } else if (user.isNew()) {
                        Log.d("MyApp", "User signed up and logged in through Facebook!");
                        getFacebookUserDetails(true, user, err);
                    } else {
                        Log.d("MyApp", "User logged in through Facebook!");
                        getFacebookUserDetails(false, user, err);
                    }
                }
            });
        } else {
            ParseUser.logOut();
        }
    }

    public void getFacebookUserDetails(final boolean firstTime, final ParseUser user, final ParseException error) {

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            usuario.setSexo(object.getString("gender"));
                            usuario.setEmail(object.getString("email"));
                            usuario.setPrimeiroNome(object.getString("first_name"));
                            usuario.setSegundoNome(object.getString("last_name"));

                            /*JSONObject picObject = object.getJSONObject("picture");
                            JSONObject dataObject = picObject.getJSONObject("data");
                            userPhoto = dataObject.getString("url");*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //User logged in with facebook for the first time
                        if (firstTime) {
                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, permissions, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d(TAG, "Woohoo, user logged in with Facebook!");

                                            user.setEmail(usuario.getEmail());
                                            user.put("primeiroNome", usuario.getPrimeiroNome());
                                            user.put("segundoNome", usuario.getSegundoNome());
                                            user.put("NomeCompleto", usuario.getNomeCompleto());

                                            user.signUpInBackground(new SignUpCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        abrirTelaPrincipal();
                                                    } else {
                                                        Log.e(TAG, e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                user.setEmail(usuario.getEmail());
                                user.put("primeiroNome", usuario.getPrimeiroNome());
                                user.put("segundoNome", usuario.getSegundoNome());
                                user.put("NomeCompleto", usuario.getNomeCompleto());
                                user.saveInBackground();

                                abrirTelaPrincipal();
                            }
                        } else {

                            Log.d(TAG, "User logged in through Facebook!");

                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, permissions, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d(TAG, "Woohoo, user logged in with Facebook!");

                                            user.setEmail(usuario.getEmail());
                                            user.put("primeiroNome", usuario.getPrimeiroNome());
                                            user.put("segundoNome", usuario.getSegundoNome());
                                            user.put("NomeCompleto", usuario.getNomeCompleto());
                                            user.saveInBackground();

                                            abrirTelaPrincipal();
                                        }
                                    }
                                });
                            } else {

                                user.setEmail(usuario.getEmail());
                                user.put("primeiroNome", usuario.getPrimeiroNome());
                                user.put("segundoNome", usuario.getSegundoNome());
                                user.put("NomeCompleto", usuario.getNomeCompleto());
                                user.saveInBackground();

                                abrirTelaPrincipal();
                            }
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "gender,name,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
