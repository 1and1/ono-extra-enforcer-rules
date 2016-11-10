File buildLog = new File(basedir, 'build.log')
assert buildLog.text.contains ('module \'net.oneandone.maven.its:submodule-with-managed-dep-sub1:jar\' has dependency' +
        ' management for: net.oneandone.maven.its:dependency1:jar')