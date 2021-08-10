package com.unity3d.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class AiSub extends AppCompatActivity implements Serializable {
    TextView tv_ai_sub;
    ImageView show_image1;
    Button btn_ai_sub,btn_ai_sub2;
    EditText et_ai_sub;
    String cur_parts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_sub);

        Intent myintent = getIntent();
        String firstName = myintent.getStringExtra("1등이름");
        float rating =myintent.getFloatExtra("rate",2.5f);


        btn_ai_sub=findViewById(R.id.btn_ai_sub);
        btn_ai_sub2=findViewById(R.id.btn_ai_sub2);
        et_ai_sub=findViewById(R.id.et1);

        btn_ai_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("content",et_ai_sub.getText().toString());
                map.put("rating",rating);
            }
        });

        btn_ai_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AiMain.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
