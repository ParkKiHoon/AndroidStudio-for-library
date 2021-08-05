package com.unity3d.player;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder>  {

    int cur_state;
    public ArrayList<BikeData> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        TextView textStock ;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textStock=itemView.findViewById(R.id.text_stock);
            textView1 = itemView.findViewById(R.id.text1) ;
            imageView=itemView.findViewById(R.id.imageView);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    SimpleTextAdapter(ArrayList<BikeData> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SimpleTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        SimpleTextAdapter.ViewHolder vh = new SimpleTextAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SimpleTextAdapter.ViewHolder holder, int position) {
        holder.textView1.setText(mData.get(position).getname()) ;
        Glide.with(holder.itemView.getContext()).load(mData.get(position).getimage()).into(holder.imageView);
        holder.textStock.setText(mData.get(position).getstock());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((UnityPlayerActivity)UnityPlayerActivity.mContext).btn_state[cur_state]==0) {
                    Log.d("here", Integer.toString(((UnityPlayerActivity)UnityPlayerActivity.mContext).btn_state[cur_state]));
                    ((UnityPlayerActivity)UnityPlayerActivity.mContext).btn_state[cur_state]=1;
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).SendToUnity(mData.get(position).getpart());
                }
                else{
                    if(cur_state==0){
                        for(int i=1;i<5;i++)
                            ((UnityPlayerActivity)UnityPlayerActivity.mContext).btn_state[i]=0;
                    }
                    Log.d("here", Integer.toString(((UnityPlayerActivity)UnityPlayerActivity.mContext).btn_state[cur_state]));
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).SendToUnity(mData.get(position).getpart());
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).SendToUnity(mData.get(position).getpart());
                }
                ((UnityPlayerActivity)UnityPlayerActivity.mContext).ButtonState();
                if(mData.get(position).getname().equals(((UnityPlayerActivity)UnityPlayerActivity.mContext).cur_name))
                {
                    ((UnityPlayerActivity)UnityPlayerActivity.mContext).cur_name[cur_state]="";
                    ((UnityPlayerActivity)UnityPlayerActivity.mContext).cur_part[cur_state]="";
                    ((UnityPlayerActivity)UnityPlayerActivity.mContext).cur_image[cur_state]="";
                }
                else{
                    ((UnityPlayerActivity)UnityPlayerActivity.mContext).cur_name[cur_state]=mData.get(position).getname();
                    ((UnityPlayerActivity)UnityPlayerActivity.mContext).cur_part[cur_state]=mData.get(position).getpart();
                    ((UnityPlayerActivity)UnityPlayerActivity.mContext).cur_image[cur_state]=mData.get(position).getimage();
                }
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void clear(){
        mData.clear();
    }

    public void addItem(BikeData data){
        mData.add(data);
    }

    public void addStock(String string,int position){
        mData.get(position).setstock(string);
        notifyItemChanged(position);
    }


}