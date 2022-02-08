package com.bothofus.myaudioandvideoapp.persistence;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.util.Objects;



public class Provider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.bothofus.myaudioandvideoapp/items");
    private static final String KEY_ID = "_id";
    public static final String KEY_TYPE = "item_type";
    public static final String KEY_DATE_TAKEN = "date_taken";
    public static final String KEY_DATE_MODIFIED = "date_modified";
    public static final String KEY_NAME = "item_name";
    public static final String KEY_LINK = "item_link";
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final UriMatcher uriMatcher;
    private DataB datab;

    @Override
    public boolean onCreate() {
        datab = new DataB(getContext(), DataB.DATABASE_NAME, null, DataB.DATABASE_VERSION);

        return true;
    }

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.bothofus.myaudioandvideoapp", "items", ALLROWS);
        uriMatcher.addURI("com.bothofus.myaudioandvideoapp", "items/#", SINGLE_ROW);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.com.bothofus.myaudioandvideoapp";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.com.bothofus.myaudioandvideoapp";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Open a read-only database.
        SQLiteDatabase db = datab.getWritableDatabase();

        // Replace these with valid SQL statements if necessary.
        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DataB.DATABASE_TABLE);

        // If this is a row query, limit the result set to the passed in row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowID);
            default:
                break;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Open a read / write database to support the transaction.
        SQLiteDatabase db = datab.getWritableDatabase();

        // If this is a row URI, limit the deletion to the specified row.
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
            default:
                break;
        }
        if (selection == null)
            selection = "1";

        // Execute the deletion.
        int deleteCount = db.delete(DataB.DATABASE_TABLE, selection, selectionArgs);

        // Notify any observers of the change in the data set.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return deleteCount;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = datab.getWritableDatabase();

        String nullColumnHack = null;

        // Insert the values into the table
        long id = db.insert(DataB.DATABASE_TABLE, null, values);

        if (id > -1) {
            // Construct and return the URI of the newly inserted row.
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);

            // Notify any observers of the change in the data set.
            getContext().getContentResolver().notifyChange(insertedId, null);

            return insertedId;
        } else
            return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = datab.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
            default:
                break;
        }

        // Perform the update.
        int updateCount = db.update(DataB.DATABASE_TABLE, values, selection, selectionArgs);

        // Notify any observers of the change in the data set.
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    private static class DataB extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "data";
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_TABLE = "ItemTable";

        public DataB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // SQL statement to create a new database.
        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE + " (" + KEY_ID +
                " integer primary key autoincrement, " +
                KEY_TYPE + " text not null, " +
                KEY_DATE_TAKEN + " text, " +
                KEY_DATE_MODIFIED + " text, " +
                KEY_NAME + " text not null, " +
                KEY_LINK + " text not null);";

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF  EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }


    }


}