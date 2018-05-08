package com.example.pemil.www.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pemil.www.DataSource.UserDataSource;
import com.example.pemil.www.Models.User;
import com.example.pemil.www.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Login Activity
 */
public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String GOOGLE_TAG = "GoogleActivity";
    private static final String FACEBOOK_TAG = "FacebookLogin";
    private static final String LOGIN_TAG = "Login";
    private static final String FACEBOOK = "facebook";
    private static final String GOOGLE = "google";
    private static final String UID = "UID";

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private String signInMethod;
    private StorageReference mStorageRef;

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        getSupportActionBar().hide();


        emailEditText = findViewById(R.id.email);

        passwordEditText = findViewById(R.id.input_user_password);

        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        TextView signUp = findViewById(R.id.link_signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        //Check the Log if Login with Facebook fails again, copy the key hash and then
        // add it to the Facebook developer dashboard page
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.pemil.www",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onStart() {

        super.onStart();
        /* Check if user is signed in (non-null) and update UI accordingly.*/

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d("FIREBASE UID", currentUser.getUid());
        }

        updateUI(currentUser);
    }

    /**
     * Login with email and password
     * Checks if in the data base there is an user with the specified
     * credentials
     *
     * @param view
     */
    public void loginWithEmail(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Log.d(LOGIN_TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOGIN_TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOGIN_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            //TODO: add TextView for displaying error at login
                        }

                    }
                });
    }

    /**
     * Validates typed credentials
     *
     * @return ture if the credentials are entered correctly
     * false otherwise
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    /**
     * Login with Google API
     *
     * @param view
     */
    public void loginWithGoogle(View view) {
        signInMethod = GOOGLE;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }

    /**
     * Initialize Login with Facebook API
     *
     * @param view
     */
    public void loginWithFacebook(View view) {
        signInMethod = FACEBOOK;

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) view;
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FACEBOOK_TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(FACEBOOK_TAG, "facebook:onCancel");

                updateUI(null);

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FACEBOOK_TAG, "facebook:onError", error);

                updateUI(null);

            }
        });

    }

    /**
     * Login Implementation
     *
     * @param token
     */
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(FACEBOOK_TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FACEBOOK_TAG, "signInWithCredential:success");

                            final FirebaseUser Fuser = mAuth.getCurrentUser();

                            GraphRequest request = GraphRequest.newMeRequest(
                                    token,
                                    new GraphRequest.GraphJSONObjectCallback() {

                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.v("Main", response.toString());
                                            User user = null;
                                            UserDataSource userDataSource = new UserDataSource();

                                            if (Fuser != null) {
                                                user = getFacebookUser(Fuser.getUid(), object);
                                            }

                                            userDataSource.sendToDB(user);
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,email,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                            updateUI(Fuser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FACEBOOK_TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Gets intent for Google login screen
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (signInMethod.equals(FACEBOOK)) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (signInMethod.equals(GOOGLE)) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);

                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(GOOGLE_TAG, "Google sign in failed", e);
                }
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(GOOGLE_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(GOOGLE_TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            UserDataSource userDataSource = new UserDataSource();

                            User googleUser = null;
                            if (user != null) {
                                googleUser = getGoogleUser(user.getUid());
                            }
                            if (googleUser != null) {
                                Log.d("SENDING TO DATABASE", googleUser.toString());
                                userDataSource.sendToDB(googleUser);
                            } else {
                                Log.d("GOOGLE LOGIN", "user is null");
                            }

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(GOOGLE_TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });

    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            intent.putExtra(UID, user.getUid());
            startActivity(intent);
        }
    }


    public User getGoogleUser(String id) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        User user = null;
        if (account != null) {
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            Uri personPhoto = account.getPhotoUrl();


            //TODO - send personPhoto to storage
            if (personPhoto != null) {
                addToStorage(id, "http://lh6.ggpht.com" + personPhoto.getPath() + "?width=500&height=500");
            }

            user = new User(personGivenName,
                    personFamilyName,
                    null,
                    null,
                    null,
                    personEmail,
                    id,
                    null);
        }
        if (user != null) {
            Log.d("GOOGLE PERSON", user.toString());
        }
        return user;
    }

    public User getFacebookUser(String id, JSONObject object) {
        User user = null;
        Log.d("FACEBOOK JSON", String.valueOf(object));

        String userId;
        String firstName = null;
        String lastName = null;
        String email = null;
        try {
            userId = object.getString("id");
//            URL profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
//            Uri uri = Uri.parse(profilePicture.toURI().toString());

            addToStorage(id, "https://graph.facebook.com/" + userId + "/picture?width=500&height=500");

            if (object.has("first_name"))
                firstName = object.getString("first_name");
            if (object.has("last_name"))
                lastName = object.getString("last_name");
            if (object.has("email"))
                email = object.getString("email");

            user = new User(firstName,
                    lastName,
                    null,
                    null,
                    null,
                    email,
                    id,
                    null);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    private void addToStorage(String id, String photoUri) {
        Log.d("STORAGE URI", photoUri);
        Log.d("STORAGE ID", id);

        Uri uri = Uri.parse(photoUri);

        StorageReference riversRef = mStorageRef.child("Users/" + id);

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("STORAGE REFERENCE", "onSucces:succes: " + downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

}
