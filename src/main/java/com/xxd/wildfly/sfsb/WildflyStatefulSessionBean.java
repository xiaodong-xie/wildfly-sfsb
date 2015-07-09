package com.xxd.wildfly.sfsb;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateful
@LocalBean
@EJB(name = "java:app/WildflyStatefulSessionBean", beanInterface = WildflyStatefulSessionBean.class)
public class WildflyStatefulSessionBean {

    private static final Logger LOG = LoggerFactory.getLogger(WildflyStatefulSessionBean.class);

    private int value;

    public void sfsbMethod() {
        try {
            Thread.sleep(35000);
        } catch (Exception e) {
            LOG.warn("interrupted...", e);
        }
        value++;
        LOG.info("current value is {}", value);
    }

}
