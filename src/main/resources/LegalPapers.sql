drop database if exists `practice`;
create database `practice` default character set utf8 collate utf8_general_ci;

use practice;

drop table if exists `appendix`;
drop table if exists `papers`;
drop table if exists `user`;

create table user
(
    id       int          not null auto_increment comment '用户id 主键',
    username varchar(50)  not null comment '用户名称',
    password varchar(100) not null comment '密码',
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table analyse
(
    id         int          not null auto_increment comment '内化法规id 主键',
    title      varchar(200) not null comment '法规标题',
    number     varchar(100) comment '法规文号',
    category   varchar(50)  not null comment '外规类别',
    interpret  varchar(50)  not null comment '解读部门',
    user_id    int          not null comment '录入人用户id',
    input_time datetime     not null comment '录入时间',
    content    text         not null comment '文本内容',
    primary key (id),
    foreign key (user_id) references user (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table papers
(
    id             int          not null auto_increment comment '法规id 主键',
    title          varchar(200) not null comment '法规标题',
    number         varchar(100) comment '法规文号',
    category       varchar(50)  not null comment '外规类别',
    department     varchar(100) not null comment '发文部门',
    release_time   datetime     not null comment '发布时间',
    implement_time datetime     not null comment '实施时间',
    grade          varchar(20)  not null comment '效力等级',
    interpret      varchar(50)  not null comment '解读部门',
    user_id        int          not null comment '录入人用户id',
    input_time     datetime     not null comment '录入时间',
    content        text         not null comment '文本内容',
    status         int          not null comment '是否发布 0 1 表示',
    analyse_id     int          not null comment '内化法规id -1表示未内化',
    primary key (id),
    foreign key (user_id) references user (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

create table appendix
(
    id        int          not null auto_increment comment '附件id',
    file_name varchar(200) not null comment '文件名 阿里云oss中',
    user_name varchar(100) not null comment '上传用户',
    paper_id  int          not null comment '法规id 正数为法规 负数为内化法规',
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


insert into `user`
values (1, 'admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');