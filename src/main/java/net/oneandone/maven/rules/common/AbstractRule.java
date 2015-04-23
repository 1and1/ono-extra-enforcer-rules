package net.oneandone.maven.rules.common;

import java.util.Objects;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.plugin.logging.Log;

public abstract class AbstractRule implements EnforcerRule {
    protected boolean shouldBuildFail = false;
    protected boolean failureDetected = false;
    private String lastLogHeader = null;

    /**
     * If your rule is cacheable, you must return a unique id when parameters or conditions
     * change that would cause the result to be different. Multiple cached results are stored
     * based on their id.
     * <p>
     * The easiest way to do this is to return a hash computed from the values of your parameters.
     * <p>
     * If your rule is not cacheable, then the result here is not important, you may return anything.
     */
    public String getCacheId() {
        //no hash on boolean...only parameter so no hash is needed.
        return null;
    }

    /**
     * This tells the system if the results are cacheable at all. Keep in mind that during
     * forked builds and other things, a given rule may be executed more than once for the same
     * project. This means that even things that change from project to project may still
     * be cacheable in certain instances.
     */
    public boolean isCacheable() {
        return false;
    }

    /**
     * If the rule is cacheable and the same id is found in the cache, the stored results
     * are passed to this method to allow double checking of the results. Most of the time
     * this can be done by generating unique ids, but sometimes the results of objects returned
     * by the helper need to be queried. You may for example, store certain objects in your rule
     * and then query them later.
     */
    public boolean isResultValid(EnforcerRule enforcerRule) {
        return false;
    }

    protected void logHeader(Log log, String type) {
        if (!Objects.equals(lastLogHeader, type)) {
            lastLogHeader = type;
            log.warn(type + " findings of " + this.getClass().getSimpleName());
            log.warn("===========================================================");
        }
    }

    public void setShouldBuildFail(boolean shouldBuildFail) {
        this.shouldBuildFail = shouldBuildFail;
    }
}
