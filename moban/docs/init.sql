create database if not exists moban default character set utf8mb4;
use moban;

create table user_info
(
    id            bigint auto_increment comment '唯一ID'
#         当新记录插入时，如果没有显式提供 id 的值，数据库会自动生成一个新的唯一值（从1开始，依次递增）。
        primary key,
    clazz_id      bigint        null comment '班级ID',
    account       varchar(20)   null comment '账号',
    user_name     varchar(20)   null comment '名字',
    password      varchar(70)   not null comment '密码（加密后的密码）',
    email         varchar(20)   null comment '电子邮箱',
    phone         varchar(20)   null comment '电话',
    method        varchar(1000) null comment '教学方法',
    created       varchar(20)   null comment '创建人',
    created_date  timestamp     null comment '创建时间',
    modified      varchar(20)   null comment '修改人',
    modified_date timestamp     null comment '更新时间',
    KEY `user_name` (`user_name`)
) comment '用户信息表';

create table student_info
(
    id            bigint auto_increment comment '学号'
        primary key,
    clazz_id      bigint      null comment '班级ID',
    name          varchar(20) not null comment '姓名',
    created       varchar(20) null comment '创建人',
    created_date  timestamp   null comment '创建时间',
    modified      varchar(20) null comment '修改人',
    modified_date timestamp   null comment '更新时间',
    KEY `clazz_id` (`clazz_id`),
    KEY `name` (`name`)
) comment '学生信息表';

create table clazz_info
(
    id            bigint auto_increment comment '唯一ID'
        primary key,
    grade_id      bigint      null comment '年级ID(冗余字段)',
    grade         varchar(70) not null comment '年级',
    school_name   varchar(20) not null comment '学校名称',
    clazz_name    varchar(20) not null comment '班级名',
    user_id       bigint      not null comment '用户ID',
    created       varchar(20) null comment '创建人',
    created_date  timestamp   null comment '创建时间',
    modified      varchar(20) null comment '修改人',
    modified_date timestamp   null comment '更新时间',
    KEY `grade` (`grade`)
) comment '班级信息表';

create table paper_manage
(
    id             bigint auto_increment comment '唯一ID'
        primary key,
    name           varchar(20)   null comment '试卷名称',
    clazz_id       bigint        not null comment '班级ID',
    subject_id     bigint        null comment '科目ID(冗余字段)',
    subject_name   varchar(20)   null comment '科目名称',
    teacher_id     bigint        null comment '阅卷老师ID',
    teacher        varchar(20)   null comment '阅卷老师名称',
    grades         decimal(5, 1) null comment '成绩',
    extended_field varchar(255)  null comment '扩展字段',
    paper_time     timestamp     null comment '时间',
    created        varchar(20)   null comment '创建人',
    created_date   timestamp     null comment '创建时间',
    modified       varchar(20)   null comment '修改人',
    modified_date  timestamp     null comment '更新时间',
    KEY `clazz_id` (`clazz_id`),
    KEY `subject_name` (`subject_name`)
) comment '试卷信息表';
