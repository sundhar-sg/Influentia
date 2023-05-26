create database InfluentiaDB;

use InfluentiaDB;

create table UserPosts(
	id int primary key auto_increment,
    postedOn date default(current_date()),
    isScheduledPost boolean,
    publishedOnDate date,
    publishedOnTime time,
    postType varchar(10),
    postContentText varchar(15000),
    postAttachmentURL varchar(200),
    postStatus varchar(10),
    userName varchar(10),
    socialNetworkType varchar(20),
    constraint chk_UserPosts check(
        lower(postType) in ("text", "image", "video") and
        lower(postStatus) in ("scheduled", "cancelled") and
        lower(socialNetworkType) in ("facebook", "instagram", "twitter", "youtube", "linkedin")
    )
);

delimiter //
Create trigger before_insert_publishDate 
BEFORE INSERT on userposts FOR EACH ROW 
BEGIN 
IF NEW.publishedOnDate < curdate() 
then SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid Published Date!';
end if;
END;
//
delimiter ;

drop trigger before_insert_publishDate;

create table subscriptionPlanLimits(
	id int primary key auto_increment,
    userName varchar(10),
    planName varchar(100),
    monthlyScheduledPostLimit int
);

delimiter //
CREATE TRIGGER before_insert_postLimit 
BEFORE INSERT ON subscriptionPlanLimits FOR EACH ROW 
BEGIN 
IF NEW.planName IN ('Pro', 'pro', 'PRO') THEN 
SET NEW.monthlyScheduledPostLimit = 150; 
ELSEIF NEW.planName IN ('Basic', 'basic', 'BASIC') THEN 
SET NEW.monthlyScheduledPostLimit = 5; 
ELSE 
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid Plan Name!';
END IF;
END;
//
delimiter ;

select * from userposts;
select * from userposts where username = "sundhar_sg";
describe userposts;
describe subscriptionplanlimits;
insert into userposts values(3, current_date(), 1, date_add(current_date(), interval 2 day), timestampadd(HOUR, 9, date_add(current_date(), interval 3 day)), 'Text', 'Sample Post for Checking DB', 'https://www.instagram.com/sundhar-sg/067358342sdsd', 'Scheduled', 'sundhar-sg', 'Instagram');
select * from subscriptionplanlimits;

alter table userposts modify column id int auto_increment;


-- resetting auto increment values for primary key fields
ALTER TABLE userposts DROP id;
alter table userposts auto_increment = 1;
ALTER TABLE userposts ADD id int AUTO_INCREMENT PRIMARY KEY FIRST;

-- set time_zone = '-05:30';
-- select @@system_time_zone;
-- select now();