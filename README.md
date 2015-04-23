# ui-extra-enforcer-rules
## Rules
```xml
<forbidOverideDependencies
        implementation="net.oneandone.maven.rules.ForbidOveridingManagedDependenciesRule" />
```
Forbids overriding dependency management of parent.

```xml
<forbidOveridePlugins
        implementation="net.oneandone.maven.rules.ForbidOveridingManagedPluginsRule" />
```
Forbids overriding plugin management of parent.

```xml
<forbidManagmentInSubmodules
        implementation="net.oneandone.maven.rules.ForbidDependencyManagementInSubModulesRule" />
```
Forbids dependency management in submodules.

```xml
<excludes>
    <exclude>javax.validation:validation-api</exclude>
</excludes>
```
The rules above can define excludes. The check is a simple startsWith on `$groupId:$artifactId`

```xml
<manageAll implementation="net.oneandone.maven.rules.ManageAllModulesRule" />
```
Checks if all submodules of a multimodule are defined in dependency management.

All rules can be configured to let the build fail:
```xml
<shouldBuildFail>true</shouldBuildFail>
```

## Example

```xml
<build>
    <plugins>
        <plugin>
            <artifactId>maven-enforcer-plugin</artifactId>
            <dependencies>
                <dependency>
                    <groupId>net.oneandone.maven</groupId>
                    <artifactId>ui-extra-enforcer-rules</artifactId>
                    <version>0.1</version>
                </dependency>
            </dependencies>
            <configuration>
                <rules>
                    <forbidOverideDependencies
                            implementation="net.oneandone.maven.rules.ForbidOveridingManagedDependenciesRule">
                            <excludes>
                                <!-- guava in parent is too old, so allow to override it -->
                                <exclude>com.google.guava:guava</exclude>
                            </excludes>
                    </forbidOverideDependencies>
                    <forbidOveridePlugins
                            implementation="net.oneandone.maven.rules.ForbidOveridingManagedPluginsRule" />
                    <forbidManagmentInSubmodules
                            implementation="net.oneandone.maven.rules.ForbidDependencyManagementInSubModulesRule" />

                    <manageAll implementation="net.oneandone.maven.rules.ManageAllModulesRule">
                        <shouldBuildFail>true</shouldBuildFail>
                    </manageAll>
                </rules>
            </configuration>
        </plugin>

    </plugins>
</build>
```