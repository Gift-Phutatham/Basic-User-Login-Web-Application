package io.muic.ssc.webapp.servlet;

import io.muic.ssc.webapp.Routable;
import io.muic.ssc.webapp.model.User;
import io.muic.ssc.webapp.service.SecurityService;
import io.muic.ssc.webapp.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChangePasswordServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/user/password";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuthorized(request)) {
            String username = StringUtils.trim(request.getParameter("username")); /* From query part. */
            UserService userService = UserService.getInstance();

            /* Prefill the form. */
            User user = userService.findByUsername(username);
            request.setAttribute("user", user);
            request.setAttribute("username", user.getUsername());

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request, response);
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
        } else {
            request.removeAttribute("hasError");
            request.removeAttribute("message");
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (securityService.isAuthorized(request)) {
            /* Change password is similar to edit user. */
            /* Ensure that username do not contain leading and trailing spaces. */
            String username = StringUtils.trim(request.getParameter("username")); /* From query part. */
            String password = request.getParameter("password");
            String cpassword = request.getParameter("cpassword");

            UserService userService = UserService.getInstance();
            User user = userService.findByUsername(username);
            String errorMessage = null;
            /* Check if exists. */
            if (user == null) {
                errorMessage = String.format("User %s does not exist.", username);
            }
            /* Check if password is valid. */
            else if (StringUtils.isBlank(password)) {
                errorMessage = "Password cannot be blank.";
            }
            /* Check if confirmed password is correct. */
            else if (!StringUtils.equals(password, cpassword)) {
                errorMessage = "Confirmed password mismatches.";
            }

            if (errorMessage != null) {
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", errorMessage);
            } else {
                /* Edit a user. */
                try {
                    userService.changePassword(username, password);
                    request.getSession().setAttribute("hasError", false);
                    request.getSession().setAttribute("message", String.format("Password for user %s has been changed successfully.", username));
                    response.sendRedirect("/");
                    return;
                } catch (Exception e) {
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message", e.getMessage());
                }
            }

            request.setAttribute("username", username);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/password.jsp");
            rd.include(request, response);
            request.getSession().removeAttribute("hasError");
            request.getSession().removeAttribute("message");
        } else {
            request.removeAttribute("hasError");
            request.removeAttribute("message");
            response.sendRedirect("/login");
        }
    }
}
