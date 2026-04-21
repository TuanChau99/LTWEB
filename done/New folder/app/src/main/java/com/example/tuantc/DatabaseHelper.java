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
    private static final int DATABASE_VERSION = 2;

    // Tên bảng
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_PRODUCTS = "all_products"; // Bảng chứa danh sách sản phẩm chung

    // Các cột cho Fav và Cart
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_DATA = "product_data";

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

        // Tạo bảng lưu tất cả sản phẩm để lấy ra làm "Sản phẩm liên quan"
        String createProductTable = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + "id TEXT PRIMARY KEY, "
                + "name TEXT, "
                + "price TEXT, "
                + "rating TEXT, "
                + "images TEXT)";

        db.execSQL(createFavTable);
        db.execSQL(createCartTable);
        db.execSQL(createProductTable);

        // Chèn dữ liệu mẫu ngay khi tạo Database
        insertSampleProducts(db);
    }

    private void insertSampleProducts(SQLiteDatabase db) {
        String[][] samples = {
                {"p1", "Áo thun Polo", "250.000", "4.8", "aothun01,aothun02"},
                {"p2", "Giày Da Nâu", "850.000", "5.0", "giay_da_nau,giay_da_den"},
                {"p3", "Thắt lưng da", "120.000", "4.5", "thatlung"},
                {"p4", "Quần Jean Slim", "450.000", "4.7", "aothun01"},
                {"p5", "Ví da cầm tay", "300.000", "4.9", "thatlung"}
        };

        for (String[] s : samples) {
            ContentValues v = new ContentValues();
            v.put("id", s[0]);
            v.put("name", s[1]);
            v.put("price", s[2]);
            v.put("rating", s[3]);
            v.put("images", s[4]);
            db.insert(TABLE_PRODUCTS, null, v);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // --- HÀM LẤY TẤT CẢ SẢN PHẨM ---
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product();
                p.setId(cursor.getString(0));
                p.setName(cursor.getString(1));
                p.setPrice(cursor.getString(2));
                p.setRating(cursor.getString(3));

                // Chuyển chuỗi images (dấu phẩy) về List
                String imgStr = cursor.getString(4);
                ArrayList<String> imgs = new ArrayList<>();
                if (imgStr != null) {
                    for (String s : imgStr.split(",")) imgs.add(s.trim());
                }
                p.setImages(imgs);
                list.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // --- HÀM LƯU/ĐỌC YÊU THÍCH ---
    public void saveFavoriteList(String userKey, List<Product> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userKey);
        values.put(COLUMN_DATA, new Gson().toJson(list));
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

    public void clearCart(String userKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userKey);
        values.put(COLUMN_DATA, new Gson().toJson(new ArrayList<Product>()));
        db.insertWithOnConflict(TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteUserAllData(String userKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_USER_ID + "=?", new String[]{userKey});
        db.delete(TABLE_FAVORITES, COLUMN_USER_ID + "=?", new String[]{userKey});
    }
}