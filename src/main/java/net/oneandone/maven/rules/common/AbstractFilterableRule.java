package net.oneandone.maven.rules.common;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;

public abstract class AbstractFilterableRule extends AbstractRule {
    private List<String> excludes = null;

    protected boolean isExcluded(String artifactIdentifier) {
        if (excludes != null && excludes.size() > 0) {
            for (String exclude : excludes) {
                if (artifactIdentifier.startsWith(exclude)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void compareDependenciesWithParentManagement(MavenProject project, Log log, DifferenceHandler differenceHandler) {
        if (project.getParent() != null) {
            List<Dependency> projectDependencies = project.getDependencies();
            if (project.getDependencyManagement() != null) {
                projectDependencies.addAll(project.getDependencyManagement().getDependencies());
            }
            ImmutableListMultimap<String, Dependency> parentProjectDependencies = RuleHelper.getManagedDependenciesAsMap(project.getParent());

            for (Dependency dependency : projectDependencies) {
                ImmutableList<Dependency> parentDependencies = parentProjectDependencies.get(RuleHelper.getDependencyIdentifier(dependency));
                if (parentDependencies != null && !isExcluded(dependency.getManagementKey())) {
                    for (Dependency parentDependency : parentDependencies) {
                        if (dependency.getManagementKey().equals(parentDependency.getManagementKey())) {
                            if (!dependency.getVersion().equals(parentDependency.getVersion())) {
                                differenceHandler.handleDifference(log, dependency, parentDependency);
                            }
                            break;
                        }
                    }
                }
            }

        }
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }
}
