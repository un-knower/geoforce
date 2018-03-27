FROM registry-internal.cn-hangzhou.aliyuncs.com/chaosting/openapi.tomcat:8.5.14_apr
MAINTAINER chaosting

ENV TZ=Asia/Shanghai LANG=en_US.utf8 LC_ALL=en_US.utf8
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ENV FILE_NAME=ROOT
ADD ./target/*.war /opt/tomcat/webapps
RUN unzip -q /opt/tomcat/webapps/*.war -d /opt/tomcat/webapps/$FILE_NAME && rm -rf /opt/tomcat/webapps/*.war