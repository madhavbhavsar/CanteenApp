package com.madapps.canteen.owner.itemmaster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.madapps.canteen.R;
import com.madapps.canteen.owner.facultyactivity.FacultyAdapter;

public class ItemAdapter extends FirestoreRecyclerAdapter<ItemModel, ItemAdapter.ItemVH> {


    Context context;

    public ItemAdapter(@NonNull FirestoreRecyclerOptions<ItemModel> options,Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull ItemVH holder, int position, @NonNull ItemModel model) {

        holder.itemname.setText(model.getItemid()+" "+model.getItemname());

    }

    @NonNull
    @Override
    public ItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rvlayout,parent,false);
        return new ItemAdapter.ItemVH(view);
    }

    public class ItemVH extends RecyclerView.ViewHolder {

        TextView itemname;
        public ItemVH(@NonNull View itemView) {
            super(itemView);

            itemname = (TextView) itemView.findViewById(R.id.itemname);


        }
    }

}
