package com.unity3d.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ai_Frame_Main extends Activity implements IUnityPlayerLifecycleEvents
{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    FrameLayout fl_forUnity3;
    public static Context mContext;
    private String[] FrameNames ;
    private String[] FrameValue ;
    private String[] FrameId ;
    float rate=0;
    int temp_val=0;
    private ArrayList<ArrayList<String>> review_nickname=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> review_content=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> review_rating=new ArrayList<ArrayList<String>>();

    //Main xml
    Button connect_btn; // ip 받아오는 버튼
    Button getresult_btn;
    TextView show_text; // 서버에서온거 보여주는 에디트
    TextView ai_text,tv6;
    TextView ratingBar;
    RatingBar ratingBar2;
    RatingBar ratingBar5,ratingBar6,ratingBar7;

    TextView rating_score;
    TextView tv_ai_main,tv_ai_main2;
    TextView tv_nick_main,tv_nick_main2;
    ImageView product_image;
    View line1;
    TextView tv7,tv8;
    //Sub xml
    TextView tv11,tv12,tv13;
    EditText et3;
    Button btn_ai_sub7,btn_ai_sub8;
    View line4;
    ImageView iv4,iv5,finish;
    TextView iv6;

    // 소켓통신에 필요한것
    private String html = "";
    private Handler mHandler;

    private Socket socket;

    private DataOutputStream dos;
    private DataInputStream dis;

    private int port = 8000; // port 번호
    private String[][] sendMsg = new String[5][2];;
    private static String STOP_MSG = "stop";
    private float rating_now;
    private String gotFrame = null;
    private int numOfProduct = 0;
    private int currentnumOfProduct  = 0;
    private String[] resultNames = new String[5];
    private String ThisType = "";
    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(this);
        if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
        {
            setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
            getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //setContentView(mUnityPlayer);
        //Set the content to main
        setContentView(R.layout.activity_ai_main);
        Intent intent =getIntent();
        FrameNames=intent.getStringArrayExtra("name");
        FrameId=intent.getStringArrayExtra("id");
        FrameValue=intent.getStringArrayExtra("value");
        ThisType = intent.getStringExtra("type");
        for(int i=0;i<5;i++) {
            review_nickname.add(intent.getStringArrayListExtra("nickname"+i));
            review_content.add(intent.getStringArrayListExtra("content"+i));
            review_rating.add(intent.getStringArrayListExtra("rating"+i));
        }
        //Inflate the frame layout from XML
        this.fl_forUnity3 = (FrameLayout)findViewById(R.id.fl_forUnity3);
        //Add the mUnityPlayer view to the FrameLayout, and set it to fill all the area available
        this.fl_forUnity3.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mContext=this;
        mUnityPlayer.requestFocus();
        //------------------------------------------------------------------------------------------------------------------------------------------------
        // Main xml
        connect_btn = (Button)findViewById(R.id.connect_btn);
        ai_text=findViewById(R.id.ai_text);
        tv6=findViewById(R.id.tv0);
        line1=findViewById(R.id.Line1);
        getresult_btn = (Button)findViewById(R.id.get_result_btn);
        show_text = (TextView)findViewById(R.id.tv2);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar2 = findViewById(R.id.ratingBar2);
        ratingBar5 = findViewById(R.id.ratingBar5);
        ratingBar6 = findViewById(R.id.ratingBar6);
        ratingBar7 = findViewById(R.id.ratingBar7);
        finish=findViewById(R.id.ai_finish);
        // Sub xml
        tv11=findViewById(R.id.tv11);
        tv12=findViewById(R.id.tv12);
        tv13=findViewById(R.id.tv13);
        et3=findViewById(R.id.et3);
        btn_ai_sub7=findViewById(R.id.btn_ai_sub7);
        btn_ai_sub8=findViewById(R.id.btn_ai_sub8);
        line4=findViewById(R.id.line4);
        iv4=findViewById(R.id.imageView4);
        iv5=findViewById(R.id.imageView5);
        iv6=findViewById(R.id.ratingBar3);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_now = rating;
            }
        });
        tv_ai_main=findViewById(R.id.tv4);
        tv_ai_main2=findViewById(R.id.tv3);
        tv_nick_main=findViewById(R.id.tv6);
        tv_nick_main2=findViewById(R.id.tv5);
        tv7=findViewById(R.id.tv7);
        tv8=findViewById(R.id.tv8);


        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putStringArrayListExtra("nickname",review_nickname.get(currentnumOfProduct));
                intent.putStringArrayListExtra("content",review_content.get(currentnumOfProduct));
                intent.putStringArrayListExtra("rating",review_rating.get(currentnumOfProduct));
                startActivity(intent);
            }
        });

        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingSecond();
            }
        });

        getresult_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg[currentnumOfProduct][0] = FrameNames[currentnumOfProduct];
                sendMsg[currentnumOfProduct][1] = rating_now+"";
                connect();
                ai_text.setText("결과");
                //Sub xml
                tv11.setVisibility(View.VISIBLE);
                tv12.setVisibility(View.VISIBLE);
                tv13.setVisibility(View.VISIBLE);
                et3.setVisibility(View.VISIBLE);
                btn_ai_sub7.setVisibility(View.VISIBLE);
                btn_ai_sub8.setVisibility(View.VISIBLE);
                line4.setVisibility(View.VISIBLE);

                //Main xml
                show_text.setVisibility(View.GONE);
                tv_ai_main.setVisibility(View.GONE);
                tv_ai_main2.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                getresult_btn.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                ratingBar.setVisibility(View.GONE);
                ratingBar2.setVisibility(View.GONE);
                tv_nick_main.setVisibility(View.GONE);
                tv_nick_main2.setVisibility(View.GONE);
                tv_ai_main.setVisibility(View.GONE);
                tv_ai_main2.setVisibility(View.GONE);
                ratingBar5.setVisibility(View.GONE);
                ratingBar6.setVisibility(View.GONE);
                ratingBar7.setVisibility(View.GONE);
                tv7.setVisibility(View.GONE);
                tv8.setVisibility(View.GONE);
                iv4.setVisibility(View.GONE);
                iv5.setVisibility(View.GONE);
                iv6.setVisibility(View.GONE);

            }
        });
        btn_ai_sub8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("content",et3.getText().toString());
                intent.putExtra("rating",rate);
                intent.putExtra("id",FrameId[temp_val]);
                Ai_Frame_Main.this.setResult(1101, intent);
                Ai_Frame_Main.this.finish();
            }
        });

        btn_ai_sub7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Ai_Frame_Main.this.setResult(1102, intent);
                Ai_Frame_Main.this.finish();
            }
        });
        SettingFirst();
    }




    void SettingFirst(){

        int temp =currentnumOfProduct + 1;
        ai_text.setText( temp +" / 5");
        Log.d("unityV",FrameValue[0]+" unity");
        UnityPlayer.UnitySendMessage("GameManager", "ShowForAi", FrameValue[0]);
        {
            List<Integer> list=GetRandom();
            if(list.get(0)==-999 && list.get(1)==-999){
                ratingBar5.setRating(0);
                tv_ai_main.setText("아직 리뷰가 없습니다.");
                tv_nick_main.setText("");
                ratingBar6.setRating(0);
                tv_ai_main2.setText("아직 리뷰가 없습니다.");
                tv_nick_main2.setText("");
            }
            else if(list.get(0)!=-999 && list.get(1)==-999)
            {
                ratingBar5.setRating(Float.parseFloat(review_rating.get(currentnumOfProduct).get(list.get(0))));
                tv_ai_main.setText(review_content.get(currentnumOfProduct).get(list.get(0)));
                tv_nick_main.setText(review_nickname.get(currentnumOfProduct).get(list.get(0)));
                ratingBar6.setRating(0);
                tv_ai_main2.setText("아직 리뷰가 없습니다.");
                tv_nick_main2.setText("");
            }
            else {
                ratingBar5.setRating(Float.parseFloat(review_rating.get(currentnumOfProduct).get(list.get(0))));
                tv_ai_main.setText(review_content.get(currentnumOfProduct).get(list.get(0)));
                tv_nick_main.setText(review_nickname.get(currentnumOfProduct).get(list.get(0)));
                ratingBar6.setRating(Float.parseFloat(review_rating.get(currentnumOfProduct).get(list.get(1))));
                tv_ai_main2.setText(review_content.get(currentnumOfProduct).get(list.get(1)));
                tv_nick_main2.setText(review_nickname.get(currentnumOfProduct).get(list.get(1)));
            }
        }
        float temp_rating=0;
        for(int i=0;i<review_rating.get(0).size();i++){
            temp_rating +=Float.parseFloat(review_rating.get(0).get(i));
        }
        temp_rating=temp_rating/review_rating.get(0).size();
        ratingBar2.setRating(temp_rating);
        ratingBar7.setRating(temp_rating);
        tv8.setText(Integer.toString(review_nickname.get(currentnumOfProduct).size())+" review");
        ratingBar.setText(FrameNames[0]);
        TextView showmyTr = findViewById(R.id.tv2);
        temp_rating = Float.parseFloat(String.format("%.1f", temp_rating));
        tv7.setText(Float.toString(temp_rating));
        showmyTr.setText(FrameValue[0].split("/")[1]+"kg | "+FrameValue[0].split("/")[2]+" ₩");
    }



    void SettingSecond(){
        sendMsg[currentnumOfProduct][0] = FrameNames[currentnumOfProduct];
        sendMsg[currentnumOfProduct][1] = rating_now+"";
        currentnumOfProduct++;
                int temp = currentnumOfProduct + 1;
        ai_text.setText( temp +" / 5");
        UnityPlayer.UnitySendMessage("GameManager", "ShowForAi", FrameValue[currentnumOfProduct]);
        UnityPlayer.UnitySendMessage("GameManager", "ShowForAi", FrameValue[currentnumOfProduct]);

        {
            List<Integer> list=GetRandom();
            if(list.get(0)==-999 && list.get(1)==-999){
                ratingBar5.setRating(0);
                tv_ai_main.setText("아직 리뷰가 없습니다.");
                tv_nick_main.setText("");
                ratingBar6.setRating(0);
                tv_ai_main2.setText("아직 리뷰가 없습니다.");
                tv_nick_main2.setText("");
            }
            else if(list.get(0)!=-999 && list.get(1)==-999)
            {
                ratingBar5.setRating(Float.parseFloat(review_rating.get(currentnumOfProduct).get(list.get(0))));
                tv_ai_main.setText(review_content.get(currentnumOfProduct).get(list.get(0)));
                tv_nick_main.setText(review_nickname.get(currentnumOfProduct).get(list.get(0)));
                ratingBar6.setRating(0);
                tv_ai_main2.setText("아직 리뷰가 없습니다.");
                tv_nick_main2.setText("");
            }
            else {
                ratingBar5.setRating(Float.parseFloat(review_rating.get(currentnumOfProduct).get(list.get(0))));
                tv_ai_main.setText(review_content.get(currentnumOfProduct).get(list.get(0)));
                tv_nick_main.setText(review_nickname.get(currentnumOfProduct).get(list.get(0)));
                ratingBar6.setRating(Float.parseFloat(review_rating.get(currentnumOfProduct).get(list.get(1))));
                tv_ai_main2.setText(review_content.get(currentnumOfProduct).get(list.get(1)));
                tv_nick_main2.setText(review_nickname.get(currentnumOfProduct).get(list.get(1)));
            }
        }
        float temp_rating=0;
        for(int i=0;i<review_rating.get(currentnumOfProduct).size();i++){
            temp_rating +=Float.parseFloat(review_rating.get(currentnumOfProduct).get(i));
        }
        temp_rating=temp_rating/review_rating.get(currentnumOfProduct).size();
        ratingBar2.setRating(temp_rating);
        ratingBar7.setRating(temp_rating);
        tv8.setText(Integer.toString(review_nickname.get(currentnumOfProduct).size())+" review");
        if(temp==5){
            connect_btn.setVisibility(View.GONE);
            getresult_btn.setVisibility(View.VISIBLE);
        }
        ratingBar.setText(FrameNames[currentnumOfProduct]);
        TextView showmyTr = findViewById(R.id.tv2);
        temp_rating = Float.parseFloat(String.format("%.1f", temp_rating));
        tv7.setText(Float.toString(temp_rating));
        showmyTr.setText(FrameValue[currentnumOfProduct].split("/")[1]+"kg | "+FrameValue[currentnumOfProduct].split("/")[2]+" ₩");

    }



    List<Integer> GetRandom(){
        int a[] = new int[2];
        int temp_int=0;
        for(int i=0; i<review_nickname.get(currentnumOfProduct).size();i++)
        {
            if(!review_content.get(currentnumOfProduct).get(i).equals("")){
                if(temp_int==2)
                {
                    List<Integer> list = new ArrayList<>();
                    for (int value : a) {
                        list.add(value);
                    }
                    return list;
                }
                a[temp_int]=i;
                temp_int++;
            }
        }
        if(temp_int==1){
            a[1]=-999;
            List<Integer> list = new ArrayList<>();
            for (int value : a) {
                list.add(value);
            }
            return list;
        }

        if(temp_int==0){
            a[0]=-999;
            a[1]=-999;
            List<Integer> list = new ArrayList<>();
            for (int value : a) {
                list.add(value);
            }
            return list;
        }
        Random r = new Random();
        for(int i=0;i<=1;i++)
        {
            Log.d("first",review_nickname.get(0).toString());
            Log.d("first",review_nickname.get(1).toString());
            Log.d("first",currentnumOfProduct+" num");
            a[i] = r.nextInt(review_nickname.get(currentnumOfProduct).size());
            if(review_nickname.get(currentnumOfProduct).size() == 1) break;

            for(int j=0;j<i;j++) {
                if(a[i]==a[j]) {
                    i--;

                }

            }
        }
        List<Integer> list = new ArrayList<>();
        for (int value : a) {
            list.add(value);
        }
        return list;
    }



    // 로그인 정보 db에 넣어주고 연결시켜야 함.
    void connect(){
        mHandler = new Handler();
        Log.w("connect","연결 하는중");
// 받아오는거
        Thread checkUpdate = new Thread() {
            public void run() {
// ip받기
                String newip = String.valueOf("34.64.251.207");

// 서버 접속
                try {
                    socket = new Socket(newip, port);
                    Log.w("서버 접속됨", "서버 접속됨");
                } catch (IOException e1) {
                    Log.w("서버접속못함", "서버접속못함");
                    e1.printStackTrace();
                }

                Log.w("edit 넘어가야 할 값 : ","안드로이드에서 서버로 연결요청");
                Log.d("tags",sendMsg[0][0] + " " + sendMsg[0][1]);
                Log.d("tags",sendMsg[1][0] + " " + sendMsg[1][1]);
                Log.d("tags",sendMsg[2][0] + " " + sendMsg[2][1]);
                Log.d("tags",sendMsg[3][0] + " " + sendMsg[3][1]);
                Log.d("tags",sendMsg[4][0] + " " + sendMsg[4][1]);
                /*
                sendMsg = new String[3][2];
                sendMsg[0][0] = "스페셜라이즈드 프레임";
                sendMsg[0][1] = "3.4";
                sendMsg[1][0] = "아르곤 18 갈리움 cs";
                sendMsg[1][1] = "1.1";
                sendMsg[2][0] = "엘파마 레이다 로드";
                sendMsg[2][1] = "1.3";
                */
                try {
                    dos = new DataOutputStream(socket.getOutputStream()); // output에 보낼꺼 넣음
                    dis = new DataInputStream(socket.getInputStream()); // input에 받을꺼 넣어짐
                    dos.writeUTF(ThisType);
                    for(int i=0 ; i<5 ; i++){
                        byte[] buf = new byte[100];
                        int read_Byte = 0;
                        String line= "";


                        read_Byte = dis.read(buf);
                        line = new String(buf, 0, read_Byte);

                        Log.d("next1",line+"");
                        if(line.equals("next")){
                            dos.writeUTF(sendMsg[i][0]);
                            Log.d("msg1",sendMsg[i][0]);
                            read_Byte = dis.read(buf);
                            line = new String(buf, 0, read_Byte);
                        }
                        Log.d("next2",line+"");
                        if(line.equals("next")){
                            sleep(2);
                            dos.writeUTF(sendMsg[i][1]);
                            Log.d("msg2",sendMsg[i][1]);
                            sleep(2);
                        }

                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨");
                }
                Log.w("버퍼","버퍼생성 잘됨");

// 서버에서 계속 받아옴 - 한번은 문자, 한번은 숫자를 읽음. 순서 맞춰줘야 함.
                try {
                    String line = "";
                    Log.w("DebugA","A");
                    byte[] buf = new byte[100];
                    int read_Byte = dis.read(buf);
                    line = new String(buf, 0, read_Byte);
                    resultNames[0] = line;
                    Log.w("DebugA","AAA");
                    Log.w("DebugA",resultNames[0]);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=0;i<5;i++){
                                if(sendMsg[i][0].equals(resultNames[0])) {
                                    temp_val=i;
                                    rate=Float.parseFloat(sendMsg[i][1]);
                                }
                            }
                            tv13.setText(FrameNames[temp_val]+"\n"+FrameValue[temp_val].split("/")[1]+"kg | "+FrameValue[temp_val].split("/")[2]+" ₩");
                            UnityPlayer.UnitySendMessage("GameManager", "ShowForAi", FrameValue[temp_val]);
                            UnityPlayer.UnitySendMessage("GameManager", "ShowForAi", FrameValue[temp_val]);
                        }
                    });


                }catch (Exception e){
                    Log.d("error",e.toString());
                }
            }
        };
// 소켓 접속 시도, 버퍼생성
        checkUpdate.start();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    // When Unity player unloaded move task to background
    @Override public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    // Callback before Unity player process is killed
    @Override public void onUnityPlayerQuitted() {
    }



    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }return mUnityPlayer.injectEvent(event);
    }
}
