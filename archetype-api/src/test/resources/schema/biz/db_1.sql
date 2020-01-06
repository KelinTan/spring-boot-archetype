DROP TABLE IF EXISTS `biz_account`;
create table biz_account
(
id int IDENTITY primary key not null ,
account varchar(20) null,
password varchar(30) null,
token varchar(2505) null
)