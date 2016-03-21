package com.school.bookstore.book.web.servlet.admin;

import com.bookstoreTools.commons.CommonUtils;
import com.bookstoreTools.servlet.BaseServlet;
import com.school.bookstore.book.domain.Book;
import com.school.bookstore.book.service.BookService;
import com.school.bookstore.category.domain.Category;
import com.school.bookstore.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Karl on 2015/12/27.
 */
public class AdminBookServlet extends BaseServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CategoryService categoryService = new CategoryService();

    private BookService bookService = new BookService();

    /**
     * 查询所有图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Book> bookList = bookService.findAll();
        request.setAttribute("categoryList",categoryService.findAll());
        request.setAttribute("bookList", bookList);
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     * 通过bid查找图书加载图书详细信息
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String load(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bid = request.getParameter("bid");
        request.setAttribute("book",bookService.load(bid));
        request.setAttribute("categoryList",categoryService.findAll());
        return "f:/adminjsps/admin/book/desc.jsp";
    }

    /**
     * 添加图书之前加载分类
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String addPre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("categoryList", categoryService.findAll());
        return "f:/adminjsps/admin/book/add.jsp";
    }

    /**
     * 删除图书，假删除，即逻辑上删除
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String bid = request.getParameter("bid");
        bookService.delete(bid);
        return findAll(request,response);
    }

    public String edit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Book book = CommonUtils.toBean(request.getParameterMap(),Book.class);
        Category category = CommonUtils.toBean(request.getParameterMap(),Category.class);
        book.setCategory(category);
        bookService.edit(book);
        return load(request,response);
    }




}
