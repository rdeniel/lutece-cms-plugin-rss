ALTER TABLE rss_generation ADD COLUMN max_items int DEFAULT 0;
ALTER TABLE rss_generation ADD COLUMN feed_type varchar(50) DEFAULT 'rss_2.0';
ALTER TABLE rss_generation ADD COLUMN feed_encoding varchar(50) DEFAULT 'UTF-8';
