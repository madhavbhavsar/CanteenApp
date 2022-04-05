package com.madapps.canteen.owner.facultyactivity;

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

public class FacultyAdapter extends FirestoreRecyclerAdapter<FacultyModel, FacultyAdapter.FacultyVH> {

    Context context;

    public FacultyAdapter(@NonNull FirestoreRecyclerOptions<FacultyModel> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull FacultyVH holder, int position, @NonNull FacultyModel model) {

        holder.fname.setText(model.getFacultyname());
    }

    @NonNull
    @Override
    public FacultyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_rvlayout,parent,false);
        return new FacultyVH(view);
    }

    public class FacultyVH extends RecyclerView.ViewHolder {

        TextView fname;

        public FacultyVH(@NonNull View itemView) {
            super(itemView);
            fname = (TextView) itemView.findViewById(R.id.fname);
        }
    }

}
