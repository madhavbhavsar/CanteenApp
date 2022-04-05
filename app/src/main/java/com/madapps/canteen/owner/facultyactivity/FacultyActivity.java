package com.madapps.canteen.owner.facultyactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.madapps.canteen.R;
import com.madapps.canteen.loginactivity.LoginActivity;
import com.madapps.canteen.owner.dashboardactivity.DashboardActivity;

import java.util.HashMap;
import java.util.Map;

public class FacultyActivity extends AppCompatActivity {

    EditText fid, fname;
    Button savebtn;
    RecyclerView facultyrv;
    RecyclerView.LayoutManager manager;
    FacultyAdapter adapter;
    String fids = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        fids = "";
        fid = (EditText) findViewById(R.id.fid);
        fname = (EditText) findViewById(R.id.fname);
        savebtn = (Button) findViewById(R.id.savebtn);

        facultyrv = (RecyclerView) findViewById(R.id.facultyrv);
        manager = new LinearLayoutManager(FacultyActivity.this, LinearLayoutManager.VERTICAL, false);
        facultyrv.setLayoutManager(manager);


        Query query = FirebaseFirestore.getInstance().collection("faculties");
        FirestoreRecyclerOptions<FacultyModel> options = new FirestoreRecyclerOptions.Builder<FacultyModel>().setQuery(query, FacultyModel.class).build();
        adapter = new FacultyAdapter(options, FacultyActivity.this);
        adapter.notifyDataSetChanged();
        facultyrv.setAdapter(adapter);



        FirebaseFirestore.getInstance().collection("entries").document("e1").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                  //  Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                 //   Log.d(TAG, "Current data: " + snapshot.getData());
                    fids = snapshot.getString("fids");

                    fid.setText("f"+fids);

                } else {
                  //  Log.d(TAG, "Current data: null");
                }
            }
        });





        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fids.isEmpty()){
                    Toast.makeText(FacultyActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                } else if (fname.getText().toString().isEmpty()){
                    Toast.makeText(FacultyActivity.this, "Missing Value", Toast.LENGTH_SHORT).show();

                } else {
                    savebtn.setEnabled(false);

                    Map<String, Object> data = new HashMap<>();
                    data.put("fid", "f"+fids);
                    data.put("facultyname", fname.getText().toString().trim());


                    FirebaseFirestore.getInstance().collection("faculties").document("f"+fids)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Map<String, Object> ndata = new HashMap<>();
                                    ndata.put("fids", String.valueOf(Integer.parseInt(fids) + 1));

                                    FirebaseFirestore.getInstance().collection("entries").document("e1")
                                            .update(ndata)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    savebtn.setEnabled(true);
                                                    Toast.makeText(FacultyActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                                    fname.setText("");

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Log.w(TAG, "Error writing document", e);
                                                }
                                            });



                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   // Log.w(TAG, "Error writing document", e);
                                }
                            });







                }









            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FacultyActivity.this, DashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}