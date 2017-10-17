package com.raydevelopers.sony.chatdetails;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.raydevelopers.sony.chatdetails.data.TaskUpdateService;
import com.raydevelopers.sony.chatdetails.data.WatsappContract;
import com.raydevelopers.sony.chatdetails.utils.ChatRecyclerViewAdapter;

/**
 * Created by SONY on 16-10-2017.
 */

public class ChatInflater extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    RecyclerView recyclerView;
    ChatRecyclerViewAdapter mAdapter;
    String projection[]={
      WatsappContract.UserEnrty._ID,
      WatsappContract.UserEnrty.COULMN_UNAME,
            WatsappContract.UserEnrty.COULMN_UCONTACT,
            WatsappContract.UserEnrty.COULMN_TEXT,
            WatsappContract.UserEnrty.COULMN_TIME
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        recyclerView=(RecyclerView)findViewById(R.id.chat_rv);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN |
                ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//Remove swiped item from list and notify the RecyclerView
                final int position = viewHolder.getAdapterPosition();
                mAdapter.notifyItemRemoved(position);
                //start backend service
                //TaskUpdateService.deleteTask(this, );

            }
        };
        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView);
        getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri= WatsappContract.UserEnrty.CONTENT_URI;
        CursorLoader cl=new CursorLoader(this,uri,projection,null,null,null);
        return  cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        System.out.println(DatabaseUtils.dumpCursorToString(data));
        mAdapter=new ChatRecyclerViewAdapter(this,data);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }
}
