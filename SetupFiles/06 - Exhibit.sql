CREATE TABLE Exhibit (
ExhibitID int NOT NULL AUTO_INCREMENT, 
ExhibitName Varchar(100), 
StartDate date, 
EndDate date, 
Room Varchar(5), 
PRIMARY KEY (ExhibitID), 
FOREIGN KEY (Room) REFERENCES Rooms(RoomNum)
);