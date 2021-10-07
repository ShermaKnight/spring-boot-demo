create database if not exists orders default character set utf8mb4;
create database if not exists users default character set utf8mb4;

create table if not exists `users`.`tbl_user` (
    `id` bigint(11) not null auto_increment primary key,
    `user_name` varchar(20) default '' comment '用户名',
    `address` varchar(1024) default '' comment '用户居住地址',
    `salary` decimal(11,2) NOT NULL COMMENT '薪资',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;

create table if not exists `orders`.`tbl_order` (
    `id` bigint(11) not null auto_increment primary key,
    `user_id` bigint(11) not null comment '用户ID',
    `url` varchar(1024) default '' comment '订单链接',
    `price` decimal(11,2) NOT NULL COMMENT '订单价格',
    `status` tinyint not null default 0 comment '订单状态',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;
