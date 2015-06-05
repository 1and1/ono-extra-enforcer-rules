package net.oneandone.maven.rules.common;

import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

public class RuleHelper {
    public static ImmutableListMultimap<String, Dependency> getManagedDependenciesAsMap(MavenProject project) {
        if (project.getDependencyManagement() != null) {
            return Multimaps.index(project.getDependencyManagement().getDependencies(), new Function<Dependency, String>() {
                public String apply(Dependency from) {
                    if (from != null) {
                        return from.getGroupId() + ":" + from.getArtifactId();
                    }

                    return null;
                }
            });

        }
        return ImmutableListMultimap.of();
    }

    public static MavenProject getProject(EnforcerRuleHelper helper) throws ExpressionEvaluationException {
        return (MavenProject) helper.evaluate("${project}");
    }

    public static MavenSession getSession(EnforcerRuleHelper helper) throws ExpressionEvaluationException {
        return (MavenSession) helper.evaluate("${session}");
    }

    public static String getProjectIdentifier(MavenProject mavenProject) {
        return mavenProject.getGroupId() + ":" + mavenProject.getArtifactId();
    }

    public static String getDependencyIdentifier(Dependency dependency) {
        return dependency.getGroupId() + ":" + dependency.getArtifactId();
    }
}