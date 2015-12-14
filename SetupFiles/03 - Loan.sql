CREATE TABLE Loan (
InstID int NOT NULL, 
ContactID int NOT NULL, 
StartDate date, 
EndDate date, 
CONSTRAINT pk_Loan PRIMARY KEY (InstID,ContactID),
FOREIGN KEY (InstID) REFERENCES Instrument(InstID),
FOREIGN KEY (ContactID) REFERENCES Contact(ContactID)
);