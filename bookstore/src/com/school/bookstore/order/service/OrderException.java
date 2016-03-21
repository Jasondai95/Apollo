package com.school.bookstore.order.service;

/**
 * Created by Karl on 2015/12/19.
 */
public class OrderException extends Throwable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderException(String message) {
        super(message);
    }

    public OrderException() {
        super();
    }
}
