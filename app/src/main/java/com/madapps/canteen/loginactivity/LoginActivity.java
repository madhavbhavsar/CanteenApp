package com.madapps.canteen.loginactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.madapps.canteen.R;
import com.madapps.canteen.owner.dashboardactivity.DashboardActivity;
import com.madapps.canteen.worker.entryactivity.EntryActivity;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    RadioButton ownerradio,workerradio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ownerradio = (RadioButton) findViewById(R.id.ownerradio);
        workerradio = (RadioButton) findViewById(R.id.workerradio);
        loginbtn = (Button) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ownerradio.isChecked()){

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();

                }

                if (workerradio.isChecked()){

                    Intent intent = new Intent(LoginActivity.this, EntryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();

                }



            }
        });


    }
}