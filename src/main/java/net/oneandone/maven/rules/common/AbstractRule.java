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
package net.oneandone.maven.rules.common;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.maven.model.Build;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.enforcer.AbstractNonCacheableEnforcerRule;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

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

    private boolean projectIsSubmodule(MavenProject project, Log log) {
        final MavenProject parent = project.getParent();
        if (parent != null) {
            for (String module : parent.getOriginalModel().getModules()) {
                final File parentProjectWithModule = new File(parent.getFile().getParentFile(), module);
                log.warn("project Parent + sub: " + parentProjectWithModule);
                final File projectParentFolder = project.getFile().getParentFile();
                log.warn("project file" + projectParentFolder);
                if (parentProjectWithModule.equals(projectParentFolder)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if rules is defined in this project or a parent that is not part of the multi module
     */
    protected boolean ruleIsDefinedInProjectOrNotModuleParent(MavenProject project, Log log) {
        if (project == null) {
            return false;
        }

        log.debug("<<< " + project.toString() + " >>>");
        log.debug("project Parent: " + project.getParent());
        log.debug("project orig Model Parent: " + project.getOriginalModel().getParent());

        if (ruleDefinedInProject(project)) {
            return true;
        } else if (!projectIsSubmodule(project, log)) {
            return ruleIsDefinedInProjectOrNotModuleParent(project.getParent(), log);
        } else {
            return false;
        }
    }

    private boolean ruleDefinedInProject(MavenProject project) {
        for (BuildBase activeBuild : getDefinedActiveBuilds(project)) {
            final Plugin plugin = activeBuild.getPluginsAsMap().get("org.apache.maven.plugins:maven-enforcer-plugin");
            if (plugin != null) {
                for (PluginExecution execution : plugin.getExecutions()) {
                    if (isRuleInConfiguration(execution, this.getClass().getSimpleName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isRuleInConfiguration(PluginExecution execution, String ruleName) {
        final Xpp3Dom configuration = (Xpp3Dom) execution.getConfiguration();
        final Xpp3Dom rules = configuration.getChild("rules");
        if (rules != null && rules.getChild(ruleName) != null) {
            return true;
        }
        return false;
    }

    private Set<BuildBase> getDefinedActiveBuilds(MavenProject project) {
        HashSet<BuildBase> activeBuilds = new HashSet<>();
        final Model originalModel = project.getOriginalModel();
        final Build build = originalModel.getBuild();
        activeBuilds.add(build);

        final List<Profile> originalProfiles = originalModel.getProfiles();
        if (originalProfiles != null) {
            for (Profile profile : project.getActiveProfiles()) {
                // check active profile is defined in project
                for (Profile originalProfile : originalProfiles) {
                    if (originalProfile.equals(profile)) {
                        activeBuilds.add(originalProfile.getBuild());
                    }
                }
            }
        }
        // remove possible null entries
        activeBuilds.remove(null);
        return activeBuilds;
    }

    protected boolean projectHasManagedDependencies(MavenProject project) {
        return project.getDependencyManagement() != null && !project.getDependencyManagement().getDependencies().isEmpty();
    }
}
