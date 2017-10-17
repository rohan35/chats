package com.raydevelopers.sony.chatdetails.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SONY on 16-10-2017.
 */

public class WatsappContract {
    public static final String CONTENT_AUTHORITY="com.raydevelopers.sony.chatdetails";
    public  WatsappContract(){}
    public static final class UserEnrty implements BaseColumns{

        public static final String TABLE_NAME = "user_details";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();


        public static final String COULMN_UNAME="name";
        public static final String COULMN_UCONTACT="number";
        public static final String COULMN_TIME="time";
        public static final String COULMN_TEXT="message";




    }
}
