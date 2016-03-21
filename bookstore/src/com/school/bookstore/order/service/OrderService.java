package com.school.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;


import com.bookstoreTools.jdbc.JdbcUtils;
import com.school.bookstore.order.dao.OrderDao;
import com.school.bookstore.order.domain.Order;

/**
 * Created by Karl on 2015/12/29.
 */
public class OrderService {

    private OrderDao orderDao = new OrderDao();

    /**
     * 持久化订单，需要事务处理
     *
     * @param order
     */
    public void add(Order order) {

        try {
            JdbcUtils.beginTransaction();

            orderDao.addOrder(order);
            orderDao.addOderItemList(order.getOrderItemList());

            JdbcUtils.commitTransaction();

        } catch (Exception e) {
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过用户id查询订单
     * @param uid
     * @return
     */
    public List<Order> myOders(String uid) {
        return orderDao.findByUid(uid);
    }

    /**
     * 用户点击“我的订单”后从数据库加载订单
     * @param oid
     * @return
     */
    public Order load(String oid) {
        return orderDao.load(oid);
    }

    public void confirm(String oid) throws OrderException {
        int state = orderDao.getStateByOid(oid);

        /**
         * 如果查询到的订单状态不为3，即已发货还未确认收货，抛出异常
         */
        if(state != 3){
            throw new OrderException("订单确认失败，你不是什么好东西！");
        }
        orderDao.updateState(oid,4);
    }

    /**
     * 判断订单当前状态，保证只修改一次数据库数据
     * @param r6_order
     */
    public void payment(String r6_order) {
        int state = orderDao.getStateByOid(r6_order);
        if( state == 1){
            orderDao.updateState(r6_order,2);
            //还可以包含其他业务，如修改用户积分，此处略去
        }


    }
}
