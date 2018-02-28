/***************************************************
****************Création des Tables*****************
***************************************************/

USE Pitson
GO

CREATE TABLE Machine
(
	idPresse int PRIMARY KEY IDENTITY (1,1),
	libellePresse TypeNomPresse UNIQUE NOT NULL,
	enService bit NOT NULL
)
GO

CREATE TABLE Modele
(
	idModele TypeIDModele PRIMARY KEY,
	diametre TypeMesure NOT NULL,
	obsolete BIT NOT NULL
)
GO


CREATE TABLE Categorie
(
	idCategorie TypeCategorie PRIMARY KEY,
	toleranceMini TypeMesure NULL,
	toleranceMaxi TypeMesure NULL
)
GO

CREATE TABLE Stock
(
	idModele TypeIDModele REFERENCES Modele (idModele) ON UPDATE CASCADE NOT NULL,
	idCategorie TypeCategorie REFERENCES Categorie (idCategorie) NOT NULL,
	qtStock int NOT NULL,
	seuilMini int NOT NULL,
	PRIMARY KEY (IdModele, IdCategorie)
)
GO 

CREATE TABLE Lot
(
	idLot int PRIMARY KEY IDENTITY (1,1),
	dateDemande DATETIME NOT NULL,
	dateProduction DATETIME NULL,
	etatProduction TypeEtat CHECK (etatProduction IN ('Attente', 'EnCours', 'Termine')) NOT NULL,
	etatControle TypeEtat CHECK (etatControle IN ('Attente', 'EnCours','Termine')) NOT NULL,
	idModele TypeIDModele REFERENCES Modele (idModele) ON UPDATE CASCADE,
	idPresse int,
	moyenneHL TypeStat NULL,
	moyenneHT TypeStat NULL,
	moyenneBL TypeStat NULL,
	moyenneBT TypeStat NULL,
	miniHL TypeMesure NULL,
	miniHT TypeMesure NULL,
	miniBL TypeMesure NULL,
	miniBT TypeMesure NULL,
	maxiHL TypeMesure NULL,
	maxiHT TypeMesure NULL,
	maxiBL TypeMesure NULL,
	maxiBT TypeMesure NULL,
	ecartTypeHL TypeStat NULL,
	ecartTypeHT TypeStat NULL,
	ecartTypeBL TypeStat NULL,
	ecartTypeBT TypeStat NULL,
	nbrPieceDemande int NOT NULL
)
GO

CREATE TABLE Piece
(
	idPiece int PRIMARY KEY IDENTITY (1,1),
	idLot int NOT NULL,
	idCategorie TypeCategorie NOT NULL,
	dimensionHL TypeMesure NOT NULL,
	dimensionHT TypeMesure NOT NULL,
	dimensionBL TypeMesure NOT NULL,
	dimensionBT TypeMesure NOT NULL,
	defautVisuel bit NOT NULL,
	commentaire TypeCommentaire NULL,
	FOREIGN KEY (idLot) REFERENCES Lot (idLot),
	FOREIGN KEY (idCategorie) REFERENCES Categorie (idCategorie)
)
GO

CREATE TABLE Cumul
(
	idLot int REFERENCES Lot(idLot) NOT NULL,
	idCategorie TypeCategorie REFERENCES Categorie (idCategorie) NOT NULL,
	nbrPiece int NOT NULL,
	PRIMARY KEY (idLot, idCategorie)
)
GO
