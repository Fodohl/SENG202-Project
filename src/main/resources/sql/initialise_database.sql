DROP TABLE IF EXISTS trash_logs;
--SPLIT
DROP TABLE IF EXISTS bins;
--SPLIT
CREATE TABLE bins (
    id INTEGER PRIMARY KEY,
    lat DECIMAL(5, 20) NOT NULL,
    lng DECIMAL(5, 20) NOT NULL,
    waste_type VARCHAR(255),
    updated_at INTEGER,
    photo_url VARCHAR(255),
    area VARCHAR(255),
    UNIQUE (lat, lng));
--SPLIT
CREATE TABLE trash_logs (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    created_at INTEGER NOT NULL,
    disposed_at INTEGER NOT NULL,
    user_id INTEGER,
    bin_id INTEGER,
    type VARCHAR(255),
    weight DECIMAL(10, 2),
    CONSTRAINT fk_trash_log_bin
       FOREIGN KEY (bin_id)
           REFERENCES bins(id)
           ON DELETE CASCADE);