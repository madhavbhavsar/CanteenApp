package com.madapps.canteen.owner.packagebtn1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.madapps.canteen.R;
import com.madapps.canteen.callbacks.FacultyCallback;
import com.madapps.canteen.loginactivity.LoginActivity;
import com.madapps.canteen.owner.dashboardactivity.DashboardActivity;
import com.madapps.canteen.worker.entryactivity.EntryActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Btn1Activity extends AppCompatActivity {

    TextView pt;
    EditText rst,rsc,rsbf,rsl,rsd;

    Button datepicker;
    Button savebtn;

    MaterialAutoCompleteTextView fnameactv;
    HashMap<String, String> facultydata;


    RecyclerView rview;
    RecyclerView.LayoutManager manager;

    Button addquantitybtn;
    TextView prnumber;
    CardView less, more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn1);

        pt = findViewById(R.id.prnumbert);
        rst = findViewById(R.id.rst);
        rsc = findViewById(R.id.rsc);
        rsbf = findViewById(R.id.rsbf);
        rsl = findViewById(R.id.rsl);
        rsd = findViewById(R.id.rsd);

        fnameactv = findViewById(R.id.fnameactv);
        savebtn = findViewById(R.id.savebtn);

        datepicker = findViewById(R.id.datepicker);
        datepicker.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));


        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        builder.setTitleText("Select Date");
        MaterialDatePicker materialDatePicker = builder.build();

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "datepickertag");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(Long.parseLong(selection.toString()));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = format.format(calendar.getTime());

                String date = selection.toString();
                datepicker.setText(formattedDate);
            }
        });


        facultydata = new HashMap<>();
        readFacultyData(new FacultyCallback() {
            @Override
            public void onCallback(HashMap<String, String> data) {

                List<String> ss = new ArrayList<>();

                for (String s : data.keySet()) {
                    ss.add(s);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Btn1Activity.this, android.R.layout.simple_list_item_1, ss);
                fnameactv.setAdapter(areasAdapter);

            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fnameactv.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Btn1Activity.this, "Missing Value", Toast.LENGTH_SHORT).show();
                } else {

                    savebtn.setEnabled(false);

                    Map<String, Object> data = new HashMap<>();
                    data.put("date", datepicker.getText().toString().trim());
                    data.put("fid", facultydata.get(fnameactv.getText().toString().trim()));
                    data.put("tea", Arrays.asList( pt.getText().toString().trim(),rst.getText().toString().trim()));


                    FirebaseFirestore.getInstance().collection("orderdetails")
                            .document(datepicker.getText().toString().trim() + fnameactv.getText().toString().trim())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Intent intent = new Intent(Btn1Activity.this, Btn1Activity.class);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    finish();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Btn1Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                    savebtn.setEnabled(false);
                                    //Log.w(TAG, "Error adding document", e);
                                }
                            });


                }


            }
        });


//        rview = (RecyclerView) findViewById(R.id.rview);
//        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        rview.setLayoutManager(manager);


    }//                int aa = Integer.parseInt(prnumber.getText().toString().trim());
//                prnumber.setText(String.valueOf( aa+1));
//            }
//        });

//        prnumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (prnumber.getText().toString().trim().equals("0")){
//
//                    addquantitybtn.setVisibility(View.VISIBLE);
//
//                } else {
//                    addquantitybtn.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });WDEFDRDGTFHYJ }


    public void morefunt(View view) {
        TextView t = findViewById(R.id.prnumbert);
        t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) + 1));
    }

    public void morefunc(View view) {
        TextView t = findViewById(R.id.prnumberc);
        t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) + 1));
    }

    public void morefunbf(View view) {
        TextView t = findViewById(R.id.prnumberbf);
        t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) + 1));
    }

    public void morefunl(View view) {
        TextView t = findViewById(R.id.prnumberl);
        t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) + 1));
    }

    public void morefund(View view) {
        TextView t = findViewById(R.id.prnumberd);
        t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) + 1));
    }

    public void lessfunt(View view) {
        TextView t = findViewById(R.id.prnumbert);
        ImageView l = findViewById(R.id.lesst);
        ImageView m = findViewById(R.id.moret);

        if (t.getText().toString().trim().toLowerCase().equals("1")) {
            t.setText("Add");
            l.setVisibility(View.GONE);
            m.setVisibility(View.GONE);
            rst.setEnabled(false);
        } else {
            t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) - 1));
        }

    }

    public void lessfunc(View view) {
        TextView t = findViewById(R.id.prnumberc);
        ImageView l = findViewById(R.id.lessc);
        ImageView m = findViewById(R.id.morec);

        if (t.getText().toString().trim().toLowerCase().equals("1")) {
            t.setText("Add");
            l.setVisibility(View.GONE);
            m.setVisibility(View.GONE);
            rsc.setEnabled(false);
        } else {
            t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) - 1));
        }

    }

    public void lessfunbf(View view) {
        TextView t = findViewById(R.id.prnumberbf);
        ImageView l = findViewById(R.id.lessbf);
        ImageView m = findViewById(R.id.morebf);

        if (t.getText().toString().trim().toLowerCase().equals("1")) {
            t.setText("Add");
            l.setVisibility(View.GONE);
            m.setVisibility(View.GONE);
            rsbf.setEnabled(false);
        } else {
            t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) - 1));
        }

    }

    public void lessfunl(View view) {
        TextView t = findViewById(R.id.prnumberl);
        ImageView l = findViewById(R.id.lessl);
        ImageView m = findViewById(R.id.morel);

        if (t.getText().toString().trim().toLowerCase().equals("1")) {
            t.setText("Add");
            l.setVisibility(View.GONE);
            m.setVisibility(View.GONE);
            rsl.setEnabled(false);
        } else {
            t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) - 1));
        }

    }

    public void lessfund(View view) {
        TextView t = findViewById(R.id.prnumberd);
        ImageView l = findViewById(R.id.lessd);
        ImageView m = findViewById(R.id.mored);

        if (t.getText().toString().trim().toLowerCase().equals("1")) {
            t.setText("Add");
            l.setVisibility(View.GONE);
            m.setVisibility(View.GONE);
            rsd.setEnabled(false);
        } else {
            t.setText(String.valueOf(Integer.parseInt(t.getText().toString().trim()) - 1));
        }

    }


    public void onec(View view) {
        TextView t = findViewById(R.id.prnumberc);
        ImageView l = findViewById(R.id.lessc);
        ImageView m = findViewById(R.id.morec);
        if (t.getText().toString().trim().toLowerCase().equals("add")) {
            t.setText("1");
            l.setVisibility(View.VISIBLE);
            rsc.setEnabled(true);
            m.setVisibility(View.VISIBLE);
        }

    }

    public void onet(View view) {
        TextView t = findViewById(R.id.prnumbert);
        ImageView l = findViewById(R.id.lesst);
        ImageView m = findViewById(R.id.moret);
        if (t.getText().toString().trim().toLowerCase().equals("add")) {
            t.setText("1");
            l.setVisibility(View.VISIBLE);
            rst.setEnabled(true);
            m.setVisibility(View.VISIBLE);
        }

    }

    public void onebf(View view) {
        TextView t = findViewById(R.id.prnumberbf);
        ImageView l = findViewById(R.id.lessbf);
        ImageView m = findViewById(R.id.morebf);
        if (t.getText().toString().trim().toLowerCase().equals("add")) {
            t.setText("1");
            rsbf.setEnabled(true);
            l.setVisibility(View.VISIBLE);
            m.setVisibility(View.VISIBLE);
        }

    }

    public void onel(View view) {
        TextView t = findViewById(R.id.prnumberl);
        ImageView l = findViewById(R.id.lessl);
        ImageView m = findViewById(R.id.morel);
        if (t.getText().toString().trim().toLowerCase().equals("add")) {
            t.setText("1");
            rsl.setEnabled(true);
            l.setVisibility(View.VISIBLE);
            m.setVisibility(View.VISIBLE);
        }

    }

    public void oned(View view) {
        TextView t = findViewById(R.id.prnumberd);
        ImageView l = findViewById(R.id.lessd);
        ImageView m = findViewById(R.id.mored);
        if (t.getText().toString().trim().toLowerCase().equals("add")) {
            t.setText("1");
            rsd.setEnabled(true);
            l.setVisibility(View.VISIBLE);
            m.setVisibility(View.VISIBLE);
        }

    }


    private void readFacultyData(FacultyCallback facultyCallback) {

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Btn1Activity.this, DashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }
}