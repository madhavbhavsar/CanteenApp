package com.madapps.canteen.owner.itemmaster;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.madapps.canteen.owner.facultyactivity.FacultyActivity;
import com.madapps.canteen.owner.facultyactivity.FacultyAdapter;
import com.madapps.canteen.owner.facultyactivity.FacultyModel;

import java.util.HashMap;
import java.util.Map;

public class ItemsActivity extends AppCompatActivity {

    EditText itemname,itemprice;
    Button savebtn;
    RecyclerView itemrv;
    RecyclerView.LayoutManager manager;
    ItemAdapter adapter;
    String itemids = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        itemname = (EditText) findViewById(R.id.itemname);
        itemprice = (EditText) findViewById(R.id.itemprice);
        savebtn = (Button) findViewById(R.id.savebtn);
        itemrv =(RecyclerView) findViewById(R.id.itemrv);

        manager = new LinearLayoutManager(ItemsActivity.this, LinearLayoutManager.VERTICAL, false);
        itemrv.setLayoutManager(manager);

        Query query = FirebaseFirestore.getInstance().collection("items");
        FirestoreRecyclerOptions<ItemModel> options = new FirestoreRecyclerOptions.Builder<ItemModel>().setQuery(query, ItemModel.class).build();
        adapter = new ItemAdapter(options, ItemsActivity.this);
        adapter.notifyDataSetChanged();
        itemrv.setAdapter(adapter);



        FirebaseFirestore.getInstance().collection("entries").document("e2").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //  Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    //   Log.d(TAG, "Current data: " + snapshot.getData());
                    itemids = snapshot.getString("itemids");


                } else {
                    //  Log.d(TAG, "Current data: null");
                }
            }
        });





        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemids.isEmpty()){
                    Toast.makeText(ItemsActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                } else if (itemname.getText().toString().isEmpty()){
                    Toast.makeText(ItemsActivity.this, "Missing Value", Toast.LENGTH_SHORT).show();

                } else if (itemprice.getText().toString().isEmpty()){
                    Toast.makeText(ItemsActivity.this, "Missing Value", Toast.LENGTH_SHORT).show();
                } else {
                    savebtn.setEnabled(false);

                    Map<String, Object> data = new HashMap<>();
                    data.put("itemid", "i"+itemids);
                    data.put("itemname", itemname.getText().toString().trim()+" Rs."+ itemprice.getText().toString().trim());
                    data.put("itemprice", itemprice.getText().toString().trim());



                    FirebaseFirestore.getInstance().collection("items").document("i"+itemids)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Map<String, Object> ndata = new HashMap<>();
                                    ndata.put("itemids", String.valueOf(Integer.parseInt(itemids) + 1));

                                    FirebaseFirestore.getInstance().collection("entries").document("e2")
                                            .update(ndata)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    savebtn.setEnabled(true);
                                                    Toast.makeText(ItemsActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                                    itemname.setText("");
                                                    itemprice.setText("");
                                                    itemname.setFocusable(true);

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
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ItemsActivity.this, DashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }
}