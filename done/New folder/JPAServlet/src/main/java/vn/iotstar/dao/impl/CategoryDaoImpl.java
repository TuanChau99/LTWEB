package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import vn.iotstar.configs.JPAConfigs;
import vn.iotstar.dao.CategoryDao;
import vn.iotstar.entity.Category;

public class CategoryDaoImpl implements CategoryDao {
	
	@Override
	public List<Category> findAll() {

		EntityManager enma = JPAConfigs.getEntityManager();

		TypedQuery<Category> query= enma.createNamedQuery("Category.findAll", Category.class);

		//String jpql ="Select c from Category c";

		//TypedQuery<Category> query= enma.createQuery(jpql, Category.class);




		return query.getResultList();



		}

}
