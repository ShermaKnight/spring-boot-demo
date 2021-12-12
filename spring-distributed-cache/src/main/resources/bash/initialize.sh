#!/bin/bash

# docker部署
apt-get install -y docker.io

# docker配置
cat << EOF > /etc/docker/daemon.json
{
  "registry-mirrors": [
    "https://hub-mirror.c.163.com",
    "https://docker.mirrors.ustc.edu.cn/"
  ],
  "debug": true,
  "experimental": true
}
EOF

sudo systemctl daemon-reload
sudo systemctl restart docker

# redis部署
docker run -d --name redis \
--restart always \
-v /etc/localtime:/etc/localtime \
-v /opt/redis:/data \
-p 6379:6379 \
redis redis-server --save 60 1 --loglevel warning


