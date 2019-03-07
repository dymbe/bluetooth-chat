package com.example.bluetoothchat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    private enum Screen {
        MAIN,
        DENIED_PERMISSION
    }
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_CODE = 0;

    private ViewFlipper viewFlipper;
    private Button allowLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewFlipper = findViewById(R.id.view_flipper);
        allowLocationBtn = findViewById(R.id.allow_location);

        allowLocationBtn.setOnClickListener(this::allowLocation);
        requestPermissions(PERMISSIONS, REQUEST_CODE);
    }

    private void allowLocation(View v) {
        requestPermissions(PERMISSIONS, REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    switchView(Screen.MAIN);
                } else {
                    switchView(Screen.DENIED_PERMISSION);
                }
            }
        }
    }

    private void switchView(Screen screen) {
        switch (screen) {
            case MAIN:
                viewFlipper.setDisplayedChild(0);
                break;
            case DENIED_PERMISSION:
                viewFlipper.setDisplayedChild(1);
                break;
        }
    }
}
