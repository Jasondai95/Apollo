package com.school.bookstore.category.service;

import java.util.List;

import com.school.bookstore.book.dao.BookDao;
import com.school.bookstore.category.dao.CategoryDao;
import com.school.bookstore.category.domain.Category;
import com.school.bookstore.category.web.servlet.admin.CategoryException;

/**
 * Created by liangrc on 2015/12/27.
 */
public class CategoryService {

    private CategoryDao categoryDao = new CategoryDao();
    private BookDao bookDao = new BookDao();

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public void add(Category category) {
        categoryDao.add(category);
    }

    public void getCountByCid(String cid) throws CategoryException {
        int count = bookDao.getCountByCid(cid);
        if(count > 0){
            throw new CategoryException("对不起，该分类下还有图书，您不能删除它！");
        }
        categoryDao.delete(cid);
    }

    public Category load(String cid) {
        return categoryDao.load(cid);
    }

    public void edit(Category category) {
        categoryDao.edit(category);
    }
}
