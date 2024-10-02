truncate table job cascade;
ALTER SEQUENCE job_seq RESTART WITH 1;

insert into job (active_status,creation_date,id,code,name)
values
('Y',NOW(),1,'JobCode1','job name 1'),
('Y',NOW(),2,'JobCode2','job name 2'),
('N',NOW(),3,'JobCode3','job name 3'),
('Y',NOW(),4,'JobCode4','job name 4');

ALTER SEQUENCE job_seq RESTART WITH 10;