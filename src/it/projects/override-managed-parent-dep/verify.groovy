File buildLog = new File(basedir, 'build.log')
assert buildLog.text.contains (
'''[WARNING] Difference for: net.oneandone.maven.its:dependency1:jar
[WARNING] Project: 1.1
[WARNING] Parent:  1.0
''')