package com.example.lab08.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class VerUsuario extends AppCompatActivity {
    private EditText nombreFLD;
    private EditText cedulaFLD;
    private EditText emailFLD;
    private EditText telefonoFLD;
    private boolean editable = true;
    private FloatingActionButton confirmBTN;
    Usuario actual;
    private ImageView imageFLD;
    private Boolean photoChanged ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);
    }
}