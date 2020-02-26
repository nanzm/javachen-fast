#!/bin/sh

set -ex;

/usr/bin/java \
  $JAVA_OPTS \
  -XX:InitialRAMPercentage=10.0 -XX:MaxRAMPercentage=50.0 -XshowSettings:vm \
  -Djava.security.egd=file:/dev/./urandom \
  -jar /data/app.jar \
  "$@"