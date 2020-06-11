package com.example.lab08.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddUpdUsuario extends AppCompatActivity {
    private EditText nombreFLD;
    private EditText cedulaFLD;
    private EditText emailFLD;
    private EditText telefonoFLD;
    private boolean editable = true;
    private FloatingActionButton confirmBTN;
    Usuario actual;
    private ImageView imageFLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_upd_usuario);

        Bundle extras = getIntent().getExtras();
        /*nombreFLD = findViewById(R.id.NombreFLD);
        cedulaFLD = findViewById(R.id.CedulaFLD);
        emailFLD = findViewById(R.id.EmailFLD);
        telefonoFLD = findViewById(R.id.TelefonoFLD);
        confirmBTN = findViewById(R.id.ConfirmBTN);
        imageFLD = findViewById(R.id.capturedImage);
        if (extras != null) {
            editable = extras.getBoolean("editable");
            if (editable) {
                actual = (Usuario) getIntent().getSerializableExtra("user");
                nombreFLD.setText(actual.getNombre());
                //codFld.setEnabled(false);
                cedulaFLD.setText(actual.getNombre());
                emailFLD.setText(actual.getCorreo());
                telefonoFLD.setText(actual.getTelefono());
                //byte[] byteArray = actual.getFoto();
                //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                String path = Environment.getExternalStorageDirectory() + "imageBitmap" + ".png";
                Bitmap bmp = BitmapFactory.decodeFile(path);
                imageFLD.setImageBitmap(bmp);
                confirmBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       //editCurso();
                    }
                });
            } else {
                confirmBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // addCurso();
                    }
                });
            }
        }*/
    }
}