package com.school.bookstore.order.domain;


import com.school.bookstore.user.domain.User;

import java.util.Date;
import java.util.List;

/**
 * Created by Karl on 2015/12/28.
 */
public class Order {

    private String oid;
    private Date ordertime;
    private double total;

    /**
     * 订单四种状态：
     *     1、未付款
     *     2、已付款但未发货
     *     3、已发货但未确认收货
     *     4、已确认交易成功
     */
    private int state;
    private User owner;
    private String address;
    private List<OrderItem> OrderItemList;

    public List<OrderItem> getOrderItemList() {
        return OrderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        OrderItemList = orderItemList;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
