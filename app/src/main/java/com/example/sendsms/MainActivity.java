package com.example.sendsms;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 1;
    private EditText phone;
    private EditText editTextMessage;
    private Button select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);


        editTextMessage= findViewById(R.id.text);
        phone = findViewById(R.id.nomor);
        select = findViewById(R.id.getkontak);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new  Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(in, RESULT_PICK_CONTACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            switch (requestCode){
                case RESULT_PICK_CONTACT:
                    contactPicked (data);
                    break;
            }
        }
        else {
            Toast.makeText(this, "Gagal Memilih Kontak", Toast.LENGTH_SHORT).show();
        }
    }

    private void contactPicked(Intent data)
    {
        Cursor cursor = null;

        try {
            String phoneNo = null;
            Uri uri = data.getData();

            cursor = getContentResolver().query(uri,  null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);

            phone.setText (phoneNo);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendSMS(View view){
        String message = editTextMessage.getText().toString();
        String number = phone.getText().toString();

        SmsManager mySmsManager = SmsManager.getDefault();
        mySmsManager.sendTextMessage(number, null, message, null, null);
    }

    public void Pindah(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }
}