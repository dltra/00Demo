package com.traf1.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button submitButton;
    EditText responseText, response2Text;
    TextView displayText;
    Timer timer = new Timer();
    SharedPreferences mPreferences;
    int duration = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize preferences
        mPreferences = getSharedPreferences("com.traf1.demo.sharedprefs",MODE_PRIVATE);
        response2Text = findViewById(R.id.response2EditText);
        submitButton=findViewById(R.id.clickButton);
        responseText=findViewById(R.id.responseEditText);
        displayText=findViewById(R.id.textBox);
        //restore preferences into submit button
        submitButton.setText(mPreferences.getString("mResponse","defaultString")
            +mPreferences.getInt("mResponseNum",99));
        responseText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(responseText.getText().toString().equals("TJ")){
                        displayText.setText("TJ Rocks!");
                        responseText.setText("");
                        responseText.setHint("That's a good name.");
                    }
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        //store values into preferences onPause
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("mResponse", responseText.getText().toString());
        preferencesEditor.putInt("mResponseNum", Integer.parseInt(response2Text.getText().toString()));
        preferencesEditor.apply();
    }
    public void submit(View view) {//process button onClick event
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String currentTime = getString(R.string.time,duration++);
                        displayText.setText(currentTime);
                        if(duration>=5){
                            timer.cancel();
                            timer.purge();
                            timer=new Timer();
                            duration=0;
                            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                            intent.putExtra("com.traf1.demo.extra.MESSAGE",displayText.getText().toString());
                            startActivityForResult(intent, 1);
                        }
                    }
                });
            }
        }, 1000, 1000);
        String text = "";
        ArrayList<String> texts = new ArrayList<>();
        try{
            InputStream inputStream = getAssets().open("myTextFile.txt");
            /** For use with raw file
             *  InputStream inputStream = getResources().openRawResource(R.raw.my_text_file);
             */
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            while((text = bufferReader.readLine())!=null) {
                texts.add(text);
            }
        }catch (IOException e){}
        displayText.setText(texts.get(0));
        Toast.makeText(getApplicationContext(),"Done reading.",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 & resultCode==RESULT_OK){
            displayText.setText(data.getStringExtra("com.traf1.demo.extra.REPLY"));
        }
    }
}