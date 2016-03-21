package com.school.bookstore.book.dao;

import com.bookstoreTools.commons.CommonUtils;
import com.bookstoreTools.jdbc.TxQueryRunner;
import com.school.bookstore.book.domain.Book;
import com.school.bookstore.category.domain.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangsenmiao on 2015/12/25.
 */
public class BookDao {
    QueryRunner qr = new TxQueryRunner();

    /**
     * 查询所有图书
     * @return
     */
    public List<Book> findAll(){
        String sql = "select * from book where del=false";
        try {
            List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler());
            List<Book> bookList = new LinkedList<Book>();
            for(@SuppressWarnings("rawtypes") Map map:mapList){
                Book book = CommonUtils.toBean(map, Book.class);
                Category category = CommonUtils.toBean(map,Category.class);
                book.setCategory(category);
                bookList.add(book);
            }
            return bookList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> findByCategory(String cid) {
        String sql = "select * from book where cid=?  and del=false";
        try {
            return qr.query(sql,new BeanListHandler<Book>(Book.class),cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book findByBid(String bid) {
        String sql = "select * from book where bid=?";
        try {
            Map<String,Object> map = qr.query(sql,new MapHandler(),bid);
            Book book = CommonUtils.toBean(map, Book.class);
            Category category = CommonUtils.toBean(map,Category.class);
            book.setCategory(category);
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCountByCid(String cid) {
        String sql = "select count(*) from book where cid=? and del=false";
        try {
             Number cnt = (Number)qr.query(sql,new ScalarHandler(),cid);
             return cnt.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Book book) {
        String sql = "insert into book values(?,?,?,?,?,?,?)";
        Object[] params = {book.getBid(),book.getBname(),book.getPrice(),book.getAuthor(),
                       book.getImage(),book.getCategory().getCid(),book.isDel()};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void detelet(String bid) {

        String sql = "update book set del=true where bid=?";
        try {
            qr.update(sql, bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void edit(Book book) {
        String sql = "update book set bname=?,price=?,author=?,image=?,cid=? where bid=?";
        Object[] params ={book.getBname(),book.getPrice(),book.getAuthor(),
                book.getImage(),book.getCategory().getCid(),book.getBid()};
        try {
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
