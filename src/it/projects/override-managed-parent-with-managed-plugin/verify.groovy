File buildLog = new File(basedir, 'build.log')
assert buildLog.text =~ /Difference for: net.oneandone.maven.its:plugin1.*\n.*Project: 1.1.*\n.*Parent:  1.0/