FROM eclipse-temurin:23-jdk

ADD build/libs/git.demo.jar /
ENTRYPOINT ["java","-server","-XX:-UseGCOverheadLimit","-XX:+DisableExplicitGC","-XX:SurvivorRatio=1","-XX:LargePageSizeInBytes=128M","-XX:SoftRefLRUPolicyMSPerMB=0","-Djava.security.egd=file:/dev/./urandom","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8085","-jar","/git.demo.jar"]