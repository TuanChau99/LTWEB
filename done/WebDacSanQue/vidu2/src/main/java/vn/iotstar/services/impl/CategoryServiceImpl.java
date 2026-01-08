package vn.iotstar.services.impl;

import java.util.List;

import vn.iotstar.daos.CategoryDao;
import vn.iotstar.daos.impl.CategoryDaoImpl;
import vn.iotstar.models.Category;
import vn.iotstar.services.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao cateDao = new CategoryDaoImpl();

    @Override
    public List<Category> getAll() {
        return cateDao.getAll();
    }

    @Override
    public Category get(int id) {
        return cateDao.get(id);
    }

    @Override
    public void insert(Category category) {
        cateDao.insert(category);
    }

    @Override
    public void update(Category category) {
        cateDao.update(category);
    }

    @Override
    public void delete(int id) {
        cateDao.delete(id);
    }

    @Override
    public int count() {
        return cateDao.count();
    }

    @Override
    public List<Category> paging(int page, int pageSize) {
        return cateDao.paging(page, pageSize);
    }

    @Override
    public List<Category> findAll() {
        return cateDao.getAll(); // ✅ sửa lỗi: không return null nữa
    }
}
