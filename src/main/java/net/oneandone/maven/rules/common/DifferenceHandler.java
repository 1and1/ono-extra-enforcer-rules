package net.oneandone.maven.rules.common;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;

public interface DifferenceHandler {
    void handleDifference(Log log, Dependency dependency, Dependency parentDependency);
}
