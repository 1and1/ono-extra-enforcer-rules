File buildLog = new File(basedir, 'build.log')
assert buildLog.text =~ /Difference for: net.oneandone.maven.its:dependency1:jar.*\n.*Project: 1.1.*\n.*Parent:  1.0/