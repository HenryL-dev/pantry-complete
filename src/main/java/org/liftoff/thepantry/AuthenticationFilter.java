package org.liftoff.thepantry;


import org.liftoff.thepantry.controllers.AuthenticationController;
import org.liftoff.thepantry.data.UserRepository;
import org.liftoff.thepantry.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements HandlerInterceptor {

    @Autowired
    UserRepository UserRepository;

    @Autowired
    AuthenticationController AuthenticationController;

    private static final List<String> whitelist = Arrays.asList("", "/", "/search/index", "/list/index", "/recipe", "/home", "/index", "/login", "/register", "/logout", "/css");

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        if (isWhitelisted(request.getRequestURI())) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = AuthenticationController.getUserFromSession(session);

        if (user != null) {
            return true;
        }

        response.sendRedirect("/index");
        return false;
    }
}
