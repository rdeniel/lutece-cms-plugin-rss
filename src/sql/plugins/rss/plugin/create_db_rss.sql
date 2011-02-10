--
-- Structure for table rss_feed
--
DROP TABLE IF EXISTS rss_feed;
CREATE TABLE rss_feed (
  id_rss_feed int DEFAULT '0' NOT NULL ,
  name varchar(255) DEFAULT NULL,
  url varchar(255) DEFAULT NULL,
  last_fetch_date datetime DEFAULT NULL,
  last_fetch_status int DEFAULT NULL,
  last_fetch_error varchar(255) DEFAULT NULL,
  workgroup_key varchar(50) DEFAULT NULL,
  include_style int default -1,
  PRIMARY KEY  (id_rss_feed)
);


--
-- Structure for table rss_generation
--
DROP TABLE IF EXISTS rss_generation;
CREATE TABLE rss_generation (
  id_rss int DEFAULT '0' NOT NULL ,
  id_portlet int DEFAULT NULL ,
  name varchar(100) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  state smallint DEFAULT '0',
  date_update timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL ,
  workgroup_key varchar(50) DEFAULT NULL,
  type_resource_rss varchar(50) DEFAULT NULL,
  PRIMARY KEY  (id_rss)
);

CREATE INDEX index_rss_portlet ON rss_generation(id_portlet);
