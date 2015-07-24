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

import static net.oneandone.maven.rules.common.RuleHelper.getProject;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import net.oneandone.maven.rules.common.AbstractFilterableRule;
import net.oneandone.maven.rules.common.DifferenceHandler;

public class ForbidOverridingManagedDependenciesRule
    extends AbstractFilterableRule {

    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
        Log log = helper.getLog();
        try {
            MavenProject project = getProject(helper);

            compareDependenciesWithParentManagement(project, log, new DifferenceHandler() {
                public void handleDifference(Log log, Dependency dependency, Dependency parentDependency) {
                    logHeader(log, "dependency management");
                    log.warn("Difference for: " + dependency.getManagementKey());
                    log.warn("Project: " + dependency.getVersion());
                    log.warn("Parent:  " + parentDependency.getVersion());
                    log.warn("----------------------------------------");
                    failureDetected = true;
                }
            });

            if (failureDetected) {
                throw new EnforcerRuleException("Failing because of overridden managed dependencies");
            }
        } catch (ExpressionEvaluationException e) {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
        }
    }

}
