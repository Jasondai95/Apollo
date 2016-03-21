package com.school.bookstore.category.web.servlet.admin;

import com.bookstoreTools.commons.CommonUtils;
import com.bookstoreTools.servlet.BaseServlet;
import com.school.bookstore.category.domain.Category;
import com.school.bookstore.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liangrc on 2015/12/27.
 */
public class AdminCategoryServlet extends BaseServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CategoryService categoryService = new CategoryService();

    /**
     * 查询所有分类
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("categoryList", categoryService.findAll());
        return "f:/adminjsps/admin/category/list.jsp";
    }

    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
        category.setCid(CommonUtils.uuid());
        categoryService.add(category);
        return findAll(request, response);
    }

    /**
     * 删除分类
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cid = request.getParameter("cid");
        try {
            categoryService.getCountByCid(cid);
            return findAll(request,response);
        } catch (CategoryException e) {
            request.setAttribute("msg", e.getMessage());
            return "f:/adminjsps/msg.jsp";
        }
    }

    /**
     * 修改之前加载分类
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String preEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cid = request.getParameter("cid");
        request.setAttribute("category",categoryService.load(cid));
        return "f:/adminjsps/admin/category/mod.jsp";
    }

    /**
     * 修改分类
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Category category = CommonUtils.toBean(request.getParameterMap(),Category.class);
        
        categoryService.edit(category);
        return findAll(request,response);
    }


}
