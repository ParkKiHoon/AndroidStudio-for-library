package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewActivity extends Activity {
    TextView txtText;
    RecyclerView recyclerView;
    Button bt_review;
    ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.review_dialog);
        recyclerView=findViewById(R.id.review_dialog);
        Intent intent =getIntent();
        ArrayList<String> nickname=intent.getStringArrayListExtra("nickname");
        ArrayList<String> content=intent.getStringArrayListExtra("content");
        ArrayList<String> rating=intent.getStringArrayListExtra("rating");

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.review_dialog) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext())) ;
        ArrayList<ReviewData> list = new ArrayList<>();
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new ReviewAdapter(list) ;
        recyclerView.setAdapter(adapter) ;
        adapter.clear();
        for(int i=0;i<nickname.size();i++){
            if(!content.get(i).equals("")){
                adapter.addItem(new ReviewData(nickname.get(i)+"(★"+rating.get(i)+")",content.get(i)));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void mOnClose(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

}
