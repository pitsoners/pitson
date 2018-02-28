USE Pitson;
GO

CREATE VIEW LotsAttenteProduction
AS
	SELECT idLot, dateDemande, idModele
	FROM Lot
	WHERE etatProduction = 'Attente';
GO

CREATE VIEW LotsEnCoursProduction
AS
	SELECT idLot, dateDemande, idModele, dateProduction, idPresse
	FROM Lot
	WHERE etatProduction = 'EnCours';
go
