package com.school.bookstore.user.web.servlet;

import com.bookstoreTools.commons.CommonUtils;
import com.bookstoreTools.mail.Mail;
import com.bookstoreTools.mail.MailUtils;
import com.bookstoreTools.servlet.BaseServlet;
import com.school.bookstore.car.domain.Cart;
import com.school.bookstore.user.domain.User;
import com.school.bookstore.user.service.UserException;
import com.school.bookstore.user.service.UserService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 与用户相关功能实现
 * Created by huangqiuhua on 2015/12/26.
 */
public class UserServlet extends BaseServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();

    /**
     * 用户注册功能，包含邮件激活
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String regist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User form = CommonUtils.toBean(request.getParameterMap(),User.class);
        form.setUid(CommonUtils.uuid());
        
        //激活码
        form.setCode(CommonUtils.uuid()+CommonUtils.uuid());


        /**
         * 输入检验
         */
        Map<String,String> errors = new HashMap<String,String>();
        String username = form.getUsername();
        if(username == null || username.trim().isEmpty()){
            errors.put("username","用户名不能为空！");
        }else if(username.length() < 3|| username.length()>10){
            errors.put("username","用户名长度必须在3~10之间！");
        }

        String password = form.getPassword();
        if(password == null || password.trim().isEmpty()){
            errors.put("password","密码不能为空！");
        }else if(password.length() < 6|| password.length()>16){
            errors.put("password","密码长度必须在6~16之间！");
        }

        String email = form.getEmail();
        if(email == null || email.trim().isEmpty()){
            errors.put("email","email不能为空！");
        }else if( !email.matches("\\w+.\\w+@\\w+\\.\\w+")){
            errors.put("email","email格式错误！");
        }

        /**
         * 判断是否有输入错误，若有则回显错误信息，输入信息
         */
        if(errors.size() > 0){
            request.setAttribute("errors",errors);
            request.setAttribute("form",form);
            return "f:/jsps/user/regist.jsp";
        }

        /**
         * 调用service的regiset()方法
         * 若有异常则保存信息并转发显示在regist.jsp
         */
        try{
            userService.regist(form);
        }catch (UserException e){
            request.setAttribute("msg",e.getMessage());
            request.setAttribute("form",form);
            return "f:/jsps/user/regist.jsp";
        }

        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
        String host = props.getProperty("host");
        String uname = props.getProperty("uname");
        String pwd = props.getProperty("pwd");
        String from = props.getProperty("from");
        String to = form.getEmail();
        String subject = props.getProperty("subject");
        String content = props.getProperty("content");
        content = MessageFormat.format(content,form.getCode());

        Session session = MailUtils.createSession(host,uname,pwd);
        Mail mail = new Mail(from,to,subject,content);
        try {
            MailUtils.send(session,mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        request.setAttribute("msg","恭喜，注册成功！请马上到邮箱激活");
        return "f:/jsps/msg.jsp";
    }

    /**
     * 用户激活功能
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String active(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        try{
            userService.active(code);
            request.setAttribute("msg","恭喜，您激活成功了!请马上登录吧。");
        }catch (UserException e){
            request.setAttribute("msg",e.getMessage());
        }
        return "f:/jsps/msg.jsp";
    }

    /**
     * 登录处理
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User form = CommonUtils.toBean(request.getParameterMap(), User.class);
        try {
            User user = userService.login(form);
            request.getSession().setAttribute("session_user", user);

            /**
             * 登录成功后给用户一个购物车
             */
            request.getSession().setAttribute("cart",new Cart());
            return "r:/index.jsp";
        } catch (UserException e) {
            request.setAttribute("msg",e.getMessage());
            request.setAttribute("form",form);
            return "f:/jsps/user/login.jsp";
        }

    }

    public String quit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       request.getSession().invalidate();
        return "r:/index.jsp";

    }




}
