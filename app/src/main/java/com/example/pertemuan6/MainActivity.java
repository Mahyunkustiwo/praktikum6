package com.example.pertemuan6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button LogInButton;
    EditText Email, Password;
    TextView RegisterButton;
    String EmailHolder, PasswordHolder;
    boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String TemoPassword = "NOT_FOUND" ;
    public static final String UserEmail = "";
    private String TempPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogInButton = (Button) findViewById(R.id.buttonLogin);
        RegisterButton = (TextView)findViewById(R.id.buttonRegister);
        Email = (EditText) findViewById(R.id.editEmail);
        Password = (EditText) findViewById(R.id.editPassword);

        sqLiteHelper = new SQLiteHelper(this);

        //methot Onclick login
        LogInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View view) {
                //method ceek email password
            CheckEditTextStatus();
            //method login akun
            LoginFunction();
        }
        });

        //method onclick register akun
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    //Method login
    public void LoginFunction() {
        if (EditTextEmptyHolder) {
            //sqlite database permision.
            sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
                                //email query
                                cursor = 
                    sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME,null, "" + 
                    SQLiteHelper.Table_Column_2_Email + "=?" , new
                    String [] {EmailHolder}, null, null, null);
            
                        while ( cursor.moveToNext()) {
                            if (cursor.isFirst()){
                                cursor.moveToFirst();
                                
                                //password email
                                TempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3_Password));
                                cursor.close();
                            }
                        }
                        
                        //methot cek email passW
                    CheckFinalResult();
        }

        else {

            //pesan pop up
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("username atau password masih kosong").setNegativeButton("OK",null).create().show();

        }
    }

    private void CheckEditTextStatus() {
        // Getting value from All EditText and storing into string variables.
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        // Checking EditText is empty or no using TextUtils.
        if( TextUtils.isEmpty(EmailHolder) ||
                TextUtils.isEmpty(PasswordHolder)){
            EditTextEmptyHolder = false ;
        }
        else {
            EditTextEmptyHolder = true ;
        }
    }


    // Cek Email Password Yang Sudah Ada Di DataBase SQLite
    private void CheckFinalResult() {
        if(TempPassword.equalsIgnoreCase(PasswordHolder))
        {
            Intent intent = new Intent(MainActivity.this,
                    DashboardActivity.class);
            intent.putExtra(UserEmail, EmailHolder);
            startActivity(intent);
        }else {
            //Messages Pop Up
            AlertDialog.Builder builder = new
                    AlertDialog.Builder(MainActivity.this);
            builder.setMessage("UserName atau Password salah, COBA KEMBALI !!!").setNegativeButton("OK", null).create().show();

        }
        TempPassword= "NOOT_FOUND";
    }
}