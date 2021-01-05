DROP TABLE IF EXISTS `year_sharding_2020`;
create table year_sharding_2020
(
id int identity primary key not null ,
sharding_time varchar(20) not null ,
data varchar(20) null
)
;
DROP TABLE IF EXISTS `year_sharding_2021`;
create table year_sharding_2021
(
id int identity primary key not null ,
sharding_time varchar(20) not null,
data varchar(20) null
)