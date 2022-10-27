FROM java:8
# 作者
MAINTAINER lwj
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp
#设置语言编码和时区
ENV LANG C.UTF-8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 将jar包添加到容器中并更名为demo.jar
ADD /target/*.jar demo.jar
# 运行jar包
EXPOSE 10000
ENTRYPOINT ["java","-jar","demo.jar"]