-- auto-generated definition
create table user
(
    username     varchar(256)                       null comment '用户昵称',
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    iphone       varchar(128)                       null comment '电话',
    email        varchar(512)                       not null comment '邮箱',
    userStatus   int      default 0                 null comment '状态-0正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    idDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    planetCode   varchar(512)                       null comment '星球编号'
)
    comment '用户' ;

alter table user add column tags varchar(1024) null comment '标签列表';

alter table user add column profile varchar(1024) null comment '个人简介';

-- 命令集中文待解决
ALTER TABLE `user` DEFAULT CHARACTER SET utf8;

INSERT INTO `user` VALUES ('admin', 1, 'admin', 'https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png', 0, '1a5914a939243dd0fa3556679abca904', '', 'admin@qq.com', 0, '2022-7-9 01:25:44', '2022-7-9 01:34:20', 0, 0, '1001', '[\"c++\",\"python\"]', 'I am admin');
INSERT INTO `user` VALUES ('testuser', 2, 'testuser', 'https://636f-codenav-8grj8px727565176-https://fastly.jsdelivr.net/npm/@vant/assets/weapp.svg', 0, '1a5914a939243dd0fa3556679abca904', '', 'testuser@qq.com', 0, '2022-7-9 01:26:46', '2022-7-9 01:34:21', 0, 0, '1002', '[\"c++\",\"python\"]', 'I am testuser');
INSERT INTO `user` VALUES ('klcadmin', 3, 'klcadmin', 'https://fastly.jsdelivr.net/npm/@vant/assets/github.svg', 1, '1a5914a939243dd0fa3556679abca904', '', 'klcadmin@qq.com', 0, '2022-7-9 01:27:06', '2022-7-9 01:34:22', 0, 1, '1003', '[\"c++\",\"python\"]', 'I am klcadmin');
INSERT INTO `user` VALUES ('testadmin', 4, 'testadmin', 'https://fastly.jsdelivr.net/npm/@vant/assets/logo.png', 1, '1a5914a939243dd0fa3556679abca904', '', 'testadmin@qq.com', 0, '2022-7-9 01:27:48', '2022-7-9 01:34:25', 0, 1, '1004', '[\"c++\",\"python\"]', 'I am testadmin');


-- auto-generated definition
create table tag
(
    id         bigint auto_increment comment 'id'
        primary key,
    tagName    varchar(256)                       null comment '标签名称',
    userId     bigint                             null comment '用户ID',
    parentId   bigint                             null comment '父标签ID',
    isParent   tinyint  default 0                 not null comment '是否为父标签 0-不是 1-是',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    idDelete   tinyint  default 0                 not null comment '是否删除',
    constraint uniIdx_tagName
        unique (tagName)
)
    comment '标签';

create index idx_userId
    on tag (userId);



