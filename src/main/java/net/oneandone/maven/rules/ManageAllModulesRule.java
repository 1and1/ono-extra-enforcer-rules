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

import java.util.List;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import com.google.common.collect.ImmutableListMultimap;

import net.oneandone.maven.rules.common.AbstractRule;
import net.oneandone.maven.rules.common.RuleHelper;

public class ManageAllModulesRule extends AbstractRule {
    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
        try {
            MavenProject project = RuleHelper.getProject(helper);

            if (project.isExecutionRoot()) {
                detectUnmanagedModules(helper, project);
            }
        } catch (ExpressionEvaluationException e) {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
        }
    }

    private void detectUnmanagedModules(EnforcerRuleHelper helper, MavenProject project) throws ExpressionEvaluationException, EnforcerRuleException {
        Log log = helper.getLog();
        MavenSession session = RuleHelper.getSession(helper);
        List<MavenProject> projects = session.getProjects();
        ImmutableListMultimap managedDependencies = RuleHelper.getManagedDependenciesAsMap(project);

        for (MavenProject mavenProject : projects) {
            if (mavenProject.isExecutionRoot()) {
                continue;
            }
            String projectIdentifier = RuleHelper.getProjectIdentifier(mavenProject);
            if (!managedDependencies.containsKey(projectIdentifier)) {
                logHeader(log, "manage all modules");
                log.warn("unmanaged project found: " + projectIdentifier);
                failureDetected = true;
            }
        }

        if (failureDetected) {
            throw new EnforcerRuleException("Failing because of overridden managed plugins");
        }
    }
}
