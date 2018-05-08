package com.example.pemil.www.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pemil.www.DataSource.UserDataSource;
import com.example.pemil.www.Models.User;
import com.example.pemil.www.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Baciu Ionut on 4/28/2018.
 */

enum PasswordState {
    ON,
    OFF
}

public class SignUpActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText id;
    private EditText password;
    private EditText password_confirm;
    private EditText telephone;
    private EditText mail;
    private EditText country;

    private Button date_button;
    private Button signUp_button;

    private ImageView privacy1;
    private ImageView privacy2;
    private ImageView profileImage;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    private DatabaseReference table;

    private UserDataSource userDataSource;

    private PasswordState state1 = PasswordState.OFF;
    private PasswordState state2 = PasswordState.OFF;

    private int day;
    private int year;
    private int month;

    static final int DIALOG_ID = 0;

    private boolean clicked = false;

    private int random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        id = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.password);
        password_confirm = (EditText) findViewById(R.id.password_confirm);
        telephone = (EditText) findViewById(R.id.phone);
        mail = (EditText) findViewById(R.id.mail);
        country = (EditText) findViewById(R.id.country);

        date_button = (Button) findViewById(R.id.date);
        signUp_button = (Button) findViewById(R.id.confirm_form);

        privacy1 = (ImageView) findViewById(R.id.visibility_1);
        privacy2 = (ImageView) findViewById(R.id.visibility_2);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // get Firebase instance
        auth = FirebaseAuth.getInstance();
        dataBase = FirebaseDatabase.getInstance();
        table = dataBase.getReference("Users");

        userDataSource = new UserDataSource();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        3);
            }
        });

        final Calendar CALENDAR = Calendar.getInstance();
        year = CALENDAR.get(Calendar.YEAR);
        month = CALENDAR.get(Calendar.MONTH);
        day = CALENDAR.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonPressed();

        setConditionForFirstCharToUpperCase();
        setToNumbersOnly();

        //Random number for suggested id
        random = new Random().nextInt(999);

        //Add id when surname is written
        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str;
                int min = getMinLengthForId(name.getText().toString());
                if (!name.getText().toString().equals("") && !surname.getText().toString().equals("")) {
                    str = name.getText().toString().substring(0, min) +
                            surname.getText().toString().substring(0, 1) +
                            Integer.toString(random);
                    id.setText(str);
                }
            }
        });

        //SignUp registration button
        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFieldsAreCompletedCorrectly()) {
                    progressBar.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    /*
                                     * If sign in fails, display a message to the user. If sign in succeeds
                                     * the auth state listener will be notified and logic to handle the
                                     * signed in user can be handled in the listener.
                                     */
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        String date = Integer.toString(month) + "\\" +
                                                Integer.toString(day) + "\\" +
                                                Integer.toString(year);

                                        BitmapDrawable drawable = (BitmapDrawable) profileImage.getDrawable();
                                        //TODO - send drawable to storage
                                        //create user
                                        User user = new User(name.getText().toString(),
                                                surname.getText().toString(),
                                                country.getText().toString(),
                                                date,
                                                Long.parseLong(telephone.getText().toString()),
                                                mail.getText().toString(),
                                                id.getText().toString(),
                                                password.getText().toString());

                                        FirebaseUser currentUser = auth.getCurrentUser();
                                        if (currentUser != null) {
                                            user.setId(currentUser.getUid());
                                        }
                                        userDataSource.sendToDB(user);
                                        updateUI(currentUser);

                                    }
                                }
                            });

                }
            }
        });

        passwordControl();
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            intent.putExtra("UID", currentUser.getUid());
            startActivity(intent);
        }
    }

    protected void onResume() {

        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Sets visibility of the password
     */
    private void passwordControl() {
        privacy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state1.equals(PasswordState.OFF)) {
                    state1 = PasswordState.ON;
                    privacy1.setImageResource(R.drawable.visibility);
                    password.setTransformationMethod(null);
                } else {
                    state1 = PasswordState.OFF;
                    privacy1.setImageResource(R.drawable.visibility_off);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        privacy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state2.equals(PasswordState.OFF)) {
                    state2 = PasswordState.ON;
                    privacy2.setImageResource(R.drawable.visibility);
                    password_confirm.setTransformationMethod(null);
                } else {
                    state2 = PasswordState.OFF;
                    privacy2.setImageResource(R.drawable.visibility_off);
                    password_confirm.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

    }


    /**
     * Dialog for date of birth
     */
    public void showDialogOnButtonPressed() {
        date_button = (Button) findViewById(R.id.date);

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = true;
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, date_picker_listener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener date_picker_listener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker,
                                      int thisYear,
                                      int thisMonth,
                                      int thisDay) {
                    year = thisYear;
                    month = thisMonth;
                    day = thisDay;
                    String month_to_string = convertToMonth(month);
                    date_button.setText(day + " - " + month_to_string + " - " + year);
                }
            };

    /**
     * Integer to month convert
     */
    private String convertToMonth(int id) {
        Context context = getApplicationContext();
        String[] months = context.getResources().getStringArray(R.array.months);
        return months[id].toUpperCase().substring(0, 3);
    }

    /**
     * Min length for id from name
     */
    private int getMinLengthForId(String str) {
        return str.length() < 5 ? str.length() : 5;
    }

    /**
     * Sets the keyboard numerical
     */
    private void setToNumbersOnly() {
        telephone.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * Caps on condition
     */
    private void setConditionForFirstCharToUpperCase() {
        name.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        surname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        country.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    /**
     * Checks if all the fields were completed
     * correctly
     *
     * @return
     */
    private boolean allFieldsAreCompletedCorrectly() {
        boolean to_return = true;

        if (name.getText().toString().isEmpty()) {
            name.setError(getString(R.string.needed));
            to_return = false;
        }
        if (surname.getText().toString().isEmpty()) {
            surname.setError(getString(R.string.needed));
            to_return = false;
        }
        if (!clicked) {
            Toast.makeText(getApplicationContext(), R.string.birthday_needed, Toast.LENGTH_SHORT).show();
            to_return = false;
        }
        if (country.getText().toString().isEmpty()) {
            country.setError(getString(R.string.needed));
            to_return = false;
        }
        if (telephone.getText().toString().isEmpty()) {
            telephone.setError(getString(R.string.needed));
            to_return = false;
        }
        //TODO add for unregistered email for validation
        if (mail.getText().toString().isEmpty()) {
            mail.setError(getString(R.string.needed));
            to_return = false;
        }
        //TODO check if user ID is valid
        if (id.getText().toString().isEmpty()) {
            id.setError(getString(R.string.needed));
            to_return = false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError(getString(R.string.needed));
            to_return = false;
        }
        if (password_confirm.getText().toString().isEmpty()) {
            password_confirm.setError(getString(R.string.needed));
            to_return = false;
        }
        if (!password.getText().toString().equals(password_confirm.getText().toString())) {
            password_confirm.setError(getString(R.string.passwords_must_match));
            password.setError(getString(R.string.passwords_must_match));
            to_return = false;
        }
        if (password.getText().toString().length() < 7) {
            password.setError(getString(R.string.error_invalid_password));
            to_return = false;
        }
        if (!password.getText().toString().matches("^.*[^a-zA-Z0-9].*$")) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.password_type),
                    Toast.LENGTH_SHORT).show();
            to_return = false;
        }
        if (!to_return) {
            Toast.makeText(getApplicationContext(), getString(R.string.have_to_complete), Toast.LENGTH_SHORT).show();
        }
        return to_return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                profileImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
