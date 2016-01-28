/**
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                !ruleIsDefinedInProjectOrNotModuleParent(project, log) &&
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

}
