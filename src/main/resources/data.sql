CREATE TABLE IF NOT EXISTS DBMSG (
   id bigint auto_increment,
   mkey varchar(20) NOT NULL,
   val varchar(20) NOT NULL,
   status boolean
);