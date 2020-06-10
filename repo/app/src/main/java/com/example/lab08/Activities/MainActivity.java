package com.example.lab08.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import com.example.lab08.Adapter.UsuarioAdapter;
import com.example.lab08.Data.Data;
import com.example.lab08.Helper.RecyclerItemTouchHelper;
import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.coordinatorLayout = findViewById(R.id.coordinator_layout_usuario);

        getSupportActionBar().setTitle(getString(R.string.titleUsuarios));
        mRecyclerView = findViewById(R.id.recycler_cursosFld);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        fab = findViewById(R.id.AddCurso);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddUpdCurso();
            }
        });*/
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
        mAdapter = new UsuarioAdapter(usuariosList, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        //intentInformation();
        mAdapter.notifyDataSetChanged();
        // whiteNotificationBar(mRecyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.END) {
            Usuario aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, VerUsuario.class);
            //intent.putExtra("user", aux);
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        } else {
            Usuario aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, AddUpdUsuario.class);
            intent.putExtra("editable", true);

            File file = new File(Environment.getExternalStorageDirectory() + "imageBitmap" + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            byte[] byteArray = aux.getFoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

            intent.putExtra("user", aux);
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed(){}
    @Override
    public void onItemMove(int source, int target) {}
    @Override
    public void onContactSelected(Usuario usuario){}
}
