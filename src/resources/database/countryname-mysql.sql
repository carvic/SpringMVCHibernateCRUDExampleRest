##Mysql

createdatabase countrydata;

use countrydata;

#drop table country ;

create table country(
id bigint NOT NULL AUTO_INCREMENT,
countryname varchar(120),
population varchar(120),
PRIMARY KEY(id)
); 


insert into country (countryname,population) values ('España', '47000000');

select * from country;

commit;


