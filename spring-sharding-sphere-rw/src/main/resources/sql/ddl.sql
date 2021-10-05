create database if not exists ds default character set utf8mb4;

create table if not exists `ds`.`tbl_user` (
    `id` bigint(11) not null auto_increment primary key,
    `user_name` varchar(20) default '' comment '用户名',
    `gender` tinyint(1) default '0' comment '性别 0表示男 1表示女',
    `salary` decimal(11,2) NOT NULL COMMENT '薪资',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;

create table if not exists `ds`.`tbl_address` (
    `id` bigint(11) not null auto_increment primary key,
    `user_id` bigint(11) not null,
    `receiver` varchar(20) default '' COMMENT '收件人',
    `address` varchar(1024) default '' comment '地址',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;
