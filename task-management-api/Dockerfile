FROM openjdk:17
WORKDIR /opt
COPY build/libs/*.jar task-management.jar
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
CMD ["java", "-jar", "task-management.jar"]