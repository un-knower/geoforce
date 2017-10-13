FROM registry-internal.cn-hangzhou.aliyuncs.com/chaosting/geoforce.tomcat:8.5.23
MAINTAINER chaosting

ENV FILE_NAME=saas
ADD ./target/*.war /usr/local/tomcat/webapps
RUN unzip -q /usr/local/tomcat/webapps/*.war -d /usr/local/tomcat/webapps/$FILE_NAME && \
    rm -rf /usr/local/tomcat/webapps/*.war