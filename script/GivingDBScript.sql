--
-- Create schema givingdb
--

CREATE DATABASE IF NOT EXISTS givingdb;
USE givingdb;

--
-- Definition of table currency
--
DROP TABLE IF EXISTS currency;
CREATE TABLE currency (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  symbol char(1) DEFAULT NULL,
  units varchar(10) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table currency
--
INSERT INTO currency (id,name,symbol,units) VALUES 
 (1,'US Dollars','$','USD'),
 (2,'AU Dollars','$','AUD'),
 (3,'CA Dollars','$','CAD'),
 (4,'Czech Koruna',NULL,'CZK'),
 (5,'Danish Kroner',NULL,'DKK'),
 (6,'Euros','€','EUR'),
 (7,'HK Dollars','$','HKD'),
 (8,'HU Forint',NULL,'HUF'),
 (9,'Norwegian Kroner',NULL,'NOK'),
 (10,'NZ Dollars','$','NZD'),
 (11,'Polish Zloty',NULL,'PLN'),
 (12,'Pounds Sterling','£','GBP'),
 (13,'SG Dollars','$','SGD'),
 (14,'Swedish Kronor',NULL,'SEK'),
 (15,'Swiss Francs',NULL,'CHF'),
 (16,'Yen','¥','JPY'),
 (17,'Mexican Pesos','$','MXN'),
 (18,'Israeli Shekels',NULL,'ILS'),
 (19,'Brazilian Real',NULL,'BRL'),
 (20,'Philippine Peso','₱','PHP'),
 (21,'Taiwan New Dollars','$','TWD'),
 (22,'Thai Baht','฿','THB');

--
-- Definition of table event
--
DROP TABLE IF EXISTS event;
CREATE TABLE event (
  id int(11) NOT NULL AUTO_INCREMENT,
  eventType varchar(20) NOT NULL,
  status varchar(20) NOT NULL,
  response longtext NOT NULL,
  created_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_dt timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table location
--
DROP TABLE IF EXISTS location;
CREATE TABLE location (
  id int(11) NOT NULL AUTO_INCREMENT,
  payerid varchar(50) DEFAULT NULL,
  country varchar(100) DEFAULT NULL,
  ipaddress varchar(50) DEFAULT NULL,
  host varchar(100) DEFAULT NULL,
  locale varchar(50) DEFAULT NULL,
  language varchar(100) DEFAULT NULL,
  created_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table render_location
--
DROP TABLE IF EXISTS render_location;
CREATE TABLE render_location (
  id int(11) NOT NULL AUTO_INCREMENT,
  payerid varchar(50) DEFAULT NULL,
  country varchar(100) DEFAULT NULL,
  ipaddress varchar(50) DEFAULT NULL,
  host varchar(100) DEFAULT NULL,
  locale varchar(50) DEFAULT NULL,
  language varchar(100) DEFAULT NULL,
  widgetexternalid varchar(50) NOT NULL,
  amount double NOT NULL,
  donateflag char(1) NOT NULL,
  created_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table transaction
--
DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction (
  id int(11) NOT NULL AUTO_INCREMENT,
  widgetexternalid varchar(50) NOT NULL,
  payerid varchar(50) DEFAULT NULL,
  emailid varchar(100) DEFAULT NULL,
  paypaltransid varchar(100) DEFAULT NULL,
  transresponse text,
  status varchar(50) DEFAULT NULL,
  amount double DEFAULT NULL,
  transactiontype varchar(50) DEFAULT NULL,
  created_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_dt timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table user
--
DROP TABLE IF EXISTS user;
CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT,
  payerid varchar(50) NOT NULL,
  emailid varchar(100) NOT NULL,
  firstname varchar(100) DEFAULT NULL,
  lastname varchar(100) DEFAULT NULL,
  correlationid varchar(100) DEFAULT NULL,
  version varchar(20) DEFAULT NULL,
  build varchar(20) DEFAULT NULL,
  granted_permission char(1) NOT NULL DEFAULT 'N',
  created_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_dt timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table widget
--
DROP TABLE IF EXISTS widget;
CREATE TABLE widget (
  id int(11) NOT NULL AUTO_INCREMENT,
  payerid varchar(50) NOT NULL,
  widgetexternalid varchar(50) NOT NULL,
  status char(1) NOT NULL,
  ein varchar(50) DEFAULT NULL,
  einVerified char(1) NOT NULL DEFAULT 'N',
  org_name varchar(260) DEFAULT NULL,
  vettingRequestId varchar(32) DEFAULT NULL,
  created_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_dt timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Definition of table widgetui
--
DROP TABLE IF EXISTS widgetui;
CREATE TABLE widgetui (
  id int(11) NOT NULL AUTO_INCREMENT,
  widgetid int(11) NOT NULL,
  payerid varchar(50) NOT NULL,
  themetype char(1) DEFAULT NULL,
  themevalue varchar(10) DEFAULT NULL,
  title varchar(50) DEFAULT NULL,
  titlecolor varchar(10) DEFAULT NULL,
  coverimageurl varchar(250) DEFAULT NULL,
  goal int(11) DEFAULT NULL,
  goalcurrency int(11) DEFAULT NULL,
  goalprogbcolor varchar(10) DEFAULT NULL,
  cause varchar(1000) DEFAULT NULL,
  weburl varchar(250) DEFAULT NULL,
  contribamts varchar(200) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
