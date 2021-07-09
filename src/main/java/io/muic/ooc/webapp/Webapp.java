package io.muic.ooc.webapp;

import java.io.File;
import javax.servlet.ServletException;

import io.muic.ooc.webapp.service.DatabaseConnectionService;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;
import io.muic.ooc.webapp.servlet.HomeServlet;
import io.muic.ooc.webapp.servlet.LoginServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

public class Webapp {

    public static void main(String[] args) {

        File docBase = new File("src/main/webapp/");
        docBase.mkdirs();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);

        SecurityService securityService = new SecurityService();
        securityService.setUserService(UserService.getInstance());

        ServletRouter servletRouter = new ServletRouter();
        servletRouter.setSecurityService(securityService);

        Context ctx;
        try {
            ctx = tomcat.addWebapp("", docBase.getAbsolutePath());
            servletRouter.init(ctx);

            // customize error page for redirection
            ErrorPage error404Page = new ErrorPage();
            error404Page.setErrorCode(404);
            error404Page.setLocation("/WEB-INF/error404.jsp");
            ctx.addErrorPage(error404Page);

            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException ex) {
            ex.printStackTrace();
        }

    }
}
