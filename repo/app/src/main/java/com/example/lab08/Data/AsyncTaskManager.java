package com.example.lab08.Data;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;


import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AsyncTaskManager  extends AsyncTask<String, String, String>{

    public interface AsyncResponse {
        void processFinish(String output) throws JSONException;
    }
    public AsyncResponse delegate = null;
private Resources R;
    private int[] photos;
    private Usuario auxiliar;
    public AsyncTaskManager(Usuario auxiliar,int[] photos,Resources r, AsyncResponse delegate) {

        this.delegate = delegate;
        this.R = r;
        this.photos=photos;
        this.auxiliar = auxiliar;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {


        Data d = null;

        try {
            d = new Data(photos,R);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Usuario> usuariosList = d.getProfesorList();
        //INTENT/////////////////////////////////////////
        if(auxiliar!=null) {
            for (Usuario c1 : usuariosList) {

                if (c1.getCedula().equals(auxiliar.getCedula())) {
                    c1.setNombre(auxiliar.getNombre());
                    c1.setCorreo(auxiliar.getCorreo());
                    c1.setTelefono(auxiliar.getTelefono());
                    auxiliar = c1;
                    break;
                }
            }

            String path = Environment.getExternalStorageDirectory() + "/imageBitmap" + ".png";
            Bitmap Icon = BitmapFactory.decodeFile(path);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            if (!(Icon.compress(Bitmap.CompressFormat.PNG, 100, stream))) {
                Icon.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }
            byte[] byteArray = stream.toByteArray();
            auxiliar.setFoto(byteArray);
        }
        //INTENT/////////////////////////////////////////
        //List<Usuario> usuariosList;

        Usuario u = usuariosList.get(0);
        JSONArray jsonArray = new JSONArray();
        for(Usuario student: usuariosList) {
            try {
                String jsonString = new JSONObject()
                        .put("correo", student.getCorreo())
                        .put("contraseña", student.getContraseña())
                        .put("nombre", student.getNombre())
                        .put("cedula", student.getCedula())
                        .put("foto",  new String(student.getFoto()))
                        .put("telefono", student.getTelefono())
                        .toString();
                jsonArray.put(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
;

        String jArray = jsonArray.toString();

        return jArray;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            delegate.processFinish(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
