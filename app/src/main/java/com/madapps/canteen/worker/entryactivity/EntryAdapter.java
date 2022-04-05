package com.madapps.canteen.worker.entryactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.madapps.canteen.R;
import com.madapps.canteen.owner.facultyactivity.FacultyAdapter;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class EntryAdapter extends FirestoreRecyclerAdapter<EntryModel, EntryAdapter.EntryVH> {


    Context context;
    HashMap<String,String> itemdata;

    public EntryAdapter(@NonNull FirestoreRecyclerOptions<EntryModel> options, Context context, HashMap<String,String> itemdata) {
        super(options);
        this.context = context;
        this.itemdata = itemdata;
    }

    @Override
    protected void onBindViewHolder(@NonNull EntryVH holder, int position, @NonNull EntryModel model) {


        holder.entrydetail.setText( itemdata.get(model.getItemid())+"  Quantity - "+model.getItemquantity()+"  Paid - "+model.getIspaid());



    }

    @NonNull
    @Override
    public EntryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_rvlayout,parent,false);
        return new EntryAdapter.EntryVH(view);
    }

    public class EntryVH extends RecyclerView.ViewHolder{

        TextView entrydetail;
        public EntryVH(@NonNull View itemView) {
            super(itemView);
            entrydetail = itemView.findViewById(R.id.entrydetail);
        }
    }
}
