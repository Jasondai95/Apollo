package com.school.bookstore.order.dao;

import com.bookstoreTools.commons.CommonUtils;
import com.bookstoreTools.jdbc.TxQueryRunner;
import com.school.bookstore.book.domain.Book;
import com.school.bookstore.order.domain.Order;
import com.school.bookstore.order.domain.OrderItem;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Karl on 2015/12/28.
 */
public class OrderDao {
    QueryRunner qr = new TxQueryRunner();

    public void addOrder(Order order) {
        try {
            String sql = "insert into orders values(?,?,?,?,?,?)";
            Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
            Object[] params = {order.getOid(), timestamp, order.getTotal(), order.getState(),
                    order.getOwner().getUid(), order.getAddress()};
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addOderItemList(List<OrderItem> orderItemList) {

        String sql = "insert into orderitem values(?,?,?,?,?)";

        Object[][] params = new Object[orderItemList.size()][];
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem item = orderItemList.get(i);
            params[i] = new Object[]{item.getIid(), item.getCount(),
                    item.getSubtotal(), item.getOrder().getOid(), item.getBook().getBid()};
        }
        try {
            qr.batch(sql, params);//执行批处理
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> findByUid(String uid) {

        try {

            String sql = "select * from orders where uid=? ORDER BY ordertime DESC";
            List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), uid);

            /**
             * 循环遍历每个订单，加载条目信息
             */
            for (Order order : orderList) {
                loadOrderItems(order);
            }
            return orderList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadOrderItems(Order order) throws SQLException {

        String sql = "select * from orderitem i,book b where i.bid=b.bid and oid=?";

        /**
         * 一行不在对应一个javaBean,所以使用MapListHandler
         */
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());


        /**
         * 将mapList转化为orderItemList,并建立OrderItem与book关系
         */
        List<OrderItem> orderItemList = toOrderItemList(mapList);

        order.setOrderItemList(orderItemList);

    }

    private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (Map<String, Object> map : mapList) {
            OrderItem item = toOrderItem(map);
            orderItemList.add(item);
        }
        return orderItemList;
    }

    private OrderItem toOrderItem(Map<String, Object> map) {
        OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        orderItem.setBook(book);
        return orderItem;
    }

    public Order load(String oid) {
        try{
            String sql = "select * from orders where oid=?";
            Order order = qr.query(sql,new BeanHandler<Order>(Order.class),oid);
            loadOrderItems(order);
            return order;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据订单id查询订单状态
     * @param oid
     * @return
     */
    public int getStateByOid(String oid){
        try{
            String sql = "select state from orders where oid=?";
            return (Integer)qr.query(sql,new ScalarHandler(),oid);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改指定订单的状态
     * @param oid
     * @param state
     */
    public void updateState(String oid,int state){
        String sql = "update orders set State=? where oid=?";
        try {
            qr.update(sql,state,oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
