package com.school.bookstore.book.web.servlet.admin;

import com.bookstoreTools.commons.CommonUtils;
import com.school.bookstore.book.domain.Book;
import com.school.bookstore.book.service.BookService;
import com.school.bookstore.category.domain.Category;
import com.school.bookstore.category.service.CategoryService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Karl on 2015/12/27.
 */

public class AdminAddBookServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7842687357953100350L;
	private BookService bookService = new BookService();
    private CategoryService categoryService = new CategoryService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
       
        //创建工厂
        DiskFileItemFactory factory = new DiskFileItemFactory(15*1024,new File("D:/temp"));

        //得到解析器
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

        servletFileUpload.setFileSizeMax(20 * 1024);

        try {
            
            @SuppressWarnings("unchecked")
			List<FileItem> fileItemList = servletFileUpload.parseRequest(request);

            /**
             * 处理表单数据，映射到对象
             */
            Map<String,String> map = new HashMap<String,String>();
            for(FileItem fileItem:fileItemList){
                if(fileItem.isFormField()){
                    map.put(fileItem.getFieldName(),fileItem.getString("UTF-8"));
                }
            }
            Book book = CommonUtils.toBean(map, Book.class);
            book.setBid(CommonUtils.uuid());
            book.setDel(false);
            Category category = CommonUtils.toBean(map, Category.class);
            book.setCategory(category);
            

            String savePath = this.getServletContext().getRealPath("/book_img");
            System.out.println("hfhshdhsjjshh哈哈哈哈哈");
            /**
             * 获取图片名并校验图片格式
             */
            String filename = CommonUtils.uuid() + "_" + fileItemList.get(1).getName();
   
            if(!filename.toLowerCase().endsWith("jpg")){
                request.setAttribute("msg","上传的图片限制为JPG格式！");
                request.setAttribute("categoryList",categoryService.findAll());
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request,response);
                return;
            }

            File destFile = new File(savePath,filename);
            fileItemList.get(1).write(destFile);
            book.setImage("book_img/" + filename);

            /**
             * 校验图片的尺寸
             */
            Image image = new ImageIcon(destFile.getAbsolutePath()).getImage();
            if(image.getWidth(null) >200 || image.getHeight(null) > 200){
                destFile.delete();
                request.setAttribute("msg", "您上传的图片尺寸超过了200*200!");
                request.setAttribute("categoryList", categoryService.findAll());
                request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request,response);
                return;
            }
           
            bookService.add(book);
            request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll").forward(request,response);

        } catch (Exception e) {
            if(e instanceof FileUploadBase.FileSizeLimitExceededException){
                request.setAttribute("msg","您上传的文件超过了20KB");
                request.setAttribute("categoryList",categoryService.findAll());
                request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll").forward(request,response);
            }
        }


    }
}
