DROP TABLE IF EXISTS trash_logs;
--SPLIT
DROP TABLE IF EXISTS bins;
--SPLIT
DROP TABLE IF EXISTS users;
--SPLIT
DROP TABLE IF EXISTS status_reports;
--SPLIT
DROP TABLE IF EXISTS community_cleanliness;
--SPLIT
DROP TABLE IF EXISTS leaderboard;
--SPLIT
CREATE TABLE bins (
    id INTEGER PRIMARY KEY,
    lat DECIMAL(11, 8) NOT NULL,
    lng DECIMAL(11, 8) NOT NULL,
    waste_type VARCHAR(255),
    updated_at BIGINT,
    photo_url VARCHAR(255),
    area VARCHAR(255),
    fullness VARCHAR(255) NOT NULL DEFAULT 'UNKNOWN',

    UNIQUE (lat, lng)

);

--SPLIT
CREATE TABLE trash_logs (

    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    created_at BIGINT NOT NULL,
    disposed_at BIGINT NOT NULL,
    synced_at BIGINT,
    user_id INTEGER NOT NULL,
    bin_id INTEGER NOT NULL,
    type VARCHAR(255) NOT NULL,
    weight DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_trash_log_bin FOREIGN KEY (bin_id) REFERENCES bins(id) ON DELETE CASCADE
);

--SPLIT
CREATE TABLE users (

   id INTEGER PRIMARY KEY AUTO_INCREMENT,
   created_at BIGINT NOT NULL,
   updated_at BIGINT,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL UNIQUE,
   password_hash VARCHAR(255) NOT NULL,
   reset_code INTEGER

);

--SPLIT
CREATE TABLE status_reports (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    bin_id INTEGER,
    user_id INTEGER,
    fullness VARCHAR(255),
    issue VARCHAR(255),
    description VARCHAR(255),
    created_at BIGINT NOT NULL,
    synced_at BIGINT,

    CONSTRAINT fk_status_report_bin FOREIGN KEY (bin_id) REFERENCES bins(id) ON DELETE CASCADE,
    CONSTRAINT fk_status_report_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

--SPLIT
CREATE TABLE community_cleanliness (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    log_count INTEGER NOT NULL DEFAULT 0,
    updated_at BIGINT NOT NULL,

    UNIQUE (year, month)
);


--SPLIT
CREATE TABLE leaderboard (
    place INTEGER PRIMARY KEY,
    user_id INTEGER NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    log_count INTEGER NOT NULL,
    tree_days DECIMAL(10, 2) NOT NULL,
    updated_at BIGINT NOT NULL,

    CONSTRAINT fk_leaderboard_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);