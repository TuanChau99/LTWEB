package vn.iotstar.daos;

import java.util.List;   // ✅ đúng import

import vn.iotstar.models.Category;

public interface CategoryDao {
    List<Category> getAll();                // ✅
    Category get(int id);                   // ✅
    void insert(Category category);         // ✅
    void update(Category category);         // ✅
    void delete(int id);                    // ✅
    int count();                            // ✅
    List<Category> paging(int page, int pageSize); // ✅
}

