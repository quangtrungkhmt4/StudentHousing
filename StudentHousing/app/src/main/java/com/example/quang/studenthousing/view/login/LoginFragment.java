package com.example.quang.studenthousing.view.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quang.studenthousing.AccountActivity;
import com.example.quang.studenthousing.R;
import com.example.quang.studenthousing.object.User;
import com.example.quang.studenthousing.presenter.login.PresenterLogicLogin;
import com.example.quang.studenthousing.services.APIClient;
import com.example.quang.studenthousing.services.DataClient;
import com.example.quang.studenthousing.view.register.RegisterFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements View.OnClickListener, ViewLogin {

    public static final int RequestSignInCode = 7;

    private EditText edtUser;
    private EditText edtPass;
    private TextView tvRegister;
    private Button btnLogin;
    private ImageView imLoginFacebook;
    private ImageView imloginGoogle;
    private AccountActivity activity;

    private PresenterLogicLogin presenterLogicLogin;

    private FirebaseAuth firebaseAuth;
    // Google API Client object.
    public GoogleApiClient googleApiClient;

    private CallbackManager callbackManager;
    private LoginButton btnLoginFacebook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        findID(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FacebookSdk.sdkInitialize(activity);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();
        initViews();
    }

    private void findID(View view) {
        edtUser = view.findViewById(R.id.edtUserNameLogin);
        edtPass = view.findViewById(R.id.edtPasswordLogin);
        tvRegister = view. findViewById(R.id.tvRegisterUser);
        btnLogin = view.findViewById(R.id.btnLogin);
        imLoginFacebook = view.findViewById(R.id.imLoginFacebook);
        imloginGoogle = view.findViewById(R.id.imLoginGoogle);
        btnLoginFacebook = view.findViewById(R.id.btnLoginFacebook);
    }

    private void initViews() {
        activity = (AccountActivity) getActivity();
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        imloginGoogle.setOnClickListener(this);

        presenterLogicLogin = new PresenterLogicLogin(this);

        ////////////////////// login google//////////////////////////////

        // Getting Firebase Auth Instance into firebaseAuth object.
        firebaseAuth = FirebaseAuth.getInstance();

        // Creating and Configuring Google Sign In object.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        //////////////////////login facebook////////////////////////

        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile","email"));
        // If using in a fragment
        btnLoginFacebook.setFragment(this);

        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                request();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    /////////////////////////////login facebook////////////////////////////

    private void request() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
                , new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("JSON",response.getJSONObject().toString());

                        try {
                            String user = object.getString("id");
                            String name = object.getString("name");

                            DataClient dataClient = APIClient.getData();
                            Call<List<User>> callBack = dataClient.loginGoogleAndFacebook(user,user,name,"null");
                            callBack.enqueue(new Callback<List<User>>() {
                                @Override
                                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                    ArrayList<User> arrUser = (ArrayList<User>) response.body();
                                    if (arrUser.size() > 0){
                                        String result = arrUser.get(0).getIDUSER()+"-"+arrUser.get(0).getUSER()
                                                +"-"+arrUser.get(0).getPASSWORD()+"-"+arrUser.get(0).getNAME()
                                                +"-"+arrUser.get(0).getPHONE()+"-"+arrUser.get(0).getPERMISSION();
                                        saveUser(result);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<User>> call, Throwable t) {
                                    Snackbar snackbar = Snackbar
                                            .make(edtPass, R.string.fail, Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvRegisterUser:
                activity.loadFragment(new RegisterFragment());
                break;
            case R.id.btnLogin:
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();

                if(user.isEmpty() || pass.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtPass, R.string.insert_info, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                presenterLogicLogin.checkLogin(user,pass,activity);
                break;
            case R.id.imLoginGoogle:
                UserSignInMethod();
                break;
        }
    }


    ///////////////////////////////login google////////////////////////
    // Sign In function Starts From Here.
    public void UserSignInMethod(){

        // Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (googleSignInResult.isSuccess()){

                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                FirebaseUserAuth(googleSignInAccount);

            }

        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(activity, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {

                        if (AuthResultTask.isSuccessful()){

                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            String user = firebaseUser.getEmail().toString();
                            String pass = firebaseUser.getProviderId().toString();
                            String name = firebaseUser.getDisplayName().toString();
                            String phone;

                            if (firebaseUser.getPhoneNumber() == null){
                                phone = "null";
                            }else {
                                phone = firebaseUser.getPhoneNumber().toString();
                            }

                            DataClient dataClient = APIClient.getData();
                            Call<List<User>> callBack = dataClient.loginGoogleAndFacebook(user,pass,name,phone);
                            callBack.enqueue(new Callback<List<User>>() {
                                @Override
                                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                    ArrayList<User> arrUser = (ArrayList<User>) response.body();
                                    if (arrUser.size() > 0){
                                        String result = arrUser.get(0).getIDUSER()+"-"+arrUser.get(0).getUSER()
                                                +"-"+arrUser.get(0).getPASSWORD()+"-"+arrUser.get(0).getNAME()
                                                +"-"+arrUser.get(0).getPHONE()+"-"+arrUser.get(0).getPERMISSION();
                                        saveUser(result);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<User>> call, Throwable t) {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Snackbar snackbar = Snackbar
                                    .make(edtPass, R.string.fail, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }


    @Override
    public void loginSuccess(String result) {
        saveUser(result);
    }

    @Override
    public void loginFail() {
        Snackbar snackbar = Snackbar
                .make(edtPass, R.string.fail, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void loginError() {

    }


    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()){
            signOut();
        }
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void saveUser(String infoUser){
        SharedPreferences pre = activity.getSharedPreferences("studenthousing", MODE_PRIVATE);
        SharedPreferences.Editor edit=pre.edit();
        edit.putString("user",infoUser);
        edit.commit();

        String[] arr = infoUser.split("-");
        int permission = Integer.parseInt(arr[5]);
        if (permission == 0 || permission == 2){
            activity.switchActivity();
        }else if (permission == 1){
            activity.switchActivityManager();
        }

    }


}
