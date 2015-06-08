package net.oneandone.maven.rules.common;

import java.util.Objects;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.enforcer.AbstractNonCacheableEnforcerRule;

public abstract class AbstractRule extends AbstractNonCacheableEnforcerRule {

    protected boolean failureDetected = false;
    private String lastLogHeader = null;

    protected void logHeader(Log log, String type) {
        if (!Objects.equals(lastLogHeader, type)) {
            lastLogHeader = type;
            log.warn(type + " findings of " + this.getClass().getSimpleName());
            log.warn("===========================================================");
        }
    }

}
