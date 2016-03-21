package com.school.bookstore.user.service;

/**
 * Created by huangqiuhua on 2015/12/26.
 */
public class UserException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }
}
