package com.example.lab08.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lab08.Adapter.UsuarioAdapter;
import com.example.lab08.Data.Data;
import com.example.lab08.Helper.RecyclerItemTouchHelper;
import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, UsuarioAdapter.ProfesorAdapterListener {
    private RecyclerView mRecyclerView;
    private UsuarioAdapter mAdapter;
    private List<Usuario> usuariosList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;
    private Data model;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.coordinatorLayout = findViewById(R.id.coordinator_layout_usuario);

        getSupportActionBar().setTitle(getString(R.string.titleUsuarios));
        mRecyclerView = findViewById(R.id.recycler_cursosFld);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        fab = findViewById(R.id.AddCurso);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        //usuariosList = new ArrayList<>();
        Data d = null;
        int []photos = {R.drawable.bryan,R.drawable.user,R.drawable.fofo};
        try {
            d = new Data(photos,getResources());
        } catch (IOException e) {
            e.printStackTrace();
        }
        usuariosList = d.getProfesorList();
        intentInformation();
        mAdapter = new UsuarioAdapter(usuariosList, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        // whiteNotificationBar(mRecyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) throws IOException {
        Usuario aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
        mAdapter.notifyDataSetChanged();
        if (direction == ItemTouchHelper.START) {
            //send data to Edit Activity
            Intent intent = new Intent(this, VerUsuario.class);
            intent.putExtra("ver", true);
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    writeOnExternalStorage(aux);
                } else {
                    requestPermission(); // Code for permission
                    writeOnExternalStorage(aux);
                }
            }
            else {
                writeOnExternalStorage(aux);
            }
            aux.setFoto(null);
            intent.putExtra("user", aux);
            startActivity(intent);
        } else {

            //send data to Edit Activity
            Intent intent = new Intent(this, AddUpdUsuario.class);
            intent.putExtra("editable", true);
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    writeOnExternalStorage(aux);
                } else {
                    requestPermission(); // Code for permission
                    writeOnExternalStorage(aux);
                }
            }
            else {
                writeOnExternalStorage(aux);
            }
            aux.setFoto(null);
            intent.putExtra("user", aux);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed(){

    }
    @Override
    public void onItemMove(int source, int target) {
        mAdapter.onItemMove(source, target);
    }
    @Override
    public void onContactSelected(Usuario usuario){
        Toast.makeText(getApplicationContext(), "Selected: " + usuario.getCedula() + ", " + usuario.getNombre(), Toast.LENGTH_LONG);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
    public void writeOnExternalStorage(Usuario aux){
        File file = new File(Environment.getExternalStorageDirectory() + "/imageBitmap" + ".png");
        try (FileOutputStream fOut = new FileOutputStream(file)) {
            byte[] byteArray = aux.getFoto();
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


    public void intentInformation(){
        Bundle extras = getIntent().getExtras();
             // se esta editando un profesor
        if(extras!=null) {
            Usuario auxiliar = (Usuario) getIntent().getSerializableExtra("editado");
            boolean founded = false;
            for (Usuario c1 : usuariosList) {
                //Toast.makeText(getApplicationContext(), auxiliar.getCedula() + " vs "+c1.getCedula(), Toast.LENGTH_LONG).show();
                if (c1.getCedula().equals(auxiliar.getCedula())) {
                    c1.setNombre(auxiliar.getNombre());
                    c1.setCorreo(auxiliar.getCorreo());
                    c1.setTelefono(auxiliar.getTelefono());
                    auxiliar = c1;
                    founded = true;
                    break;
                }
            }
            if (founded) {
                Toast.makeText(getApplicationContext(), auxiliar.getNombre() + " editado correctamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), auxiliar.getNombre() + " no encontrado", Toast.LENGTH_LONG).show();
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
    }
}
