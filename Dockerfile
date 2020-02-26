# https://chanjarster.github.io/post/java-dockerfile-best-practice/
# https://github.com/fabianenardon/docker-java-issues-demo
FROM javachen/openjdk-ffmpeg:8u201-jdk-alpine

# 确保 ${user.home} 可用
WORKDIR /data

ARG APPJAR=target/*.jar

COPY docker-entrypoint.sh docker-entrypoint.sh
COPY ${APPJAR} app.jar

ENTRYPOINT ["/data/docker-entrypoint.sh"]