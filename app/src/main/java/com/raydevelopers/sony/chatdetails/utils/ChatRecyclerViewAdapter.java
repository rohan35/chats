package com.raydevelopers.sony.chatdetails.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;

import com.raydevelopers.sony.chatdetails.R;

/**
 * Created by SONY on 16-10-2017.
 */

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {
    private Cursor mCursor;
    private Context mContext;

public ChatRecyclerViewAdapter(Context c,Cursor cursor)
{
    mCursor=cursor;
    mContext=c;
}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
View v= LayoutInflater.from(mContext).inflate(R.layout.chat_rv_layout,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
mCursor.moveToPosition(position);
        holder.name.setText(mCursor.getString(1));
    }

    @Override
    public int getItemCount() {
        if(mCursor.getCount()>0)
            return mCursor.getCount();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);

        }
    }
}
