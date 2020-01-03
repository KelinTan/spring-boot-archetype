DROP TABLE IF EXISTS `t_user`;
create table t_user
(
id int IDENTITY primary key not null ,
user_name varchar(20) null
)