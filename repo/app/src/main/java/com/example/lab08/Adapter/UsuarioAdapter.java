package com.example.lab08.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab08.LogicaNegocio.Usuario;
import com.example.lab08.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.MyViewHolder> implements Filterable {
    private List<Usuario> profesorList;
    private List<Usuario> profesorListFiltered;
    private ProfesorAdapterListener listener;
    private Usuario deletedItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView titulo1, titulo2, description;
        private ImageView image;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;


        public MyViewHolder(View view) {
            super(view);
            titulo1 = view.findViewById(R.id.titleFirstLbl);
            titulo2 = view.findViewById(R.id.titleSecLbl);
            description = view.findViewById(R.id.descriptionLbl);
            image = view.findViewById(R.id.capturedImage);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(profesorListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public UsuarioAdapter(List<Usuario> profesorList, ProfesorAdapterListener listener) {
        this.profesorList = profesorList;
        this.listener = listener;
        //init filter
        this.profesorListFiltered = profesorList;
    }

    @Override
    public UsuarioAdapter.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UsuarioAdapter.MyViewHolder holder, int position) {
        final Usuario profesor = profesorListFiltered.get(position);
        holder.titulo1.setText(profesor.getNombre());
        holder.titulo2.setText(profesor.getCorreo());
        holder.description.setText("Cedula " + profesor.getCedula());
        if(profesor.getFoto()!= null){
            byte[] byteArray = profesor.getFoto();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


            holder.image.setImageBitmap(bmp);
        }
    }

    @Override
    public int getItemCount() {
        return profesorListFiltered.size();
    }
    public void removeItem(int position) {
        deletedItem = profesorListFiltered.remove(position);
        Iterator<Usuario> iter = profesorList.iterator();
        while (iter.hasNext()) {
            Usuario aux = iter.next();
            if (deletedItem.equals(aux))
                iter.remove();
        }
        // notify item removed
        notifyItemRemoved(position);
    }
    public void restoreItem(int position) {

        if (profesorListFiltered.size() == profesorList.size()) {
            profesorListFiltered.add(position, deletedItem);
        } else {
            profesorListFiltered.add(position, deletedItem);
            profesorList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }
    public Usuario getSwipedItem(int index) {
        if (this.profesorList.size() == this.profesorListFiltered.size()) { //not filtered yet
            return profesorList.get(index);
        } else {
            return profesorListFiltered.get(index);
        }
    }
    public void onItemMove(int fromPosition, int toPosition) {
        if (profesorList.size() == profesorListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(profesorList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(profesorList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(profesorListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(profesorListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    profesorListFiltered = profesorList;
                } else {
                    List<Usuario> filteredList = new ArrayList<>();
                    for (Usuario row : profesorList) {
                        // filter use two parameters
                        if (row.getCedula().toLowerCase().contains(charString.toLowerCase()) || row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    profesorListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = profesorListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                profesorListFiltered = (ArrayList<Usuario>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ProfesorAdapterListener {
        void onContactSelected(Usuario profesor);
    }
}
