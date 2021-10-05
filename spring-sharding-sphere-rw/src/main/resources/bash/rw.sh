# master节点部署
mkdir -p /opt/mysql/master/lib/mysql
mkdir -p /opt/mysql/master/conf.d

cat << EOF > /opt/mysql/master/conf.d/docker.cnf
[mysqld]
log-bin=mysql-bin
server-id=1
EOF

docker run -d --name mysql-master \
-e MYSQL_ROOT_PASSWORD=FN5HW5Y1XQJ742I7 \
-v /opt/mysql/master/lib/mysql:/var/lib/mysql \
-v /opt/mysql/master/conf.d:/etc/mysql/conf.d \
-p 3306:3306 mysql:5.7

# slave0节点部署
mkdir -p /opt/mysql/slave0/lib/mysql
mkdir -p /opt/mysql/slave0/conf.d

cat << EOF > /opt/mysql/slave0/conf.d/docker.cnf
[mysqld]
log-bin=mysql-bin
server-id=10
EOF

docker run -d --name mysql-slave0 \
-e MYSQL_ROOT_PASSWORD=FN5HW5Y1XQJ742I7 \
-v /opt/mysql/slave0/lib/mysql:/var/lib/mysql \
-v /opt/mysql/slave0/conf.d:/etc/mysql/conf.d \
-p 3307:3306 mysql:5.7

# slave1节点部署
mkdir -p /opt/mysql/slave1/lib/mysql
mkdir -p /opt/mysql/slave1/conf.d

cat << EOF > /opt/mysql/slave1/conf.d/docker.cnf
[mysqld]
log-bin=mysql-bin
server-id=11
EOF

docker run -d --name mysql-slave1 \
-e MYSQL_ROOT_PASSWORD=FN5HW5Y1XQJ742I7 \
-v /opt/mysql/slave1/lib/mysql:/var/lib/mysql \
-v /opt/mysql/slave1/conf.d:/etc/mysql/conf.d \
-p 3308:3306 mysql:5.7

# master节点配置主从
docker exec -it mysql-master mysql -uroot -pFN5HW5Y1XQJ742I7 -e "grant replication slave on *.* to 'replicate'@'%' identified by 'MLNTZ5QDUXBAGRR3';"
docker exec -it mysql-master mysql -uroot -pFN5HW5Y1XQJ742I7 -e "flush privileges;"
docker exec -it mysql-master mysql -uroot -pFN5HW5Y1XQJ742I7 -e "show master status;"

# slave节点配置主从
docker exec -it mysql-slave0 mysql -uroot -pFN5HW5Y1XQJ742I7 -e "change master to master_host='192.168.71.128',master_port=3306,master_user='replicate',master_password='MLNTZ5QDUXBAGRR3',master_log_file='mysql-bin.000003',master_log_pos=154;"
docker exec -it mysql-slave0 mysql -uroot -pFN5HW5Y1XQJ742I7 -e "start slave;"
docker exec -it mysql-slave0 mysql -uroot -pFN5HW5Y1XQJ742I7 -e "show slave status\G" | grep Running

docker exec -it mysql-slave1 mysql -uroot -pFN5HW5Y1XQJ742I7 -e "change master to master_host='192.168.71.128',master_port=3306,master_user='replicate',master_password='MLNTZ5QDUXBAGRR3',master_log_file='mysql-bin.000003',master_log_pos=154;"
docker exec -it mysql-slave1 mysql -uroot -pFN5HW5Y1XQJ742I7 -e "start slave;"
docker exec -it mysql-slave1 mysql -uroot -pFN5HW5Y1XQJ742I7 -e "show slave status\G" | grep Running