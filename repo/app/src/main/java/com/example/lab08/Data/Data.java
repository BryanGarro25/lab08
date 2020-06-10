package com.example.lab08.Data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.lab08.LogicaNegocio.Usuario;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<Usuario> UsuarioList;
    public Data(int[] arr, Resources r) throws IOException {
        UsuarioList = new ArrayList<>();
        this.prepareProfesorData(arr,r);

    }
    public void prepareProfesorData(int[] arr,Resources r) throws IOException {


        Bitmap Icon = BitmapFactory.decodeResource(r, arr[0]);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(!(Icon.compress(Bitmap.CompressFormat.PNG, 100, stream))){
            Icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        byte[] byteArray = stream.toByteArray();


        Usuario usuario = new Usuario("123", "Jose", "@jose", "123", byteArray,"85916085");
        UsuarioList.add(usuario);

        Icon = BitmapFactory.decodeResource(r, arr[1]);
        stream = new ByteArrayOutputStream();
        if(!(Icon.compress(Bitmap.CompressFormat.PNG, 100, stream))){
            Icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        byteArray = stream.toByteArray();
        usuario = new Usuario("234", "Juan", "@juan", "876",byteArray,"85916085");
        UsuarioList.add(usuario);

        Icon = BitmapFactory.decodeResource(r, arr[2]);
        stream = new ByteArrayOutputStream();
        if(!(Icon.compress(Bitmap.CompressFormat.PNG, 100, stream))){
            Icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        byteArray = stream.toByteArray();
        usuario = new Usuario("345", "Mario", "@mario", "789",byteArray,"85916085");
        UsuarioList.add(usuario);

        Icon = BitmapFactory.decodeResource(r, arr[1]);
        stream = new ByteArrayOutputStream();
        if(!(Icon.compress(Bitmap.CompressFormat.PNG, 100, stream))){
            Icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        byteArray = stream.toByteArray();
        usuario = new Usuario("456", "Jesus", "@Jesus", "978",byteArray,"85916085");
        UsuarioList.add(usuario);
    }

    public List<Usuario> getProfesorList() {
        return UsuarioList;
    }
    public void setProfesorList(List<Usuario> UsuarioList) {
        this.UsuarioList = UsuarioList;
    }

}
