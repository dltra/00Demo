package com.traf1.demo;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button submitButton;
    EditText responseText;
    TextView displayText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitButton=findViewById(R.id.clickButton);
        responseText=findViewById(R.id.responseEditText);
        displayText=findViewById(R.id.textBox);
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
    public void submit(View view) {//process button onClick event
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
}