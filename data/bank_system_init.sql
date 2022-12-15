-- 创建数据库与数据表
drop database if exists bank_system;
create database bank_system 
default character set utf8; -- 设置字符集

-- 进入数据库 
use bank_system; 

-- utf-8 一个汉字三个字节 
create table users(
	card_id bigint primary key auto_increment,
    
    username varchar(30) not null, 
    
    passwd varchar(30) not null
    check (length(passwd) >= 4),
    
    
    user_id char(12) unique not null
    check(user_id regexp '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    
    phone_num char(11) unique not null
	check(phone_num regexp '1[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    
    sex enum('M', 'F') default 'M',
    
    birthday date,
    
    balance float(10, 2) not null
);


-- 展示scheme
desc users;

-- 删表
-- truncate table users;

-- 插入数据
insert into users
values
(5222412555, '陈之帆', 'mima', '320190939791', '18659829293', 'M', '2001-09-23', 100.03),
(0, '林川', 'mima', '320190940241', '18659829523', 'M', '2000-01-01', 20),
(0, '宋婕', 'mima', '320190932491', '18171711193', 'F', '2000-01-02', 21),
(0, '邵岚晔', 'mima', '320190939721', '13365929293', 'M', '2000-01-03', 0);

 
 select * from users;
