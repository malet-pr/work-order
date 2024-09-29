truncate table job_type cascade;
ALTER SEQUENCE job_type_seq RESTART WITH 1;

insert into job_type values
('Y',NOW(),1,'consumer','type1','job type name 1'),
('Y',NOW(),2,'corporate','type2','job type name 2'),
('N',NOW(),3,'corporate','type3','job type name 3');

ALTER SEQUENCE job_type_seq RESTART WITH 10;