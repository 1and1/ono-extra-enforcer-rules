File buildLog = new File(basedir, 'build.log')
assert buildLog.text.contains ('[WARNING] unmanaged project found: net.oneandone.maven.its:unmanaged-submodules-sub1')
