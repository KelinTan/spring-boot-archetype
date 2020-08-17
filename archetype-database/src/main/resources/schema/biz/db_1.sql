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
)