File buildLog = new File(basedir, 'build.log')
assert buildLog.text.contains (
'''[WARNING] Difference for: net.oneandone.maven.its:plugin1
[WARNING] Project: 1.1
[WARNING] Parent:  1.0
''')