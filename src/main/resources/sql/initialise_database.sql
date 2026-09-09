DROP TABLE IF EXISTS trash_log;
--SPLIT
DROP TABLE IF EXISTS bin;
--SPLIT
CREATE TABLE bin (
     id INTEGER PRIMARY KEY,
     waste_type VARCHAR(255),
     photo_url VARCHAR(255),
     lat DECIMAL(5, 20) NOT NULL,
     lng DECIMAL(5, 20) NOT NULL,
     UNIQUE (lat, lng));
--SPLIT
CREATE TABLE trash_log (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    created_at DATETIME NOT NULL,
    disposed_at DATETIME NOT NULL,
    user_id INTEGER,
    bin_id INTEGER,
    type VARCHAR(255),
    weight DECIMAL(10, 2),
    CONSTRAINT fk_trash_log_bin
       FOREIGN KEY (bin_id)
           REFERENCES bin(id)
           ON DELETE CASCADE);