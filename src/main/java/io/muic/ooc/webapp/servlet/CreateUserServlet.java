package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/user/create";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/create.jsp");
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
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {

            // ensure that username and displayName do not contain leading and trailing spaces
            String username = StringUtils.trim((String) request.getParameter("username"));
            String displayName = StringUtils.trim((String) request.getParameter("displayName"));
            String password = (String) request.getParameter("password");
            String cpassword = (String) request.getParameter("cpassword");

            UserService userService = UserService.getInstance();
            String errorMessage = null;
            // check if username is valid
            if (StringUtils.isBlank(username)) {
                errorMessage = "Username cannot be blank.";
            }
            else if (userService.findByUsername(username) != null) {
                errorMessage = String.format("Username %s has already been taken.", username);
            }
            // check if displayName is valid
            else if (StringUtils.isBlank(displayName)) {
                errorMessage = "Display Name cannot be blank.";
            }
            // check if password is valid
            else if (StringUtils.isBlank(password)) {
                errorMessage = "Password cannot be blank.";
            }
            // check if confirmed password is correct
            else if (!StringUtils.equals(password, cpassword)) {
                errorMessage = "Confirmed password mismatches.";
            }

            if (errorMessage != null) {
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", errorMessage);
            } else {
                // create a user
                try {
                    userService.createUser(username, password, displayName);
                    // if no error redirect
                    request.getSession().setAttribute("hasError", false);
                    request.getSession().setAttribute("message", String.format("User %s has been created successfully.", username));
                    response.sendRedirect("/");
                    return;
                } catch (Exception e) {
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message", e.getMessage());
                }
            }

            // prefill the form
            request.setAttribute("username", username);
            request.setAttribute("displayName", displayName);
            request.setAttribute("password", password);
            request.setAttribute("cpassword", cpassword);

            // if not success, it will arrive here
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/create.jsp");
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
