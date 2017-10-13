FROM registry-internal.cn-hangzhou.aliyuncs.com/chaosting/geoforce.jdk:1.8.0_141
MAINTAINER chaosting

ENV FILE_NAME=point
ADD ./target/$FILE_NAME.tar.gz /opt/
RUN chown root:root /opt/$FILE_NAME/ -R && \
    chmod 755 /opt/$FILE_NAME/ -R
WORKDIR /opt/$FILE_NAME/