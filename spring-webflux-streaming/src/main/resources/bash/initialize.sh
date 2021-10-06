#!/bin/bash

mkdir -p /opt/redis/data

docker run --name redis -d \
-v /opt/redis/data:/data \
-p 6379:6379 redis:5.0.13

