package com.madapps.canteen.owner.workeractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.madapps.canteen.R;
import com.madapps.canteen.owner.dashboardactivity.DashboardActivity;
import com.madapps.canteen.owner.facultyactivity.FacultyActivity;

public class WorkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WorkerActivity.this, DashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
    }
}