package com.xxd.wildfly.sfsb;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class StartupListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(StartupListener.class);

    @Inject
    private WildflyTimer wildflyTimer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        wildflyTimer.stop();
    }
}
