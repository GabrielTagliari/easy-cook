package com.easycook.easycook.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.easycook.easycook.R;
import com.easycook.easycook.model.Usuario;
import com.easycook.easycook.util.Permission;
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

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";

    private Usuario usuario = new Usuario();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage(getString(R.string.progress_carregando));

        if (usuarioLogado()) {
            abrirTelaPrincipal();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void logInFacebook(View view) {
        if (!usuarioLogado()) {

            progressDialog.show();

            ParseFacebookUtils.logInWithReadPermissionsInBackground(this,
                    Permission.getPermissions(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {
                    if (err == null) {
                        if (user == null) {
                            Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                            ParseUser.logOut();
                        } else if (user.isNew()) {
                            Log.d(TAG, "User signed up and logged in through Facebook!");
                            getFacebookUserDetails(true, user);
                        } else {
                            Log.d(TAG, "User logged in through Facebook!");
                            getFacebookUserDetails(false, user);
                        }
                    } else {
                        err.printStackTrace();
                    }
                }
            });
        } else {
            ParseUser.logOut();
        }
    }

    private boolean usuarioLogado() {
        return ParseUser.getCurrentUser() != null;
    }

    public void getFacebookUserDetails(final boolean firstTime, final ParseUser user) {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            usuario.setSexo(object.getString("gender"));
                            usuario.setEmail(object.getString("email"));
                            usuario.setPrimeiroNome(object.getString("first_name"));
                            usuario.setSegundoNome(object.getString("last_name"));

                            populaUsuario(user);

                            /*JSONObject picObject = object.getJSONObject("picture");
                            JSONObject dataObject = picObject.getJSONObject("data");
                            userPhoto = dataObject.getString("url");*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (firstTime) {
                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, Permission.getPermissions(), new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d(TAG, "Woohoo, user logged in with Facebook!");

                                            user.signUpInBackground(new SignUpCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        progressDialog.dismiss();
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
                                user.saveInBackground();

                                progressDialog.dismiss();
                                abrirTelaPrincipal();
                            }
                        } else {

                            Log.d(TAG, "User logged in through Facebook!");

                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, Permission.getPermissions(), new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d(TAG, "Woohoo, user logged in with Facebook!");

                                            user.saveInBackground();

                                            progressDialog.dismiss();
                                            abrirTelaPrincipal();
                                        }
                                    }
                                });
                            } else {
                                user.saveInBackground();

                                progressDialog.dismiss();
                                abrirTelaPrincipal();
                            }
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "gender,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void populaUsuario(ParseUser user) {
        user.setEmail(usuario.getEmail());
        user.put("primeiroNome", usuario.getPrimeiroNome());
        user.put("segundoNome", usuario.getSegundoNome());
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginEmail(View view) {
    }

    public void abrirTelaCadastro(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
