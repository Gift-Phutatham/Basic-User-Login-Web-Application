/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.model.User;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author gigadot
 */
public class DeleteUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;

    @Override
    public String getMapping() {
        return "/user/delete";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            String username = (String) request.getSession().getAttribute("username");
            UserService userService = UserService.getInstance();
            // just in case there is any error, we will silently suppress the error with nice error message
            try {
                User currentUser = userService.findByUsername(username);
                // we will delete user by username, so we need to get requested username from parameter
                User deletingUser = userService.findByUsername(request.getParameter("username"));
                // prevent deleting own account, user cannot do it from UI but still can send request directly to server
                if (StringUtils.equals(currentUser.getUsername(), deletingUser.getUsername())) {
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message", "You cannot delete your own account.");
                } else {
                    if (userService.deleteUserByUsername(deletingUser.getUsername())) {
                        // go to user list page with successful delete message
                        // we will put message in the session
                        // these attributes are added to session so they will persist unless remove from session
                        // we need to ensure that they are deleted when they are read next time
                        // since in all cases, it will be redirect to home page, so we will remove them in home servlet
                        request.getSession().setAttribute("hasError", false);
                        request.getSession().setAttribute("message", String.format("User %s is successfully deleted.", deletingUser.getUsername()));
                    } else {
                        // go to user list page with error delete message
                        request.getSession().setAttribute("hasError", true);
                        request.getSession().setAttribute("message", String.format("Unable to delete user %s.", deletingUser.getUsername()));
                    }
                }
            } catch (Exception e) {
                // go to user list page with error delete message
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", String.format("Unable to delete user %s.", request.getParameter("username")));
            }
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login");
        }
    }
}
