# ono-extra-enforcer-rules

Latest Travis-Build: [![Build Status](https://travis-ci.org/1and1/ono-extra-enforcer-rules.svg?branch=master)](https://travis-ci.org/1and1/ono-extra-enforcer-rules)

## Rules
```xml
<forbidOverridingManagedDependencies
        implementation="net.oneandone.maven.rules.ForbidOverridingManagedDependenciesRule" />
```
Forbids overriding dependency management of parent.

```xml
<forbidOverridingManagedPlugins
        implementation="net.oneandone.maven.rules.ForbidOverridingManagedPluginsRule" />
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
                    <version>${project.version}</version>
                </dependency>
            </dependencies>
            <configuration>
                <rules>
                    <forbidOverridingManagedDependencies
                            implementation="net.oneandone.maven.rules.ForbidOverridingManagedDependenciesRule">
                            <excludes>
                                <!-- guava in parent is too old, so allow to override it -->
                                <exclude>com.google.guava:guava</exclude>
                            </excludes>
                    </forbidOverridingManagedDependencies>
                    <forbidOverridingManagedPlugins
                            implementation="net.oneandone.maven.rules.ForbidOverridingManagedPluginsRule" />
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