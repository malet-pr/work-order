truncate table work_order cascade;
ALTER SEQUENCE work_order_seq RESTART WITH 1;
ALTER SEQUENCE job_seq RESTART WITH 1;
ALTER SEQUENCE job_type_seq RESTART WITH 1;

insert into job_type values
('Y',NOW(),1,'consumer','type1','job type name 1'),
('Y',NOW(),2,'corporate','type2','job type name 2'),
('N',NOW(),3,'corporate','type3','job type name 3');

ALTER SEQUENCE job_type_seq RESTART WITH 10;

insert into job values
('Y',NOW(),1,'jobCode1','job name 1'),
('Y',NOW(),2,'jobCode2','job name 2'),
('N',NOW(),3,'jobCode3','job name 3'),
('Y',NOW(),4,'jobCode4','job name 4');

ALTER SEQUENCE job_seq RESTART WITH 10;

insert into work_order values
(NOW(),1,1,CURRENT_DATE-1,CURRENT_DATE-3,'addres1','A1','city1','client1','state1','ABC123'),
(NOW(),2,2,CURRENT_DATE-2,CURRENT_DATE-4,'addres2','A2','city2','client2','state2','ABC456'),
(NOW(),3,null,CURRENT_DATE-2,CURRENT_DATE-4,'addres3','','city3','client3','state3','ZZZ999');

ALTER SEQUENCE work_order_seq RESTART WITH 10;

insert into wo_job values
('Y',5,NOW(),1,1,1,''),
('N',1,NOW(),2,4,1,'A1'),
('Y',3,NOW(),3,2,2,''),
('Y',4,NOW(),4,1,2,'A2');

ALTER SEQUENCE work_order_job_seq RESTART WITH 10;


