# ono-extra-enforcer-rules

Latest Travis-Build: [![Build Status](https://travis-ci.org/1and1/ono-extra-enforcer-rules.svg?branch=master)](https://travis-ci.org/1and1/ono-extra-enforcer-rules)

## Rules
```xml
<ForbidOverridingManagedDependenciesRule />
```
Forbids overriding dependency management of parent.

```xml
<ForbidOverridingManagedPluginsRule />
```
Forbids overriding plugin management of parent.

```xml
<ForbidDependencyManagementInSubModulesRule />
```
Forbids dependency management in submodules.

```xml
<excludes>
    <exclude>javax.validation:validation-api</exclude>
</excludes>
```
The rules above can define excludes. The check is a simple startsWith on `$groupId:$artifactId`

```xml
<ManageAllModulesRule />
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
                    <artifactId>ono-extra-enforcer-rules</artifactId>
                    <version>${project.version}</version>
                </dependency>
            </dependencies>
            <configuration>
                <rules>
                    <ForbidOverridingManagedDependenciesRule>
                            <excludes>
                                <!-- guava in parent is too old, so allow to override it -->
                                <exclude>com.google.guava:guava</exclude>
                            </excludes>
                    </ForbidOverridingManagedDependenciesRule>
                    <ForbidOverridingManagedPluginsRule />
                    <ForbidDependencyManagementInSubModulesRule />
                    <ManageAllModulesRule />
                </rules>
            </configuration>
        </plugin>
    </plugins>
</build>
```