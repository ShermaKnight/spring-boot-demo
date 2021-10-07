create database if not exists quartz default character set utf8mb4;

create table if not exists `quartz`.`user` (
    `id` bigint(11) not null auto_increment primary key,
    `user_name` varchar(20) default '' comment '用户名',
    `address` varchar(1024) default '' comment '用户居住地址',
    `salary` decimal(11,2) NOT NULL COMMENT '薪资',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;

