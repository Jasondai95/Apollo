package com.school.bookstore.book.web.servlet;

import com.bookstoreTools.servlet.BaseServlet;
import com.school.bookstore.book.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wangsenmiao on 2015/12/25.
 */

public class BookServlet extends BaseServlet {


    /**
	 * 
	 */
	private static final long serialVersionUID = -9044628946850281221L;
	private BookService bookService = new BookService();

    /**
     * 查询所有图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        request.setAttribute("bookList",bookService.findAll());
        return "f:/jsps/book/list.jsp";
    }

    /**
     * 按图书分类查询
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cid = request.getParameter("cid");
        request.setAttribute("bookList",bookService.findByCategory(cid));
        return "f:/jsps/book/list.jsp";
    }

    /**
     * 加载图书详细信息
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String bid = request.getParameter("bid");
        request.setAttribute("book",bookService.load(bid));
        return "f:/jsps/book/desc.jsp";
    }


}
