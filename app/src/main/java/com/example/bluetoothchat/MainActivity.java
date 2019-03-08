package com.example.bluetoothchat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private enum Screen {
        MAIN,
        CHAT,
        DENIED_PERMISSION
    }
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_CODE = 0;

    private ViewFlipper viewFlipper;
    private EditText editText;
    private ImageButton sendBtn;

    private MessageAdapter messageAdapter;
    private ListView messagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewFlipper = findViewById(R.id.view_flipper);
        editText = findViewById(R.id.editText);

        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this::sendMessage);

        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        Button allowLocationBtn = findViewById(R.id.allow_location);
        allowLocationBtn.setOnClickListener(this::allowLocation);

        requestPermissions(PERMISSIONS, REQUEST_CODE);
        selectScreen(Screen.CHAT);
    }

    public void onReceiveMessage(final String msg) {
        // since the message body is a simple string in our case we can use json.asText() to parse it as such
        // if it was instead an object we could use a similar pattern to data parsing
        final Message message = new Message(msg, new MemberData("Someone else", "#FF0000"), false);
        runOnUiThread(() -> {
            messageAdapter.add(message);
            messagesView.setSelection(messagesView.getCount() - 1);
        });
    }



    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            runOnUiThread(() -> {
                System.out.println("hello");
                messageAdapter.add(new Message(message, new MemberData("Me", "#00FF00"), true));
                messagesView.setSelection(messagesView.getCount() - 1);
            });

            editText.getText().clear();
        }

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        onReceiveMessage(message + "? What do you mean?");
                    }
                },
                1000
        );
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
                    selectScreen(Screen.CHAT);
                } else {
                    selectScreen(Screen.DENIED_PERMISSION);
                }
            }
        }
    }

    private void selectScreen(Screen screen) {
        switch (screen) {
            case MAIN:
                viewFlipper.setDisplayedChild(0);
                break;
            case CHAT:
                viewFlipper.setDisplayedChild(1);
                break;
            case DENIED_PERMISSION:
                viewFlipper.setDisplayedChild(2);
                break;
        }
    }
}
