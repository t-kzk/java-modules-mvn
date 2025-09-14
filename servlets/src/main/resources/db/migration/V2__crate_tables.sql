CREATE TABLE IF NOT EXISTS writers (
                                       id         INT PRIMARY KEY GENERATED ALWAYS AS identity,
                                       username VARCHAR(50) UNIQUE NOT NULL
);


CREATE TABLE IF NOT EXISTS files (
                                     id        INT PRIMARY KEY GENERATED ALWAYS AS identity,
                                     name   TEXT NOT NULL,
                                     file_path   TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
                                      id   INT PRIMARY KEY GENERATED ALWAYS AS identity,
                                      writer_id INT NOT NULL,
                                      file_id INT NOT NULL,
                                      FOREIGN KEY (writer_id) REFERENCES writers (id) ON DELETE CASCADE,
                                      FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);

