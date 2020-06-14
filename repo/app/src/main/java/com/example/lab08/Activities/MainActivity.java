package com.example.lab08.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, UsuarioAdapter.ProfesorAdapterListener, SensorEventListener {
    private RecyclerView mRecyclerView;
    private UsuarioAdapter mAdapter;
    private List<Usuario> usuariosList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;
    private Data model;
    private Usuario UserSelected;
    private Usuario aux;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int Image_Capture_Code = 1;
    SensorManager sm;
    Sensor sensor;
    String msjToSend;
    ImageView ActfotoBTNAux;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.coordinatorLayout = findViewById(R.id.coordinator_layout_usuario);
        UserSelected = null;
        getSupportActionBar().setTitle(getString(R.string.titleUsuarios));
        mRecyclerView = findViewById(R.id.recycler_cursosFld);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);


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
        mAdapter = new UsuarioAdapter(usuariosList, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        // whiteNotificationBar(mRecyclerView);
    }
//---------------------------- Moviemientos ------------------------------------------------------------------
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) throws IOException {
        final Usuario aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
        this.aux = aux;
        mAdapter.notifyDataSetChanged();
        if (direction == ItemTouchHelper.START) {
            //----------------------------------------------------------------ver------------------------------------------

            AlertDialog.Builder mBuider = new AlertDialog.Builder(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.activity_ver_usuario,null);
            mBuider.setView(view);
            final AlertDialog dialog = mBuider.create();

            EditText DnombreFLD = view.findViewById(R.id.NombreFLD);
            EditText DcedulaFLD = view.findViewById(R.id.CedulaFLD);
            EditText DemailFLD = view.findViewById(R.id.EmailFLD);
            EditText DtelefonoFLD = view.findViewById(R.id.TelefonoFLD);
            ImageView DimageFLD = view.findViewById(R.id.capturedImage);
            if(aux.getFoto()!= null){
                byte[] byteArray = aux.getFoto();
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                DimageFLD.setImageBitmap(bmp);
            }
            DnombreFLD.setText(aux.getNombre());
            DcedulaFLD.setText(aux.getCedula());
            DemailFLD.setText(aux.getCorreo());
            DtelefonoFLD.setText(aux.getTelefono());

            ImageButton text = view.findViewById(R.id.text);
            ImageButton call = view.findViewById(R.id.call);
            ImageButton salirBTN = view.findViewById(R.id.salirBTN);

            salirBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.hide();
                }
            });
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

            dialog.show();
            //----------------------------------------------------------------ver------------------------------------------
        } else {
            //----------------------------------------------------------------editar------------------------------------------
            //send data to Edit Activity
            AlertDialog.Builder mBuider = new AlertDialog.Builder(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.activity_add_upd_usuario,null);
            mBuider.setView(view);
            final AlertDialog dialog = mBuider.create();

            final EditText DnombreFLD = view.findViewById(R.id.NombreFLD);
            final EditText DcedulaFLD = view.findViewById(R.id.CedulaFLD);
            final EditText DemailFLD = view.findViewById(R.id.EmailFLD);
            final EditText DtelefonoFLD = view.findViewById(R.id.TelefonoFLD);
            ImageView DimageFLD = view.findViewById(R.id.capturedImage);
            if(aux.getFoto()!= null){
                byte[] byteArray = aux.getFoto();
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                DimageFLD.setImageBitmap(bmp);
            }
            DnombreFLD.setText(aux.getNombre());
            DcedulaFLD.setText(aux.getCedula());
            DemailFLD.setText(aux.getCorreo());
            DtelefonoFLD.setText(aux.getTelefono());
            DcedulaFLD.setEnabled(false);
            ImageButton confirmBTN = view.findViewById(R.id.ConfirmBTN);
            ImageButton ActfotoBTN = view.findViewById(R.id.fotoBTN);
            ActfotoBTNAux = DimageFLD;
            confirmBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (Usuario c1 : usuariosList) {
                        //Toast.makeText(getApplicationContext(), auxiliar.getCedula() + " vs "+c1.getCedula(), Toast.LENGTH_LONG).show();
                        if (c1.getCedula().equals(DcedulaFLD.getText().toString())) {
                            c1.setNombre(DnombreFLD.getText().toString());
                            c1.setCorreo(DemailFLD.getText().toString());
                            c1.setTelefono(DtelefonoFLD.getText().toString());

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            Bitmap bmp =  ((BitmapDrawable)ActfotoBTNAux.getDrawable()).getBitmap();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            bmp.recycle();
                            c1.setFoto(byteArray);

                            break;
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    dialog.hide();
                }
            });
            ActfotoBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cInt,Image_Capture_Code);
                }
            });
            dialog.show();
            //----------------------------------------------------------------editar------------------------------------------
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
        if(UserSelected != null) {
            if(usuario.getCedula().equals(UserSelected.getCedula())) {
                getSupportActionBar().setTitle(getString(R.string.titleUsuarios));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6200EE")));
                UserSelected=null;
            }else{
                getSupportActionBar().setTitle("Llamar a " + usuario.getNombre());
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#388E3C")));
                UserSelected = usuario;
            }
        }else{
            getSupportActionBar().setTitle("Llamar a " + usuario.getNombre());
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#388E3C")));
            UserSelected = usuario;
        }
    }
    //---------------------------- Moviemientos------------------------------------------------------------------

    //---------------------------- Manejo de DATA------------------------------------------------------------------

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

    //---------------------------- Manejo de DATA------------------------------------------------------------------

    //----------------------------------Permisos, llamadas, mensajes, sensores
    public void callPhoneNumber() {
        try {
            if(Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + UserSelected.getTelefono()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + UserSelected.getTelefono()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    } //calls to UserSelected
    public void callPhoneNumber2() {
        try {
            if(Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + aux.getTelefono()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + aux.getTelefono()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }// calls aux
    public void openDiolog(){
        AlertDialog.Builder mBuider = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.sendmessege,null);
        mBuider.setView(view);
        final AlertDialog dialog = mBuider.create();
        final EditText Dmessege = (EditText) view.findViewById(R.id.msjText);
        TextView NombreContacto = (TextView)view.findViewById(R.id.NombreContacto);
        NombreContacto.setText("Mensaje para: "+aux.getNombre());
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
    } // abre el dialog de msj
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
    } // check or ask for mermissions then call sendMSJ
    private void requestPermission2() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
            Toast.makeText(MainActivity.this, "Porfavor dar permisos de envio de mensajes.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    } //permiso de sms
    private boolean checkPermission2() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }//permiso de sms
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    } //escribir external storage permiso
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    } // read external storage permiso
    public void sendMSJ(String msg){
        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(aux.getTelefono(),null,msg,null,null);
            Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    } //send mesegge to aux



    @Override
    public void onSensorChanged(SensorEvent event) {
        //Toast.makeText(getApplicationContext(), "llamar", Toast.LENGTH_LONG).show();
        if (event.values[0] == 0){

            if(UserSelected!=null){
                callPhoneNumber();
                getSupportActionBar().setTitle(getString(R.string.titleUsuarios));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6200EE")));
                UserSelected=null;
            }else{
                Toast.makeText(getApplicationContext(), "Seleccione un contacto.", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ActfotoBTNAux.setImageBitmap(bmp);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
