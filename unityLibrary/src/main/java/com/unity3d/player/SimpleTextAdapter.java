package com.unity3d.player;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder>  {

    int cur_state;
    public ArrayList<BikeData> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        TextView textStock ;
        ImageView imageView;
        TextView textView2;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textStock=itemView.findViewById(R.id.text_price);
            textView1 = itemView.findViewById(R.id.review_tv) ;
            textView2 = itemView.findViewById(R.id.review_tv2) ;
            imageView=itemView.findViewById(R.id.review_iv);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    SimpleTextAdapter(ArrayList<BikeData> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView1.setText(mData.get(position).getname()) ;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(25));
        Glide.with(holder.itemView.getContext()).load(mData.get(position).getimage()).apply(requestOptions)
                .into(holder.imageView);
        String[] splitText = mData.get(position).getpart().split("/");
        holder.textStock.setText(splitText[2]+"₩");
        if(mData.get(position).getstock().equals("")){
            holder.textView2.setText(splitText[1]+"kg");
        }
        else{
            holder.textView2.setText(splitText[1]+"kg / "+mData.get(position).getstock());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[cur_state]==0) {
                    if(cur_state==0){
                        Log.d("here", Integer.toString(((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[cur_state]));
                        ((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[cur_state] = 1;
                        ((UnityPlayerActivity) UnityPlayerActivity.mContext).SendToUnity(mData.get(position).getpart());
                    }
                    else {
                        if (((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[0] != 0) {
                            Log.d("here", Integer.toString(((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[cur_state]));
                            ((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[cur_state] = 1;
                            ((UnityPlayerActivity) UnityPlayerActivity.mContext).SendToUnity(mData.get(position).getpart());
                        }
                    }
                }
                else{
                    if(cur_state==0){
                        for(int i=1;i<5;i++)
                            ((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[i]=0;
                    }
                    Log.d("here", Integer.toString(((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[cur_state]));
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).SendToUnity(mData.get(position).getpart());
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).SendToUnity(mData.get(position).getpart());
                }
                ((UnityPlayerActivity) UnityPlayerActivity.mContext).ButtonState();
                if(mData.get(position).getname().equals(((UnityPlayerActivity) UnityPlayerActivity.mContext).cur_name))
                {
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).cur_name[cur_state]="";
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).cur_part[cur_state]="";
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).cur_image[cur_state]="";
                }
                else{
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).cur_name[cur_state]=mData.get(position).getname();
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).cur_part[cur_state]=mData.get(position).getpart();
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).cur_image[cur_state]=mData.get(position).getimage();
                }
                if(((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[0]==1 &&
                        ((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[1]==1 &&
                        ((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[2]==1 &&
                        ((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[3]==1 &&
                        ((UnityPlayerActivity) UnityPlayerActivity.mContext).btn_state[4]==1){
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).bt_save.setVisibility(View.VISIBLE);
                }
                else{
                    ((UnityPlayerActivity) UnityPlayerActivity.mContext).bt_save.setVisibility(View.INVISIBLE);
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