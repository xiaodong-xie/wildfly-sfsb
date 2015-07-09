package com.xxd.wildfly.sfsb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.naming.InitialContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 <subsystem xmlns="urn:jboss:domain:transactions:2.0">
 <core-environment node-identifier="standalone">
 <process-id>
 <uuid/>
 </process-id>
 </core-environment>
 <recovery-environment socket-binding="txn-recovery-environment" status-socket-binding="txn-status-manager"/>
 <coordinator-environment default-timeout="25"/>
 </subsystem>
 */
@Singleton
@LocalBean
@Startup
public class WildflyTimer {

    private static Logger LOG = LoggerFactory.getLogger(WildflyTimer.class);

    private volatile boolean run;

    private volatile Thread runningThread;

    @Resource
    private TimerService timerService;

    @PostConstruct
    protected void postConstruct() {
        run = true;
        timerService.createSingleActionTimer(0, new TimerConfig("WildflyTimer", false));
    }

    @Lock(LockType.READ)
    @Timeout
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void runPeriodically() {
        LOG.info("started to runPeriodically...");
        runningThread = Thread.currentThread();
        while (run) {
            try {
                WildflyStatefulSessionBean sfsb =
                    (WildflyStatefulSessionBean) new InitialContext().lookup("java:app/WildflyStatefulSessionBean");
                if (sfsb != null) {
                    sfsb.sfsbMethod();
                }
            } catch (Exception e) {
                LOG.warn("exception happened...", e);
                try {
                    Thread.sleep(30000);
                } catch (Exception e1) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        LOG.info("finished to runPeriodically...");
    }

    @Lock(LockType.READ)
    @PreDestroy
    public void stop() {
        LOG.info("Stopping WildflyTimer");
        if (run) {
            // stop processing
            run = false;
            if (runningThread != null) {
                runningThread.interrupt();
            }
        }
        LOG.info("Stopped WildflyTimer.");
    }
}
