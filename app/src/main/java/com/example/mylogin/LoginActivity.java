package com.example.mylogin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylogin.database.AppDatabase;
import com.example.mylogin.database.UserDao;
import com.example.mylogin.model.HondanaUser;
import com.example.mylogin.repository.UserRepository;
import com.example.mylogin.viewmodel.ListaUsersViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.reflect.Method;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUser, txtPass;
    private Button btnLogin, btnRegister, btnGoogle;
    public static final String EXTRA_MESSAGE = "com.example.mylogin.MESSAGE";
    private AppDatabase db;
    private TextView btnForgot;
    private ProgressBar progressBar;
    private ConstraintLayout cl;
    private FirebaseAnalytics mFirebaseAnalytics;

    private String TAG = "GoogleSignIn";
    private SignInButton SInButton;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private GoogleSignInClient mGoogleSignClient;
    private String d1, d2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = (EditText) findViewById(R.id.editUsername);
        txtPass = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnGoRegister);
        btnForgot = (TextView) findViewById(R.id.txtForgot);
        btnGoogle = (Button) findViewById(R.id.btnLoginGoogle);
        cl = (ConstraintLayout) findViewById(R.id.ContainerL);

        // Set the dimensions of the sign-in button.
        SInButton = (SignInButton) findViewById(R.id.sign_in_button);
        SInButton.setSize(SignInButton.SIZE_STANDARD);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.METHOD, "Login");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,params);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "View Login");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //Configurar Google SignIn
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //.requestIdToken(getString(R.string.default_web_client_id))

        mGoogleSignClient = GoogleSignIn.getClient(this, gso);

        //Iniciar con correo
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUser.length() == 0 || txtPass.length() == 0){
                    Toast.makeText(LoginActivity.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                } else {

                    d1 = txtUser.getText().toString();
                    d2 = txtPass.getText().toString();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(d1, d2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mFirebaseAnalytics = FirebaseAnalytics.getInstance(LoginActivity.this);
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "LoginEmail");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);


                                Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                loginIntent.putExtra("Username", d1);
                                LoginActivity.this.startActivity(loginIntent);

                            }else {
                                String mensaje = task.getException().getMessage().toString();
                                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //Enviar correo de recuperacion
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar = new ProgressBar(LoginActivity.this, null, android.R.attr.progressBarStyleHorizontal);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                progressBar.setLayoutParams(lp);
                progressBar.setIndeterminate(true);
                cl.addView(progressBar);
                progressBar.setVisibility(View.VISIBLE);

                String email = txtUser.getText().toString();
                if (!email.isEmpty()){

                    FirebaseAuth.getInstance().setLanguageCode("es");
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mFirebaseAnalytics = FirebaseAnalytics.getInstance(LoginActivity.this);
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ResetPasswor");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                                Toast.makeText(LoginActivity.this, "Correo enviado correctamente", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else {
                                String mensaje = task.getException().getMessage().toString();
                                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                }
                else{

                    Toast.makeText(LoginActivity.this, "Ingrese el correo de la cuenta", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Ir a registrarse
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        //Boton usar cuenta Google
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignClient.getSignInIntent();
                //launcher.launch(signInIntent);
                startActivityForResult(signInIntent, 1234);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.d(TAG, "signInWithCredential:success" + account.getEmail());

                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if(currentUser != null){
                                        mFirebaseAnalytics = FirebaseAnalytics.getInstance(LoginActivity.this);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "RegisterGoogle");
                                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);


                                        String usr = mAuth.getCurrentUser().getEmail();
                                        Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                        loginIntent.putExtra("Username", usr);
                                        LoginActivity.this.startActivity(loginIntent);
                                    }

                                }   else{
                                    Log.w(TAG, "signInWithCredential:failuer", task.getException());
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            } catch (ApiException e){
                Log.w(TAG, "Google sign in failed", e);
                e.printStackTrace();
            }
        }
    }
}