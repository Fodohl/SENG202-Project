DROP TABLE IF EXISTS trash;
--SPLIT
CREATE TABLE trash (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    weight DECIMAL(10, 2));
--SPLIT
DROP TABLE IF EXISTS trash_log;
--SPLIT
CREATE TABLE trash_log (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    trash_id INTEGER,
    bin_id INTEGER,
    disposed_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT  fk_trash_log_trash
        FOREIGN KEY (trash_id)
        REFERENCES trash(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_trash_log_bin
        FOREIGN KEY (bin_id)
        REFERENCES bin(id)
        ON DELETE CASCADE);
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
