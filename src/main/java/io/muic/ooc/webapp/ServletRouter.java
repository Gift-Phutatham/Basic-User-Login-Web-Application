/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp;

import io.muic.ooc.webapp.servlet.*;
import io.muic.ooc.webapp.service.SecurityService;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gigadot
 */
public class ServletRouter {

    private static final List<Class<? extends Routable>> routables = new ArrayList<>();

    static {
        routables.add(HomeServlet.class);
        routables.add(DeleteUserServlet.class);
        routables.add(CreateUserServlet.class);
        routables.add(EditUserServlet.class);
        routables.add(LoginServlet.class);
        routables.add(LogoutServlet.class);
    }

    private SecurityService securityService;

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void init(Context ctx) {
        for (Class<? extends Routable> routableClass : routables) {
            try {
                Routable routable = routableClass.newInstance();
                routable.setSecurityService(securityService);
                String name = routable.getClass().getSimpleName();
                Tomcat.addServlet(ctx, name, (HttpServlet) routable);
                ctx.addServletMapping(routable.getMapping(), name);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
