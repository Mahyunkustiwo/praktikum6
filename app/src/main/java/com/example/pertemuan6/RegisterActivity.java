package com.example.pertemuan6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText Email,Password,Name;
    Button Register;
    String NameHolder,EmailHolder,PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";
    private String SQLiteDataBaseQueryHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register = (Button)findViewById(R.id.buttonRegister);
        Email = (EditText)findViewById(R.id.editEmail);
        Password = (EditText)findViewById(R.id.editPassword);
        Name = (EditText)findViewById(R.id.editName);
        sqLiteHelper = new SQLiteHelper(this);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Method Create Db Sqlite
                SQLiteDataBaseBuild();

                // Method Db Sqlite
                SQLiteTableBuild();

                // Method Cek Status Email
                CheckEditTextStatus();

                // Method cek email yang sudah ada di db Sqlite
                CheckingEmailAlreadyExistsOrNot();

                // Method Proses insert email password
                EmptyEditTextAfterDataInsert();


            }

        });

    
    }

    private void SQLiteDataBaseBuild() {
        sqLiteDatabaseObj =
                openOrCreateDatabase(SQLiteHelper.DATABASE_NAME,
                        Context.MODE_PRIVATE, null);

    }

    private void SQLiteTableBuild() {
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " +
                        SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                SQLiteHelper.Table_Column_1_Name + " VARCHAR, " +
                        SQLiteHelper.Table_Column_2_Email + " VARCHAR, " +
                        SQLiteHelper.Table_Column_3_Password + " VARCHAR);");

    }

    public void CheckEditTextStatus(){
        NameHolder = Name.getText().toString() ;
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        if (TextUtils.isEmpty(NameHolder) ||
                TextUtils.isEmpty(EmailHolder) ||
                TextUtils.isEmpty(PasswordHolder)){
            EditTextEmptyHolder = false ;
        }
        else {
            EditTextEmptyHolder = true ;
        }
    }



    private void InsertDataIntoSQLiteDatabase() {
        if (EditTextEmptyHolder == true) {
            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO ; "+SQLiteHelper.TABLE_NAME+" (name, email, password)VALUES('"+NameHolder+"', '"+EmailHolder+"', '"+PasswordHolder+"');";

            // Executing query.
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);
            // Closing SQLite database object.
            sqLiteDatabaseObj.close();
            // Printing toast message after done inserting.
            AlertDialog.Builder builder = new
                    AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("User Registered Successfully").setNegativeButton("OK", null).create().show();
        } else {
            // Messages Pop Up
            AlertDialog.Builder builder = new
                    AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Please Fill All The Required Fields").setNegativeButton("OK", null).create().show();

        }
    }

    private void EmptyEditTextAfterDataInsert() {
        Name.getText().clear();
        Email.getText().clear();
        Password.getText().clear();

    }


    private void CheckingEmailAlreadyExistsOrNot() {

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();
        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME,
                null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new
                        String[]{EmailHolder}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                // If Email is already exists then Result variable
                //value set as Email Found.
                F_Result = "Email Found";
                // Closing cursor.
                cursor.close();
            }
        }
        // Calling method to check final result and insert data into
        //SQLite database.
        CheckFinalResult();
    }

    private void CheckFinalResult() {
        if(F_Result.equalsIgnoreCase("Email Found"))
        {
            // If email is exists then toast msg will display.
            Toast.makeText(RegisterActivity.this,"Email Already Exists", Toast.LENGTH_LONG).show();
        }
        else {
            // If email already dose n't exists then user
            //registration details will entered to SQLite database.
                    InsertDataIntoSQLiteDatabase();
        }
        F_Result = "Not_Found" ;
    }




    public void Beck(View view) {
        Intent intent = new
                Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }
    }
