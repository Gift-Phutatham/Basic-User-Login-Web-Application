package io.muic.ssc.webapp.servlet;

import io.muic.ssc.webapp.Routable;
import io.muic.ssc.webapp.model.User;
import io.muic.ssc.webapp.service.SecurityService;
import io.muic.ssc.webapp.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (securityService.isAuthorized(request)) {
            String username = (String) request.getSession().getAttribute("username");
            UserService userService = UserService.getInstance();
            /* Just in case there is any error, we will silently suppress the error with nice error message. */
            try {
                User currentUser = userService.findByUsername(username);
                /* We will delete user by username, so we need to get requested username from parameter. */
                User deletingUser = userService.findByUsername(request.getParameter("username"));
                /* Prevent deleting own account, either from UI or send request directly to server. */
                if (StringUtils.equals(currentUser.getUsername(), deletingUser.getUsername())) {
                    request.getSession().setAttribute("hasError", true);
                    request.getSession().setAttribute("message", "You cannot delete your own account.");
                } else {
                    if (userService.deleteUserByUsername(deletingUser.getUsername())) {
                        /* Go to user list page with successful delete message. */
                        /* Put message in the session. */
                        /* These attributes are added to session so they will persist unless remove from session. */
                        /* We need to ensure that they are deleted when they are read next time. */
                        /* Since in all cases, it will be redirect to home page, so we will remove them in home servlet. */
                        request.getSession().setAttribute("hasError", false);
                        request.getSession().setAttribute("message", String.format("User %s is successfully deleted.", deletingUser.getUsername()));
                    } else {
                        /* Go to user list page with error delete message. */
                        request.getSession().setAttribute("hasError", true);
                        request.getSession().setAttribute("message", String.format("Unable to delete user %s.", deletingUser.getUsername()));
                    }
                }
            } catch (Exception e) {
                /* Go to user list page with error delete message. */
                request.getSession().setAttribute("hasError", true);
                request.getSession().setAttribute("message", String.format("Unable to delete user %s.", request.getParameter("username")));
            }
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login");
        }
    }
}
