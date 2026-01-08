package vn.iotstar.services.impl;

import java.util.List;

import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.impl.CategoryDaoImpl;
import vn.iotstar.entity.Category;
import vn.iotstar.services.CategoryService;

public class CategoryServiceImpl implements CategoryService {
	
	CategoryDao cateDao = new CategoryDaoImpl();

	@Override
	public List<Category> findAll() {
		return cateDao.findAll();
	}
	

}
