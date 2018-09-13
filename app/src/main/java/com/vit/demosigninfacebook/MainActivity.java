package com.vit.demosigninfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LoginButton mButton;

    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFacebookSignIn();

        findViewById(R.id.button_login_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                        Arrays.asList("public_profile", "email", "user_gender"));
            }
        });


        /*mButton = findViewById(R.id.button_login);
        mButton.setReadPermissions("email");
        mButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mText.setText("User ID: " + loginResult.getAccessToken().getUserId() + "\n" +
                        "Auth Token: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                mText.setText("Login canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                mText.setText("Login failed.");
            }
        });*/


        /**
         * get Key hash facebook api
         */
        /*PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.you.name", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/


    }

    private void initFacebookSignIn() {
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookProfile(loginResult);

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: Login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: Login failed");
            }
        });
    }

    private void getFacebookProfile(LoginResult loginResult) {
        Profile profile = Profile.getCurrentProfile();

        String id = loginResult.getAccessToken().getUserId();
        String token = loginResult.getAccessToken().getToken();
        String firstName = profile.getFirstName();
        String lastName = profile.getLastName();
        String avatar = profile.getProfilePictureUri(300, 300).toString();

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        Log.v("LoginActivity Response ", response.toString());

                        try {
                            String email = object.getString("email");
                            String gender = object.getString("gender");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
