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

import java.util.Objects;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.enforcer.AbstractNonCacheableEnforcerRule;

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

}
