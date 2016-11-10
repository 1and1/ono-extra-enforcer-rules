File buildLog = new File(basedir, 'build.log')
assert buildLog.text.contains ('unmanaged project found: net.oneandone.maven.its:unmanaged-submodules-sub1')
