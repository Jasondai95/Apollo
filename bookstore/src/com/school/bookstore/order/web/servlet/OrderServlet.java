package com.school.bookstore.order.web.servlet;

import com.bookstoreTools.commons.CommonUtils;
import com.bookstoreTools.servlet.BaseServlet;
import com.school.bookstore.car.domain.Cart;
import com.school.bookstore.car.domain.CartItem;
import com.school.bookstore.order.domain.Order;
import com.school.bookstore.order.domain.OrderItem;
import com.school.bookstore.order.service.OrderException;
import com.school.bookstore.order.service.OrderService;
import com.school.bookstore.user.domain.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by Karl on 2015/12/27.
 */
public class OrderServlet extends BaseServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderService orderService = new OrderService();

    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        /**
         * 创建订单对象
         */
        Order order = new Order();
        order.setOid(CommonUtils.uuid());
        order.setOrdertime(new Date());
        order.setTotal(cart.getTotal());
        order.setState(1);
        User user = (User) request.getSession().getAttribute("session_user");
        order.setOwner(user);

        /**
         * 创建订单条目
         */
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        for (CartItem cartItem : cart.getCartItems()) {
            //设置属性
            OrderItem orderItem = new OrderItem();
            orderItem.setIid(CommonUtils.uuid());
            orderItem.setCount(cartItem.getCount());
            orderItem.setBook(cartItem.getBook());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItem.setOrder(order);

            orderItemList.add(orderItem);
        }
        order.setOrderItemList(orderItemList);

        //清空购物车
        cart.clear();
        orderService.add(order);
        request.setAttribute("order", order);

        return "f:jsps/order/desc.jsp";
    }

    public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("session_user");
        List<Order> orderList = orderService.myOders(user.getUid());
        request.setAttribute("orderList", orderList);

        return "f:jsps/order/list.jsp";
    }

    public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("order", orderService.load(request.getParameter("oid")));

        return "f:jsps/order/desc.jsp";
    }

    /**
     * 确认收货
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String oid = request.getParameter("oid");
        try {
            orderService.confirm(oid);
            request.setAttribute("msg", "恭喜，确认订单完成，交易成功！");
        } catch (OrderException e) {
            request.setAttribute("msg", e.getMessage());
        }

        return "f:/jsps/msg.jsp";
    }

    /**
     * 支付之去银行
     */
    public String payment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Properties props = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
        props.load(input);

        String p0_Cmd = "Buy";
        String p1_MerId = props.getProperty("p1_MerId");
        String p2_Order = request.getParameter("oid");
        String p3_Amt = "0.01";
        String p4_Cur = "CNY";
        String p5_Pid = "";
        String p6_Pcat = "";
        String p7_Pdesc = "";
        String p8_Url = props.getProperty("p8_Url");
        String p9_SAF = "";
        String pa_MP = "";
        String pd_FrpId = request.getParameter("pd_FrpId");
        String pr_NeedResponse = "1";

        /**
         * 计算hmac,验证hash码
         */
        String keyValue = props.getProperty("keyValue");
        String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
                p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
                pd_FrpId, pr_NeedResponse, keyValue);

        StringBuilder url = new StringBuilder(props.getProperty("url"));
        url.append("?p0_Cmd=").append(p0_Cmd);
        url.append("&p1_MerId=").append(p1_MerId);
        url.append("&p2_Order=").append(p2_Order);
        url.append("&p3_Amt=").append(p3_Amt);
        url.append("&p4_Cur=").append(p4_Cur);
        url.append("&p5_Pid=").append(p5_Pid);
        url.append("&p6_Pcat=").append(p6_Pcat);
        url.append("&p7_Pdesc=").append(p7_Pdesc);
        url.append("&p8_Url=").append(p8_Url);
        url.append("&p9_SAF=").append(p9_SAF);
        url.append("&pa_MP=").append(pa_MP);
        url.append("&pd_FrpId=").append(pd_FrpId);
        url.append("&pr_NeedResponse=").append(pr_NeedResponse);
        url.append("&hmac=").append(hmac);


        response.sendRedirect(url.toString());

        return null;

    }

    /**
     * 此方法由易宝回调，必须判断请求此方法的是否为易宝
     */
    public String back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        /**
         * 获取请求前11个参数，用来计算hmac，已验证请求者
         */
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");

        String hmac = request.getParameter("hmac");

        Properties props = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
        props.load(input);

        String keyValue = props.getProperty("keyValue");

        boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
                r3_Amt,r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, keyValue);

        if(!bool){
            request.setAttribute("msg","您不是什么好东西！");
            return "f:/jsps/msg.jsp";
        }

        orderService.payment(r6_Order);

        /**
         * 判断当前回调方法
         * 如果为点对点，反馈success字符串
         */
        if(r9_BType.equals("2")){
            response.getWriter().print("success");
        }

        request.setAttribute("msg","支付成功！请等待买家发货！你慢慢等吧~~~");
        return "f:/jsps/msg.jsp";
    }


}
