package com.raydevelopers.sony.chatdetails.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.raydevelopers.sony.chatdetails.ChatInflater;
import com.raydevelopers.sony.chatdetails.MainActivity;
import com.raydevelopers.sony.chatdetails.R;
import com.raydevelopers.sony.chatdetails.data.WatsappContract;
import com.raydevelopers.sony.chatdetails.utils.ChatRecyclerViewAdapter;

/**
 * Created by SONY on 16-10-2017.
 */

public class ChatHeadService extends Service implements Loader.OnLoadCompleteListener<Cursor> {
    private WindowManager mWindowManager;
    private View mChatHeadView;
    RecyclerView recyclerView;
    ChatRecyclerViewAdapter mAdapter;
    private CursorLoader mCursorLoader;
    Uri uri= WatsappContract.UserEnrty.CONTENT_URI;
    String projection[]={
            WatsappContract.UserEnrty._ID,
            WatsappContract.UserEnrty.COULMN_UNAME,
            WatsappContract.UserEnrty.COULMN_UCONTACT,
            WatsappContract.UserEnrty.COULMN_TEXT,
            WatsappContract.UserEnrty.COULMN_TIME
    };
    public ChatHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the chat head layout we created

        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.chat_head_layout, null);
        mCursorLoader = new CursorLoader(this, uri, projection, null,null,null);
        mCursorLoader.registerListener(1, this);



        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the chat head position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mChatHeadView, params);
        //The root element of the collapsed view layout
        final View collapsedView = mChatHeadView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        recyclerView=(RecyclerView)mChatHeadView.findViewById(R.id.chat_rv);
        final View expandedView = mChatHeadView.findViewById(R.id.expanded_container);

        //Set the close button.
        ImageView closeButton = (ImageView) mChatHeadView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the service and remove the chat head from the window
                stopSelf();
            }
        });

        //Drag and move chat head using user's touch action.
        final ImageView chatHeadImage = (ImageView) mChatHeadView.findViewById(R.id.chat_head_profile_iv);
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            //Open the chat conversation click.

                            int Xdiff = (int) (event.getRawX() - initialTouchX);
                            int Ydiff = (int) (event.getRawY() - initialTouchY);


                            //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                            //So that is click event.
                            if (Xdiff < 10 && Ydiff < 10) {
                                if (isViewCollapsed()) {
                                    //When user clicks on the image view of the collapsed layout,
                                    //visibility of the collapsed layout will be changed to "View.GONE"
                                    //and expanded view will become visible.
                                    /*Intent intent = new Intent(ChatHeadService.this, ChatInflater.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);*/
                                    collapsedView.setVisibility(View.GONE);
                                    mCursorLoader.startLoading();
                                    expandedView.setVisibility(View.VISIBLE);
                                    //close the service and remove the chat heads 
                                    stopSelf();

                                }
                            }
                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mChatHeadView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
    }

    private boolean isViewCollapsed() {
        return mChatHeadView == null || mChatHeadView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatHeadView != null) mWindowManager.removeView(mChatHeadView);
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        mAdapter=new ChatRecyclerViewAdapter(this,data);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }
}