create table member(id bigint auto_increment, email varchar(200) not null, name varchar(200));
alter table member add constraint member_uq_email unique(email);