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

# mongo部署
docker run -d --name mongo \
--restart always \
-v /etc/localtime:/etc/localtime \
-e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
-e MONGO_INITDB_ROOT_PASSWORD=secret \
-p 27017:27017 mongo:5.0
