CREATE TABLE InstrumentExhibit (
ExhibitID int NOT NULL, 
InstID int NOT NULL, 
Room Varchar(5), 
LocationInRoom Varchar(16),
CONSTRAINT pk_InstrumentExhibit PRIMARY KEY (ExhibitID, InstID),
FOREIGN KEY (ExhibitID) REFERENCES Exhibit(ExhibitID),
FOREIGN KEY (InstID) REFERENCES Instrument(InstID)
);