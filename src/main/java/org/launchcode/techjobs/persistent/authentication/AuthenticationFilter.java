package org.launchcode.techjobs.persistent.authentication;

import org.launchcode.techjobs.persistent.controllers.UserController;
import org.launchcode.techjobs.persistent.models.User;
import org.launchcode.techjobs.persistent.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserController userController;

    private static final List<String> whitelist = Arrays.asList(
            "/",
            "/user/logout",
            "/user/signup",
            "/user/login",
            "/js/bootstrap/css",
            "/css/techjobs.css",
            "/css/bootstrap.css");

    private static boolean isPathWhitelisted(String path) {
        for (String whitePath : whitelist) {
            if (path.equals(whitePath)) return true;
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws IOException {

        HttpSession session = request.getSession();
        User user = userController.getUserFromSession(session);

        System.out.println(request.getRequestURI());
        if (isPathWhitelisted(request.getRequestURI())) return true;

        if (user != null) return true;
        response.sendRedirect("/user/login");
        return false;
    }
}
