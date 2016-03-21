package com.school.bookstore.car.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * Created by xhy on 2015/12/27.
 */
public class Cart {

    private Map<String,CartItem> map = new LinkedHashMap<String,CartItem>();

    /**
     * 添加条目
     * @param cartItem
     */
    public void add(CartItem cartItem){
        if(map.containsKey(cartItem.getBook().getBid())){
            CartItem _cartItem = map.get(cartItem.getBook().getBid());
            _cartItem.setCount(_cartItem.getCount() + cartItem.getCount());
            map.put(cartItem.getBook().getBid(),_cartItem);
        }else {
            map.put(cartItem.getBook().getBid(),cartItem);
        }
    }

    public void clear(){
        map.clear();
    }

    public void delete(String bid){
        map.remove(bid);
    }

    /**
     * 获取所有条目
     * @return
     */
    public Collection<CartItem> getCartItems(){
        return map.values();
    }

    /**
     * 计算合计
     * @return
     */
    public double getTotal(){
        BigDecimal total = new BigDecimal("0");
        for(CartItem cartItem:map.values()){
            BigDecimal subtotal = new BigDecimal("" + cartItem.getSubtotal());
            total = total.add(subtotal);
        }
        return total.doubleValue();
    }

}
