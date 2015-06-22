package net.oneandone.maven.rules;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import net.oneandone.maven.rules.common.AbstractFilterableRule;
import net.oneandone.maven.rules.common.RuleHelper;

public class ForbidDependencyManagementInSubModulesRule extends AbstractFilterableRule {

    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
        Log log = helper.getLog();

        try {
            MavenProject currentProject = RuleHelper.getSession(helper).getCurrentProject();

            checkForSubmoduleDependencyManagement(currentProject, log);

            if (failureDetected) {
                throw new EnforcerRuleException("Failing because of forbidden dependency management in sub modules");
            }
        } catch (ExpressionEvaluationException e) {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
        }
    }

    private void checkForSubmoduleDependencyManagement(final MavenProject project, Log log) {
        if (projectHasManagedDependencies(project) &&
                !project.isExecutionRoot() &&
                !isExcluded(RuleHelper.getProjectIdentifier(project))) {

            final DependencyManagement dependencyManagement = project.getOriginalModel().getDependencyManagement();
            if (dependencyManagement.getDependencies().size() > 0) {
                failureDetected = true;
                logHeader(log, "dependency management in sub modules");
                for (Dependency dependency : dependencyManagement.getDependencies()) {
                    log.warn("module '" + project.getArtifact().getDependencyConflictId() + "' has dependency management for: " +
                            dependency.getManagementKey());
                }
            }
        }
    }

    private boolean projectHasManagedDependencies(MavenProject project) {
        return project.getDependencyManagement() != null && project.getDependencyManagement().getDependencies().size() > 0;
    }

}
