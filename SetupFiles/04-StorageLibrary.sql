CREATE TABLE StorageLibrary (
InstID int NOT NULL, 
StorageType Varchar(30), 
Room Varchar(5), 
Cabinet Varchar(1), 
Shelf smallint, 
PRIMARY KEY (InstID), 
FOREIGN KEY (InstID) REFERENCES Instrument(InstID)
);