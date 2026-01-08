package vn.iotstar.services;

import java.util.List;   

import vn.iotstar.models.Category;

public interface CategoryService {
    List<Category> getAll();
    Category get(int id);
    void insert(Category category);
    void update(Category category);
    void delete(int id);
    int count();
    List<Category> paging(int page, int pageSize);
	List<Category> findAll();
}
