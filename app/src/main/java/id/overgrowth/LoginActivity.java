package id.overgrowth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import id.overgrowth.utility.InternetCheck;
import id.overgrowth.utility.SessionManager;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 1;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private SignInButton buttonSignIn;
    SessionManager session;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inisialisasi();
        buttonSignIn.setOnClickListener(this);
    }

    private void inisialisasi(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        buttonSignIn = (SignInButton) findViewById(R.id.button_sign_in_googleplus);
        buttonSignIn.setSize(SignInButton.SIZE_STANDARD);
        buttonSignIn.setScopes(gso.getScopeArray());
        session = new SessionManager(getBaseContext());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_in_googleplus:
                if(InternetCheck.isNetworkConnected(LoginActivity.this)){
                    if (InternetCheck.isNetworkAvailable(LoginActivity.this)){
                        signIn();
                    } else {
                        Toast.makeText(LoginActivity.this, "Internet tidak bisa diakses!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Mohon nyalakan paket data atau koneksi WiFi!", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount akun = result.getSignInAccount();;
            String id = akun.getId();
            String nama = akun.getDisplayName();
            String email = akun.getEmail();

            Uri urlFoto = akun.getPhotoUrl();
            String urlAvatar = String.valueOf(urlFoto);

            Toast.makeText(getApplicationContext(),"ID "+id, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Welcome "+nama, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"UrlFoto "+urlAvatar, Toast.LENGTH_SHORT).show();

            session.createLoginSession(id, nama, email, urlAvatar);
            
            if(session.isLoggedIn()) {
                finishLogin();
            }
        }
    }
    private void finishLogin() {
        SharedPreferences preferences = getSharedPreferences("login_preferences", MODE_PRIVATE);
        preferences.edit().putBoolean("login_complete",true).apply();
        intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
