package com.example.tuantc;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataLocalManager {
    private static final String PREF_NAME = "VIMEN_STORE_PREFS";
    private static final String KEY_FAV = "KEY_FAVORITE";
    private static final String KEY_CART = "KEY_CART";
    private static DataLocalManager instance;
    private SharedPreferences sharedPreferences;

    public static void init(Context context) {
        if (instance == null) {
            instance = new DataLocalManager();
            instance.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    public static DataLocalManager getInstance() {
        if (instance == null) {
            // Tránh trường hợp gọi getInstance mà chưa init
            throw new RuntimeException("DataLocalManager must be initialized in Application or HomeActivity");
        }
        return instance;
    }

    // Hàm lưu danh sách xuống máy
    public void saveFavoriteList(String userKey, List<Product> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        sharedPreferences.edit().putString(KEY_FAV + "_" + userKey, json).apply();
    }

    // Hàm đọc danh sách từ máy lên
    public List<Product> getFavoriteList(String userKey) {
        String json = sharedPreferences.getString(KEY_FAV + "_" + userKey, null);
        if (json == null) return new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Product>>() {}.getType();
        return gson.fromJson(json, type);
    }
    // Hàm lưu giỏ hàng
    public void saveCartList(String userKey, List<Product> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        sharedPreferences.edit().putString(KEY_CART + "_" + userKey, json).apply();
    }

    // Hàm đọc giỏ hàng
    public List<Product> getCartList(String userKey) {
        String json = sharedPreferences.getString(KEY_CART + "_" + userKey, null);
        if (json == null) return new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Product>>() {}.getType();
        return gson.fromJson(json, type);
    }
}