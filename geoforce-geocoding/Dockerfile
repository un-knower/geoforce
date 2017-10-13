FROM registry-internal.cn-hangzhou.aliyuncs.com/chaosting/geoforce.smo:712_12428_49334
MAINTAINER chaosting

ENV FILE_NAME=geocoding
ADD ./target/$FILE_NAME.tar.gz /opt/
RUN chown root:root /opt/$FILE_NAME/ -R && \
    chmod 755 /opt/$FILE_NAME/ -R
WORKDIR /opt/$FILE_NAME/