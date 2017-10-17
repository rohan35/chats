package com.raydevelopers.sony.chatdetails.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.raydevelopers.sony.chatdetails.data.WatsappContract.UserEnrty.CONTENT_URI;

/**
 * Created by SONY on 16-10-2017.
 */

public class DetailsProvider extends ContentProvider{
    private static final int TASKS = 100;
    private static final int TASKS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.google.developer.taskmaker/tasks
        sUriMatcher.addURI(WatsappContract.CONTENT_AUTHORITY,
                WatsappContract.UserEnrty.TABLE_NAME,
                TASKS);

        // content://com.google.developer.taskmaker/tasks/id
        sUriMatcher.addURI(WatsappContract.CONTENT_AUTHORITY,
                WatsappContract.UserEnrty.TABLE_NAME + "/#",
                TASKS_WITH_ID);
    }
    private WatsappHelperClass mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new WatsappHelperClass(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //TODO: Implement task query
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            //TODO: Expected "query all" Uri: content://com.google.developer.taskmaker/tasks
            case TASKS:
                cursor = mDbHelper.getReadableDatabase().query(
                        WatsappContract.UserEnrty.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            //TODO: Expected "query one" Uri: content://com.google.developer.taskmaker/tasks/{id}
            case TASKS_WITH_ID:
                cursor = mDbHelper.getReadableDatabase().query(
                        WatsappContract.UserEnrty.TABLE_NAME,
                        projection,
                        WatsappContract.UserEnrty.COULMN_UNAME + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //TODO: Implement new task insert
        long rowID;

        switch (sUriMatcher.match(uri)) {

            case TASKS:
                rowID = mDbHelper.getWritableDatabase().insert(WatsappContract.UserEnrty.TABLE_NAME, null, values);

                break;
            default:
                throw new SQLException("Failed to add a record into " + uri);
                //TODO: Expected Uri: content://com.google.developer.taskmaker/tasks

        }
        if (rowID == -1) {
            Log.e("Insert", "Failed to insert row for " + uri);
            return null;
        }
        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(_uri, null);

        return _uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case TASKS:
                //Rows aren't counted with null selection
                selection = (selection == null) ? "1" : selection;
                break;
            case TASKS_WITH_ID:
                long id = ContentUris.parseId(uri);
                selection = String.format("%s = ?", WatsappContract.UserEnrty.COULMN_UNAME);
                selectionArgs = new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Illegal delete URI");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count = db.delete(WatsappContract.UserEnrty.TABLE_NAME, selection, selectionArgs);

        if (count > 0) {
            //Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //TODO: Implement existing task update
        int numRowsUpdated;
        if (null == selection) selection = "1";
        //TODO: Expected Uri: content://com.google.developer.taskmaker/tasks/{id}
        switch (sUriMatcher.match(uri)) {
            case TASKS_WITH_ID:
                selection = WatsappContract.UserEnrty.COULMN_UNAME + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                numRowsUpdated = mDbHelper.getWritableDatabase().update(WatsappContract.UserEnrty.TABLE_NAME, values,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsUpdated;
    }
}
