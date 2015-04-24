package net.oneandone.maven.rules;

import static net.oneandone.maven.rules.common.RuleHelper.getProject;

import java.util.Map;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import net.oneandone.maven.rules.common.AbstractFilterableRule;

public class ForbidOveridingManagedPluginsRule extends AbstractFilterableRule {

    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
        Log log = helper.getLog();

        try {
            MavenProject project = getProject(helper);

            checkPluginManagementDiffs(project, log);

            if ( shouldBuildFail() && failureDetected) {
                throw new EnforcerRuleException("Failing because of overridden managed plugins");
            }
        } catch (ExpressionEvaluationException e) {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
        }
    }

    private void checkPluginManagementDiffs(MavenProject project, Log log) {
        final Map<String, Plugin> projectPlugins = project.getPluginManagement().getPluginsAsMap();
        final Map<String, Plugin> parentProjectPlugins = project.getParent().getPluginManagement().getPluginsAsMap();

        final MapDifference<String, Plugin> difference = Maps.difference(projectPlugins,
                parentProjectPlugins);

        final Map<String, Plugin> entriesDiffering = difference.entriesInCommon();

        for (Map.Entry<String, Plugin> differenceEntry : entriesDiffering.entrySet()) {
            final String key = differenceEntry.getKey();
            final Plugin plugin = projectPlugins.get(key);
            final Plugin parentPlugin = parentProjectPlugins.get(key);
            if (!plugin.getVersion().equals(parentPlugin.getVersion()) && !isExcluded(key)) {
                logHeader(log, "plugin management");
                log.warn("Difference for: " + key);
                log.warn("Project: " + plugin.getVersion());
                log.warn("Parent:  " + parentPlugin.getVersion());
                log.warn("----------------------------------------");
                failureDetected = true;
            }
        }
    }

}
