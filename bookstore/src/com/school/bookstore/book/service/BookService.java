package com.school.bookstore.book.service;

import com.school.bookstore.book.dao.BookDao;
import com.school.bookstore.book.domain.Book;

import java.util.List;

/**
 * Created by wangsenmiao on 2015/12/25.
 */
public class BookService {

    private BookDao bookDao = new BookDao();

    public List<Book> findAll(){
        return bookDao.findAll();
    }

    public List<Book> findByCategory(String cid) {
        return bookDao.findByCategory(cid);
    }

    public Book load(String bid) {
        return bookDao.findByBid(bid);
    }

    public void add(Book book) {
        System.out.println("djjdjs");
      bookDao.add(book);
    }

    public void delete(String bid) {
        bookDao.detelet(bid);
    }

    public void edit(Book book) {
        bookDao.edit(book);
    }
}
