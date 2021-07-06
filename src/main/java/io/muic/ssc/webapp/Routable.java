package io.muic.ssc.webapp;

import io.muic.ssc.webapp.service.SecurityService;

public interface Routable {

    String getMapping();

    void setSecurityService(SecurityService securityService);

}
