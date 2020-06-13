package com.example.lab08.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddUpdUsuario extends AppCompatActivity {
    private EditText nombreFLD;
    private EditText cedulaFLD;
    private EditText emailFLD;
    private EditText telefonoFLD;
    private boolean editable = true;
    private FloatingActionButton confirmBTN;
    Usuario actual;
    private ImageView imageFLD;
    private Boolean photoChanged ;
    private ImageButton fotoBTN;
    private static final int Image_Capture_Code = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_usuario);

        Bundle extras = getIntent().getExtras();
        nombreFLD = findViewById(R.id.NombreFLD);
        cedulaFLD = findViewById(R.id.CedulaFLD);
        emailFLD = findViewById(R.id.EmailFLD);
        telefonoFLD = findViewById(R.id.TelefonoFLD);
        confirmBTN = findViewById(R.id.ConfirmBTN);
        imageFLD = findViewById(R.id.capturedImage);
        fotoBTN = findViewById(R.id.fotoBTN);

        fotoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
            }
        });
        photoChanged = false;
        if (extras != null) {
            editable = extras.getBoolean("editable");
            if (editable) {
                getSupportActionBar().setTitle(getString(R.string.actualizarcontacto));
                actual = (Usuario) getIntent().getSerializableExtra("user");
                nombreFLD.setText(actual.getNombre());
                cedulaFLD.setText(actual.getCedula());
                cedulaFLD.setEnabled(false);
                emailFLD.setText(actual.getCorreo());
                telefonoFLD.setText(actual.getTelefono());
                String path = Environment.getExternalStorageDirectory() + "/imageBitmap" + ".png";
                Bitmap bmp = BitmapFactory.decodeFile(path);
                imageFLD.setImageBitmap(bmp);
                confirmBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       editCurso();
                    }
                });
            } else {
                getSupportActionBar().setTitle(getString(R.string.agregarcontacto));
                confirmBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // addCurso();
                    }
                });
            }
        }
    }
    public void editCurso(){
        Intent intent = new Intent(this, MainActivity.class);

        actual.setCedula(cedulaFLD.getText().toString());
        actual.setNombre(nombreFLD.getText().toString());
        actual.setCorreo(emailFLD.getText().toString());
        actual.setTelefono(telefonoFLD.getText().toString());
        intent.putExtra("editado", actual);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                imageFLD.setImageBitmap(bmp);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bmp.recycle();
                if (Build.VERSION.SDK_INT >= 23)
                {
                    if (checkPermission())
                    {
                        writeOnExternalStorage(byteArray);
                    } else {
                        requestPermission(); // Code for permission
                        writeOnExternalStorage(byteArray);
                    }
                }
                else
                {

                    writeOnExternalStorage(byteArray);
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void writeOnExternalStorage(byte[] aux){
        File file = new File(Environment.getExternalStorageDirectory() + "/imageBitmap" + ".png");
        try (FileOutputStream fOut = new FileOutputStream(file)) {
            byte[] byteArray = aux;
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            if (!(bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut))) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddUpdUsuario.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddUpdUsuario.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(AddUpdUsuario.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddUpdUsuario.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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