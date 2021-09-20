FROM openjdk:11
VOLUME /tmp
ENV LANG C.UTF-8
# dockerfile-maven-plugin配置
ARG JAR_FILE
# 将jar包添加到容器中并更名为app.jar
ADD ${JAR_FILE} app.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo Asia/Shanghai >/etc/timezone
# 运行jar包
RUN bash -c "touch /app.jar"
EXPOSE 8080
# 以profile = prod 启动项目
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/app.jar"]