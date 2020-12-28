DROP TABLE IF EXISTS `biz_account`;
create table biz_account
(
id int identity primary key not null ,
account varchar(20) null,
password varchar(30) null,
token varchar(2505) null,
age int(11) null,
height int(11) null,
money decimal(10,2) null,
birth_date timestamp null,
verify boolean null
);

DROP TABLE IF EXISTS `id_sharding_0`;
create table id_sharding_0
(
id int identity primary key not null ,
data varchar(20) null
);

DROP TABLE IF EXISTS `id_sharding_1`;
create table id_sharding_1
(
id int identity primary key not null ,
data varchar(20) null
)