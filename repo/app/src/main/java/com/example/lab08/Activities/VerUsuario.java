package com.example.lab08.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VerUsuario extends AppCompatActivity {
    private EditText nombreFLD;
    private EditText cedulaFLD;
    private EditText emailFLD;
    private EditText telefonoFLD;
    private boolean editable = true;
    //private FloatingActionButton confirmBTN;
    Usuario actual;
    private ImageView imageFLD;
    private Boolean photoChanged ;
    private ImageButton salirBTN;
    private ImageButton text;
    private ImageButton call;
    private static final int PERMISSION_REQUEST_CODE = 1;
private String msjToSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);
        getSupportActionBar().setTitle(getString(R.string.titlecontacto));

        nombreFLD = findViewById(R.id.NombreFLD);
        cedulaFLD = findViewById(R.id.CedulaFLD);
        emailFLD = findViewById(R.id.EmailFLD);
        telefonoFLD = findViewById(R.id.TelefonoFLD);
       // confirmBTN = findViewById(R.id.ConfirmBTN);
        imageFLD = findViewById(R.id.capturedImage);

        text = findViewById(R.id.text);
        call = findViewById(R.id.call);

        salirBTN = findViewById(R.id.salirBTN);
        salirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salir();
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editable = extras.getBoolean("ver");
            if (editable) {
                actual = (Usuario) getIntent().getSerializableExtra("user");
                nombreFLD.setText(actual.getNombre());
                cedulaFLD.setText(actual.getCedula());
                emailFLD.setText(actual.getCorreo());
                telefonoFLD.setText(actual.getTelefono());
                String path = Environment.getExternalStorageDirectory() + "/imageBitmap" + ".png";
                Bitmap bmp = BitmapFactory.decodeFile(path);
                imageFLD.setImageBitmap(bmp);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callPhoneNumber2();
                    }
                });
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDiolog();
                    }
                });
            }
        }

    }
    public void salir(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void callPhoneNumber2() {
        try {
            if(Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(VerUsuario.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telefonoFLD.getText().toString()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telefonoFLD.getText().toString()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void openDiolog(){
        AlertDialog.Builder mBuider = new AlertDialog.Builder(VerUsuario.this);
        View view = getLayoutInflater().inflate(R.layout.sendmessege,null);
        mBuider.setView(view);
        final AlertDialog dialog = mBuider.create();
        final EditText Dmessege = (EditText) view.findViewById(R.id.msjText);
        TextView NombreContacto = (TextView)view.findViewById(R.id.NombreContacto);
        NombreContacto.setText("Mensaje para: "+actual.getNombre());
        ImageButton DsendTxtBTN = (ImageButton) view.findViewById(R.id.sendTxtBTN);
        DsendTxtBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                msjToSend = Dmessege.getText().toString();
                sendMSJ2();
                dialog.hide();

            }
        });

        dialog.show();
    }
    public void sendMSJ(String msg){
        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(telefonoFLD.getText().toString(),null,msg,null,null);
            Toast.makeText(VerUsuario.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(VerUsuario.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

  public void sendMSJ2(){
      Intent intent = new Intent(this, AddUpdUsuario.class);
      intent.putExtra("editable", true);
      if (Build.VERSION.SDK_INT >= 23) {
          if (checkPermission2()) {
              sendMSJ(msjToSend);
          } else {
              requestPermission2(); // Code for permission
              sendMSJ(msjToSend);
          }
      }
      else {
          sendMSJ(msjToSend);
      }
  }
    private void requestPermission2() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(VerUsuario.this, Manifest.permission.SEND_SMS)) {
            Toast.makeText(VerUsuario.this, "Porfavor dar permisos de envio de mensajes.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(VerUsuario.this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission2() {
        int result = ContextCompat.checkSelfPermission(VerUsuario.this, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

      @Override
      public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
          switch (requestCode) {
              case PERMISSION_REQUEST_CODE:
                  if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                      Log.e("value", "Permission Granted, Now you can use local drive .");
                  } else {
                      Log.e("value", "Permission Denied, You cannot use local drive .");
                  }
                  break;
          }
      }


}