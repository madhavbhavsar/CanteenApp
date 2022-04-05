package com.madapps.canteen.worker.entryactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.madapps.canteen.R;
import com.madapps.canteen.callbacks.FacultyCallback;
import com.madapps.canteen.callbacks.ItemsCallback;
import com.madapps.canteen.loginactivity.LoginActivity;
import com.madapps.canteen.owner.dashboardactivity.DashboardActivity;
import com.madapps.canteen.owner.itemmaster.ItemsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class EntryActivity extends AppCompatActivity {

    AutoCompleteTextView fnameactv;

    Button datepicker;
    Button addbtn;
    RecyclerView orderdetailsrv;
    RecyclerView.LayoutManager manager;
    HashMap<String,String> facultydata;
    HashMap<String,String> itemdata,itempricedata,itemidname;
    HashMap<String,String> iidata;
    EntryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        facultydata = new HashMap<>();
        itemdata = new HashMap<>();
        itempricedata = new HashMap<>();
        itemidname = new HashMap<>();
        iidata = new HashMap<>();

        fnameactv = (AutoCompleteTextView) findViewById(R.id.fnameactv);
        //itemactv = (AutoCompleteTextView) findViewById(R.id.itemactv);
        datepicker = (Button) findViewById(R.id.datepicker);

        addbtn = (Button) findViewById(R.id.addbtn);

        orderdetailsrv = (RecyclerView) findViewById(R.id.orderdetailsrv);
        manager = new LinearLayoutManager(EntryActivity.this, LinearLayoutManager.VERTICAL, false);
        orderdetailsrv.setLayoutManager(manager);
        datepicker.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));





        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        builder.setTitleText("Select Date");
        MaterialDatePicker materialDatePicker = builder.build();

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(),"datepickertag");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(Long.parseLong(selection.toString()) );
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate  = format.format(calendar.getTime());

                String date = selection.toString();
                datepicker.setText(formattedDate);
            }
        });


        readFacultyData(new FacultyCallback() {
            @Override
            public void onCallback(HashMap<String, String> data) {

                List<String> ss = new ArrayList<>();

                for (String s : data.keySet()) {
                    ss.add(s);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(EntryActivity.this, android.R.layout.simple_list_item_1, ss);
                fnameactv.setAdapter(areasAdapter);

            }
        });

        readItemsData(new ItemsCallback() {
            @Override
            public void onCallback(HashMap<String, String> data, HashMap<String, String> dataprice,HashMap<String, String> itemidname) {
                iidata = (HashMap<String, String>) itemidname.clone();
            }
        });

       datepicker.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               if (fnameactv.getText().toString().trim().isEmpty()){

               } else {
                   Query query = FirebaseFirestore.getInstance().collection("orderdetails")
                           .whereEqualTo("fid", facultydata.get(fnameactv.getText().toString().trim()))
                           .whereEqualTo("date", datepicker.getText().toString().trim());

                   FirestoreRecyclerOptions<EntryModel> options1 = new FirestoreRecyclerOptions.Builder<EntryModel>().setQuery(query, EntryModel.class).build();

                   adapter = new EntryAdapter(options1, EntryActivity.this,iidata);
                   adapter.notifyDataSetChanged();
                   adapter.startListening();
                   orderdetailsrv.setAdapter(adapter);
               }

           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });


        fnameactv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("taggg", "clicked0" );
                Log.i("taggg", facultydata.get(fnameactv.getText().toString().trim()) );

                Query query = FirebaseFirestore.getInstance().collection("snacksdetails")
                        .whereEqualTo("fid", facultydata.get(fnameactv.getText().toString().trim()))
                        .whereEqualTo("date", datepicker.getText().toString().trim());

                FirestoreRecyclerOptions<EntryModel> options1 = new FirestoreRecyclerOptions.Builder<EntryModel>().setQuery(query, EntryModel.class).build();

                adapter = new EntryAdapter(options1, EntryActivity.this,iidata);
                adapter.notifyDataSetChanged();
                adapter.startListening();
                orderdetailsrv.setAdapter(adapter);
            }
        });


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showcustomdialog();

            }
        });





    }

    private void showcustomdialog() {

        final Dialog dialog= new Dialog(EntryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.additem_dialog);

        final AutoCompleteTextView itemactv = dialog.findViewById(R.id.itemname);
        final EditText quantity = dialog.findViewById(R.id.itemquantity);
        final Button savebtn = dialog.findViewById(R.id.savebtn);
        final Button cancelbtn = dialog.findViewById(R.id.cancelbtn);
        final RadioButton paidyes = dialog.findViewById(R.id.paidyes);
        final RadioButton paidno = dialog.findViewById(R.id.paidno);


        readItemsData(new ItemsCallback() {
            @Override
            public void onCallback(HashMap<String, String> data, HashMap<String, String> dataprice,HashMap<String,String> itemidname) {

                List<String> ss = new ArrayList<>();

                for (String s : data.keySet()) {
                    ss.add(s);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(EntryActivity.this, android.R.layout.simple_list_item_1, ss);
                itemactv.setAdapter(areasAdapter);


            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemactv.getText().toString().trim().isEmpty()) {
                    Toast.makeText(EntryActivity.this, "Missing Value", Toast.LENGTH_SHORT).show();
                } else if (quantity.getText().toString().trim().isEmpty()) {
                    Toast.makeText(EntryActivity.this, "Missing Value", Toast.LENGTH_SHORT).show();
                } else {

                    savebtn.setEnabled(false);

                    Map<String, Object> data = new HashMap<>();
                    data.put("date",datepicker.getText().toString().trim());
                    data.put("fid", facultydata.get(fnameactv.getText().toString().trim()));
                    data.put("itemid", itemdata.get(itemactv.getText().toString().trim()));
                    data.put("itemquantity",quantity.getText().toString().trim());

                    if (paidyes.isChecked()){
                        data.put("ispaid","yes");
                    }
                    if (paidno.isChecked()){
                        data.put("ispaid","no");
                    }


                    FirebaseFirestore.getInstance().collection("snacksdetails")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    Toast.makeText(EntryActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                    savebtn.setEnabled(true);
                                    quantity.setText("");
                                    itemactv.setText("");
                                    itemactv.setFocusable(true);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EntryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    savebtn.setEnabled(false);
                                    //Log.w(TAG, "Error adding document", e);
                                }
                            });




                }


            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);



    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(EntryActivity.this, DashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

    }

    private void readFacultyData(FacultyCallback facultyCallback){

        OnCompleteListener<QuerySnapshot> qq = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d("HeloWorl", task.getResult().getDocuments().toString());


                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("HeloWorl", document.getId() + " => " + document.getData());

                            facultydata.put(document.getString("facultyname"), document.getString("fid"));

                        }


                        facultyCallback.onCallback(facultydata);
                    } else {
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                    }


            }
        };

            FirebaseFirestore.getInstance().collection("faculties")
                    .get().addOnCompleteListener(qq);



    }

    private void readItemsData(ItemsCallback itemsCallback){

        OnCompleteListener<QuerySnapshot> qq = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("HeloWorl", task.getResult().getDocuments().toString());


                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("HeloWorl", document.getId() + " => " + document.getData());

                        itemdata.put(document.getString("itemname"), document.getString("itemid"));
                        itempricedata.put(document.getString("itemname"), document.getString("itemprice"));
                        itemidname.put(document.getString("itemid"),document.getString("itemname"));
                    }


                    itemsCallback.onCallback(itemdata,itempricedata,itemidname);
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }


            }
        };

        FirebaseFirestore.getInstance().collection("items")
                .get().addOnCompleteListener(qq);



    }

    @Override
    protected void onStop() {
        super.onStop();
//        adapter.stopListening();
    }
}