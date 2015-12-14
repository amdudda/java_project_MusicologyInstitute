CREATE TABLE Contact (
ContactID int NOT NULL AUTO_INCREMENT, 
ContactName Varchar(100), 
BusinessName Varchar(100), 
Address Varchar(300), 
City Varchar(75), 
State Varchar(2), 
PostalCode Varchar(6), 
Country Varchar(100), 
BusinessPhone Varchar(15), 
ContactPhone Varchar(15), 
ContactType Varchar(30), 
Notes Varchar(1500), 
PRIMARY KEY (ContactID),
FOREIGN KEY (Country) REFERENCES Countries(CountryName)
);