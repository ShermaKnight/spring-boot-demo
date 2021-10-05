create database if not exists ds0 default character set utf8mb4;
create database if not exists ds1 default character set utf8mb4;

create table if not exists `ds0`.`tbl_order_1` (
    `id` bigint(11) not null primary key,
    `user_id` bigint(11) not null,
    `course_name` varchar(20) default '' comment '课程名称',
    `price` decimal(11,2) NOT NULL COMMENT '单价',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;

create table if not exists `ds0`.`tbl_order_2` (
    `id` bigint(11) not null primary key,
    `user_id` bigint(11) not null,
    `course_name` varchar(20) default '' comment '课程名称',
    `price` decimal(11,2) NOT NULL COMMENT '单价',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;

create table if not exists `ds1`.`tbl_order_1` (
    `id` bigint(11) not null primary key,
    `user_id` bigint(11) not null,
    `course_name` varchar(20) default '' comment '课程名称',
    `price` decimal(11,2) NOT NULL COMMENT '单价',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;

create table if not exists `ds1`.`tbl_order_2` (
    `id` bigint(11) not null primary key,
    `user_id` bigint(11) not null,
    `course_name` varchar(20) default '' comment '课程名称',
    `price` decimal(11,2) NOT NULL COMMENT '单价',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;

-- 未配置分片规则的表将通过默认数据源定位
create database if not exists sharding default character set utf8mb4;

create table if not exists `sharding`.`tbl_course` (
    `id` bigint(11) not null primary key,
    `course_name` varchar(20) default '' comment '课程名称',
    `price` decimal(11,2) NOT NULL COMMENT '单价',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) engine = InnoDB default charset = utf8mb4;