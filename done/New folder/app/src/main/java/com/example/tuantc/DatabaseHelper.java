package com.example.tuantc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "VimenStore.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_CART = "cart";

    // Các cột
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_DATA = "product_data"; // Lưu JSON của list sản phẩm cho nhanh

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFavTable = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_USER_ID + " TEXT PRIMARY KEY, "
                + COLUMN_DATA + " TEXT)";

        String createCartTable = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_USER_ID + " TEXT PRIMARY KEY, "
                + COLUMN_DATA + " TEXT)";

        db.execSQL(createFavTable);
        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // --- HÀM LƯU/ĐỌC YÊU THÍCH ---
    public void saveFavoriteList(String userKey, List<Product> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userKey);
        values.put(COLUMN_DATA, new Gson().toJson(list));

        // REPLACE giúp chèn mới hoặc đè lên nếu UserID đã tồn tại
        db.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<Product> getFavoriteList(String userKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, new String[]{COLUMN_DATA},
                COLUMN_USER_ID + "=?", new String[]{userKey}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String json = cursor.getString(0);
            cursor.close();
            Type type = new TypeToken<List<Product>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }

    // --- HÀM LƯU/ĐỌC GIỎ HÀNG ---
    public void saveCartList(String userKey, List<Product> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userKey);
        values.put(COLUMN_DATA, new Gson().toJson(list));

        db.insertWithOnConflict(TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<Product> getCartList(String userKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{COLUMN_DATA},
                COLUMN_USER_ID + "=?", new String[]{userKey}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String json = cursor.getString(0);
            cursor.close();
            Type type = new TypeToken<List<Product>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }
    // Hàm xóa sạch giỏ hàng của một User (Dùng khi thanh toán xong)
    public void clearCart(String userKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userKey);
        values.put(COLUMN_DATA, new Gson().toJson(new ArrayList<Product>())); // Ghi đè bằng list rỗng

        db.insertWithOnConflict(TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // Hàm xóa một bảng
    public void deleteUserAllData(String userKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_USER_ID + "=?", new String[]{userKey});
        db.delete(TABLE_FAVORITES, COLUMN_USER_ID + "=?", new String[]{userKey});
    }
}