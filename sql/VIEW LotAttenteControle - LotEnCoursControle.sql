USE Pitson;
GO

CREATE VIEW LotsAttenteControle
AS
	SELECT idLot, dateDemande, idModele
	FROM Lot
	WHERE etatControle = 'Attente' AND etatProduction <> 'Attente';
GO

CREATE VIEW LotsEnCoursControle
AS
	SELECT idLot, dateDemande, idModele, dateProduction, idPresse
	FROM Lot
	WHERE etatControle = 'EnCours';
go
