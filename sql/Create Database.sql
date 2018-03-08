
/* Création des types */

/* type mesures (utilisé pour les mesures dans PIECE, les marges de tolérance dans CATEGORIES, le diamètre dans MODELE */
CREATE TYPE TypeMesure
FROM DECIMAL(5,3)
NULL

/* type statistique (utilisé pour moyenne et écart type dans LOT */
CREATE TYPE TypeStat
FROM DECIMAL(9,7)
NULL

/* type ID modèle */
CREATE TYPE TypeIDModele
FROM VARCHAR(12)
NULL

/* type nom presse */
CREATE TYPE TypeNomPresse
FROM VARCHAR(15)
NULL

/* type état, pour l'état de la production et du contrôle (3 valeurs possibles : EN ATTENTE, EN COURS, TERMINE) */
CREATE TYPE TypeEtat
FROM VARCHAR(10)
NULL

/* type catégorie de pièce (4 valeurs possibles : PETIT, MOYEN, GRAND, REBUT) */
CREATE TYPE TypeCategorie
FROM CHAR(5)
NULL

/* type message retour d'une procédure */
CREATE TYPE TypeMessageRetour
FROM VARCHAR(255)
NULL

/* type commentaire (commentaire pour le contrôle visuel notamment) */
CREATE TYPE TypeCommentaire
FROM VARCHAR(255)
NULL

GO

/***************************************************
****************Création des Tables*****************
***************************************************/

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
	idCategorie TypeCategorie CHECK (idCategorie IN ('Petit', 'Moyen', 'Grand', 'Rebut')) PRIMARY KEY,
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

/* Création des Rôles */

CREATE ROLE ResponsableApplication;
CREATE ROLE ResponsableAtelier;
CREATE ROLE ResponsablePresse;
CREATE ROLE Controleur;
CREATE ROLE Magasinier;
CREATE ROLE ResponsableQualite;

GO

/* Information generale sur la base de donnée */
CREATE TABLE InfoBase
(
	id bit PRIMARY KEY CHECK (id = 1) DEFAULT 1,
	numVersion int NOT NULL,
	nombreDePiecesParCaisse int NOT NULL
);

INSErT InTO InfoBase(numVersion, nombreDePiecesParCaisse) VALUES (1, 40);
GO

/* Création des vues */

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

--===============================================================================
-- View qui permet de vérifier si il y a des machines libres
--===============================================================================

CREATE view MachinesLibres
AS

SELECT idPresse, libellePresse
FROM Machine
Where enService = 1 
AND NOT EXISTS (SELECT etatProduction FROM Lot Where etatProduction = 'EnCours' AND lot.idPresse = Machine.idPresse)

GO
--===============================================================================
-- View qui permet de vérifier les catégories de piéces produites par une machine
--===============================================================================

CREATE view StatMachines
AS
SELECT m.idPresse, m.libellePresse, p.idCategorie, COUNT(p.idPiece) as production
FROM Machine m
JOIN Lot l ON l.idPresse = m.idPresse
JOIN Piece p on p.idLot = l.idLot
GROUP BY m.idPresse, m.libellePresse, p.idCategorie

/***************************************
 * Procédures Stockées et fonctions    *
 ***************************************/
GO 
 CREATE FUNCTION fn_getToleranceMini
(
	@cat TypeCategorie
)
RETURNS TypeMesure
AS
BEGIN
	DECLARE @tolerance TypeMesure;
	SELECT @tolerance = toleranceMini FROM Categorie WHERE idCategorie = @cat
	RETURN @tolerance;
END
GO

CREATE FUNCTION fn_getToleranceMaxi
(
	@cat TypeCategorie
)
RETURNS TypeMesure
AS
BEGIN
	DECLARE @tolerance TypeMesure;
	SELECT @tolerance = toleranceMaxi FROM Categorie WHERE idCategorie = @cat
	RETURN @tolerance;
END
GO

/*
 * retourne la sous catégorie de la mesure:
 *		rt	[minPetit	tp	[minMoyen	pm	[maxPetit	mm	minGrand]	mg	maxMoyen]	tg	maxGrand]	rt
 */
CREATE FUNCTION fn_categorieMesure
(
	@mesure TypeMesure
)
RETURNS char(2)
AS
BEGIN
	DECLARE @cat char(2);
	IF @mesure IS NULL
		SET @cat = NULL;
	ELSE IF @mesure < dbo.fn_getToleranceMini('Petit') OR @mesure > dbo.fn_getToleranceMaxi('Grand')
		SET @cat = 'rt';
	ELSE IF @mesure < dbo.fn_getToleranceMini('Moyen')
		SET @cat = 'tp';
	ELSE IF @mesure < dbo.fn_getToleranceMaxi('Petit')
		SET @cat = 'pm';
	ELSE IF @mesure <= dbo.fn_getToleranceMini('Grand')
		SET @cat = 'mm';
	ELSE IF @mesure <= dbo.fn_getToleranceMaxi('Moyen')
		SET @cat = 'mg';
	ELSE
		SET @cat = 'tg';

	RETURN @cat;
END

GO
/*
 * sous procédure qui ajoute la sous catégorie a laquelle appartient la mesure
 * @mesure : la mesure à appliquer
 * @tp : il y a une mesure 'très petit'
 * @pm : il y a une mesure 'petit moyen'
 * @mm : il y a une mesure 'moyen moyen'
 * @mg : il y a une mesure 'moyen grand'
 * @tg : il y a une mesure 'très grand'
 * @rt : il y a une mesure 'rebut'
 */
CREATE PROCEDURE sp_appliquerMesure
(
	@mesure TypeMesure,
	@tp bit OUTPUT,
	@pm bit OUTPUT,
	@mm bit OUTPUT,
	@mg bit OUTPUT,
	@tg bit OUTPUT,
	@rt bit OUTPUT
)
AS
BEGIN
	DECLARE @cat char(2) = dbo.fn_categorieMesure(@mesure);
	IF  @cat = 'tp'
		SET @tp = 1;
	ELSE IF @cat = 'pm'
		SET @pm = 1;
	ELSE IF @cat = 'mm'
		SET @mm = 1;
	ELSE IF @cat = 'mg'
		SET @mg = 1;
	ELSE IF @cat = 'tg'
		SET @tg = 1;
	ELSE
		SET @rt = 1;
END
GO

CREATE PROCEDURE sp_categoriePiece
(
	@nominal TypeMesure,
	@ht TypeMesure,
	@hl TypeMesure,
	@bt TypeMesure,
	@bl TypeMesure,
	@defaut bit,
	@categorie TypeCategorie OUTPUT
)
AS
BEGIN
	IF @defaut = 1
		SET @categorie = 'Rebut';
	ELSE
	BEGIN
		DECLARE @tp bit = 0;
		DECLARE @pm bit = 0;
		DECLARE @mm bit = 0;
		DECLARE @mg bit = 0;
		DECLARE @tg bit = 0;
		DECLARE @rt bit = 0;
		
		SET @ht = @ht - @nominal;
		SET @hl = @hl - @nominal;
		SET @bt = @bt - @nominal;
		SET @bl = @bl - @nominal;
		
		--pour chaque mesure, on teste la catégorie
		EXECUTE sp_appliquerMesure @ht, @tp OUTPUT, @pm OUTPUT, @mm OUTPUT, @mg OUTPUT, @tg OUTPUT, @rt OUTPUT;
		EXECUTE sp_appliquerMesure @hl, @tp OUTPUT, @pm OUTPUT, @mm OUTPUT, @mg OUTPUT, @tg OUTPUT, @rt OUTPUT;
		EXECUTE sp_appliquerMesure @bt, @tp OUTPUT, @pm OUTPUT, @mm OUTPUT, @mg OUTPUT, @tg OUTPUT, @rt OUTPUT;
		EXECUTE sp_appliquerMesure @bl, @tp OUTPUT, @pm OUTPUT, @mm OUTPUT, @mg OUTPUT, @tg OUTPUT, @rt OUTPUT;
		
		IF @rt = 1
			SET @categorie = 'Rebut';
		ELSE IF @tp = 1
			IF @mm = 1 OR @mg = 1 OR @tg = 1
				SET @categorie = 'Rebut';
			ELSE
				SET @categorie = 'Petit';
		ELSE IF @pm = 1
			IF @tg = 1
				SET @categorie = 'Rebut';
			ELSE IF @mm = 1 OR @mg = 1
				SET @categorie = 'Moyen';
			ELSE
				SET @categorie = 'Petit';
		ELSE IF @mm = 1
			IF @tg = 1
				SET @categorie = 'Rebut';
			ELSE
				SET @categorie = 'Moyen';
		ELSE IF @mg = 1 OR @tg = 1
			SET @categorie = 'Grand';
		ELSE
			SET @categorie = 'Rebut'; -- cas normalement impossible (on ne peut avoir aucune mesure)
	END
END
GO

/****************************************************************************************
 * Saisie d'une pièce d'un lot								*
 * ---------------------------								*
 * @idLot = id du lot de la pièce							*
 * @hl, ht, bl, bt = cotes mesurées, non NULL						*
 * @defautVisuel = 0 si pas de défaut constaté; 1 si un défaut est constaté		*
 *					si NULL, alors considéré comme valant 0		*
 * @commentaire = contient des précisions sur l'état de la pièce (facultatif)	    	*
 * SORTIE @messageRetour contient un information sur l'exécution de la procédure	*
 * RETOUR = 0 si la saisie s'est correctement déroulée					*
 ****************************************************************************************/
CREATE PROCEDURE sp_saisirPiece
(
	@idLot int,
	@ht TypeMesure,
	@hl TypeMesure,
	@bt TypeMesure,
	@bl TypeMesure,
	@defautVisuel bit,
	@commentaire TypeCommentaire,
	@idPiece int OUTPUT,
	@messageRetour TypeMessageRetour OUTPUT
)
AS
BEGIN
	DECLARE @codeRetour int = -1;
	SET @messageRetour = 'Non implementé';
	IF @idLot IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 1 (@idLot) ne peut être NULL';
		END
	ELSE IF @ht IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 2 (@ht) ne peut être NULL';
		END
	ELSE IF @hl IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 3 (@hl) ne peut être NULL';
		END
	ELSE IF @bt IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 4 (@bt) ne peut être NULL';
		END
	ELSE IF @bl IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 3 (@bl) ne peut être NULL';
		END
	ELSE
		BEGIN
			IF @defautVisuel IS NULL
				SET @defautVisuel = 0;
			BEGIN TRY
				IF NOT EXISTS
					(
						SELECT idLot
						FROM Lot
						WHERE idLot = @idLot
					)
					BEGIN
						SET @codeRetour = 2;
						SET @messageRetour = 'Le lot ''' + convert(varchar(10), @idLot) + ''' n''existe pas.';
					END
				ELSE
					BEGIN
						DECLARE @etat TypeEtat;
						SELECT @etat = etatControle FROM Lot WHERE @idLot = idLot;
						IF @etat = 'Attente'
							BEGIN
								SET @codeRetour = 2;
								SET @messageRetour = 'Le contrôle du Lot ''' + convert(varchar(10), @idLot) + ''' doit être annoncé au préalable';
							END
						ELSE IF @etat = 'Termine'
							BEGIN
								SET @codeRetour = 2;
								SET @messageRetour = 'Le contrôle du Lot ''' + convert(varchar(10), @idLot) + ''' a déjà été clôturé.';
							END
						ELSE
							BEGIN
								DECLARE @cat TypeCategorie;
								DECLARE @tab TABLE (idPiece int);
								DECLARE @nominal TypeMesure;
								SELECT @nominal = m.diametre
								FROM Modele m JOIN Lot l On m.idModele = l.idModele
								WHERE l.idLot = @idLot;
								EXECUTE dbo.sp_categoriePiece @nominal, @ht, @hl, @bt, @bl, @defautVisuel, @cat OUTPUT;
								INSERT INTO Piece (idLot, dimensionHT, dimensionHL, dimensionBT, dimensionBL, defautVisuel, commentaire, idCategorie)
									OUTPUT INSERTED.idPiece INTO @tab VALUES (@idLot, @ht, @hl, @bt, @bl, @defautVisuel, @commentaire, @cat);
								SELECT @idPiece = idPiece FROM @tab;
								SET @codeRetour = 0;
								SET @messageRetour = 'Pièce saisie avec succès.';
							END
					END
			END TRY
			BEGIN CATCH
				SET @codeRetour = 3;
				SET @messageRetour = 'Erreur base de donnée : ' + ERROR_MESSAGE();
			END CATCH
		END
	RETURN @codeRetour;
END
GO

/******************************************************************************
 * @idLot : le lot a annuler                                                  *
 * @messageRetour : message d'information sur le déroulement de la procédure  *
 * Retoure : 0 si la procédure s'est bien déroulée, le lot a été annulé       *
 ******************************************************************************/
CREATE PROCEDURE sp_annulerLot
(
	@idLot int,
	@messageRetour TypeMessageRetour OUTPUT
)
AS
BEGIN
	DECLARE @codeRetour int = -1;
	SET @messageRetour = 'non implementé';

	IF @idLot IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 1 (@idLot) ne peut être NULL';
		END
	ELSE
		BEGIN TRY
			IF NOT EXISTS
				(
					SELECT idLot
					FROM Lot
					WHERE idLot = @idLot
				)
				BEGIN
					SET @codeRetour = 2;
					SET @messageRetour = 'Le lot ''' + convert(varchar(10), @idLot) + ''' n''existe pas';
				END
			ELSE
				BEGIN
					DELETE FROM Lot WHERE idLot = @idLot AND etatProduction = 'Attente';
					IF @@ROWCOUNT = 0
						BEGIN
							SET @codeRetour = 2;
							SET @messageRetour = 'impossible d''annuler le lot ''' + convert(varchar(10), @idLot) + ''' car sa production a déjà démarré'
						END
					ELSE
						BEGIN
							SET @codeRetour = 0;
							SET @messageRetour = 'Le lot '''+ convert(varchar(10), @idLot) +'''a bient été annulé';
						END
				END
		END TRY
		BEGIN CATCH
			SET @codeRetour = 3;
			SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE();
		END CATCH
	RETURN @codeRetour;
END
GO


/**********************************************************************************************************************************
 * affecte un employé à un role. crée l'utilisateur dans la base de donnée et l'affecte au rôle donné.                            *
 * @employe est le nom systeme de login à affecter                                                                                *
 * @role est le nom systeme du role a affecter. Un employé ne peut etre affecté qu'à un seul role.                                *
 * Si @role vaut NULL, l'employe n'est plus affecté à aucun role, et l'utilisateur de base de donnée correspondant est supprimé.  *
 * retourne 0 lorsque l'opération est un succès                                                                                   *
 **********************************************************************************************************************************/
CREATE PROCEDURE sp_assignerEmploye
(
	@employe sysname,
	@role sysname,
	@messageRetour TypeMessageRetour OUTPUT
)
AS
BEGIN
	DECLARE @codeRetour int;
	SET @codeRetour = -1;
	SET @messageRetour = 'non implémenté';
	IF @employe IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 1 (@employe) ne peut être null';
		END
	ELSE IF @employe = 'dbo'
		BEGIN
			SET @codeRetour = 2;
			SET @messageRetour = 'Le paramètre 1 (@employe) ne peut être ''dbo''. Le propriétaire de la base est un utilisateur réservé.';
		END
	ELSE IF @role IS NOT NULL AND @role NOT IN ('ResponsableApplication', 'ResponsableAtelier', 'ResponsablePresse', 'Controleur', 'Magasinier', 'ResponsableQualite')
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 2 (@role = ''' + @role + ''') est incorrect. (doit valoir ''ResponsableApplication'', ''ResponsableAtelier'', ''ResponsablePresse'', ''Controleur'', ''Magasinier'', ''ResponsableQualite'' ou NULL)';
		END
	ELSE 
	BEGIN
		BEGIN TRANSACTION
		DECLARE @sql nvarchar(512);
		BEGIN TRY
			-- suppresion préalable de l'utilisateur s'il existe. ce qui revient à le retirer de tout role auquel il était affecté
			SET @sql = 'DROP USER ' + @employe;
			EXECUTE sp_executesql @sql;
		END TRY
		BEGIN CATCH
			-- si une erreur se produit, c'est que l'utilisateur n'existait pas.
		END CATCH
		-- création de l'utilisateur
		BEGIN TRY
		IF @role IS NOT NULL
			BEGIN
				SET @sql = 'CREATE USER ' + @employe;
				EXECUTE sp_executesql @sql;
				SET @sql = 'ALTER ROLE ' + @role + ' ADD MEMBER ' + @employe;
				EXECUTE sp_executesql @sql;
				COMMIT TRANSACTION
				SET @codeRetour = 0;
				SET @messageRetour = 'L''utilisateur ''' + @employe + ''' est affecté au role ''' + @role + '''.';
			END
		ELSE
			BEGIN
				SET @codeRetour = 0;
				SET @messageRetour = 'L''utilisateur ''' + @employe + ''' a bien été supprimé.';
				COMMIT TRANSACTION;
			END
		END TRY
		BEGIN CATCH
			-- si erreur creation, abandon!
			SET @codeRetour = 3;
			SET @messageRetour = 'Une erreur de base de données est survenue lors de la création de l''utilisateur ''' + @employe + ''' : ' + ERROR_MESSAGE();
			ROLLBACK TRANSACTION;
		END CATCH
	END
	RETURN @codeRetour;
END
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 27/02/2018
-- Description: Lancer le control d'un lot

-- @IdLot : Id du Lot dont on veut démarrer le control
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => Lancement impossible
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/
CREATE PROC sp_demarrerControle (@idLot int, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idLot IS NULL OR @idLot = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du lot manquant';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @IdLot
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''existe pas';
		END
		-- Vérifier que la production a été lancée
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @IdLot
							AND Lot.etatProduction = 'EnCours'
							)
		BEGIN 
			SET @retour = 2;
			SET @msg = 'La production de ce lot n''est pas lancée ';
		END
		-- Verifier que le control n'a pas été lancé
		ELSE IF EXISTS (
						SELECT Lot.idLot 
						FROM Lot
						WHERE Lot.idLot = @IdLot 
						AND Lot.etatControle = 'Attente'
						)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Le control a déjà été lancée'
		END
		ELSE 
		BEGIN
				UPDATE Lot 
				SET Lot.etatControle = 'EnCours'
				WHERE Lot.idLot = @IdLot
				SET @retour = 0;
				SET @msg = 'Etat du control mise à jour de Attente à En Cour'
		END
	END TRY

		BEGIN CATCH
			SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 27/02/2018
-- Description: Lancer la production d'un Lot

-- @IdLot : Id du Lot dont on veut lancer la production
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => N'éxiste pas 
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/
CREATE PROC sp_demarrerProd (@idLot int, @idMachine int, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idLot IS NULL OR @idLot = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du lot manquant';
		END
		ELSE IF @idMachine IS NULL OR @idMachine = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Machine manquante';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @idLot
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''existe pas';
		END
		-- Vérifier que la production n'a pas déjà été lancée
		ELSE IF NOT	EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @IdLot
							AND Lot.etatProduction = 'Attente'
						)
		BEGIN 
			SET @retour = 2;
			SET @msg = 'La production de ce lot est déjà lancée';
		END
		-- Verifier que le control n'a pas été lancé
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot 
							FROM Lot
							WHERE Lot.idLot = @idLot 
							AND Lot.etatControle = 'Attente'
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Le control a déjà été lancée'
		END
		-- Verification de disponibilité de la machine
		ELSE IF NOT EXISTS (
							SELECT *
							FROM MachinesLibres
							WHERE MachinesLibres.IdPresse = @idMachine
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'La machine n''est pas disponible'
		END
		ELSE 
		BEGIN
			UPDATE Lot 
			SET Lot.etatProduction = 'EnCours',
				 Lot.idPresse = @idMachine
				 Lot.dateProduction = GETDATE()
			WHERE Lot.idLot = @idLot

			SET @retour = 0;
			SET @msg = 'Etat de la production mise à jour de "Attente" à "En Cours"'
		END
	END TRY

		BEGIN CATCH
			SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

--===============================================================================
-- Procedure entrée caisse dans le stock
--===============================================================================

CREATE PROCEDURE sp_entreeStock(@idModele TypeIDModele, @idCategorie TypeCategorie, @qtEntree int, @msg varchar(250) OUTPUT)
AS
BEGIN
	declare @return int;

	begin try
	-- verification des données entrées
		if @idModele = NULL OR @idModele = ''
		begin
			set @return = 1;
			set @msg = 'Id du modele null ou manquant !'
		end
		
		else if @idCategorie = NULL OR @idCategorie = ''
		begin
			set @return = 1;
			set @msg = 'Id du categorie null ou manquant !'
		end
		
		else if @qtEntree = NULL or @qtEntree <= 0
		begin
			set @return = 1;
			set @msg = 'Quantitée entrée invalide !'
		end

		else if not exists(
							SELECT idModele
							FROM Modele
							where idModele = @idModele
							)
		begin
			set @return = 2;
			set @msg = 'Modele inexistant !'
		end

		else if @idCategorie <> 'Petit' AND @idCategorie <> 'Moyen' AND @idCategorie <> 'Grand' 
		begin
			set @return = 2;
			set @msg = 'Categorie invalide !'
		end

		else
		begin
			UPDATE Stock
			set qtStock = qtStock + @qtEntree
			where Stock.idModele = @idModele AND Stock.idCategorie = @idCategorie
				set @return = 0;
				set @msg = 'Quantitée ajoutée au stock'
		end
	end try

	begin catch
			set @return = 3;
			set @msg = 'Exception' + ERROR_MESSAGE();
	end catch

	RETURN @return;
END
GO

/********************************************************************************************
 * Crée une demande de production de lot, qui sera en attente de production et de controle. *
 * @modele est l'ID du modèle dont créer le lot. Doit faire référence à un modèle existant, *
 *      ne peut être NULL.                                                                  *
 * @quantité est la quantité désirée. ce paramètre ne peut être NULL et doit être un entier *
 *      strictement positif.                                                                *
 ********************************************************************************************/
CREATE PROCEDURE sp_lancerLot
(
	@modele TypeIDModele,
	@quantite int,
	@messageRetour TypeMessageRetour OUTPUT
)
AS
BEGIN
	DECLARE @codeRetour int;
	SET @codeRetour = -1;
	SET @messageRetour = 'non implémenté';

	IF @modele IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 1 (@modele) ne peut être NULL';
		END
	ELSE IF @quantite IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 2 (@quantite) ne peut être NULL';
		END
	ELSE IF @quantite <= 0
		BEGIN
			SET @codeRetour = 2;
			SET @messageRetour = 'La quantité spécifiée est incorrecte';
		END
	ELSE
		BEGIN TRY
			IF NOT EXISTS
				(
					SELECT idModele
					FROM Modele
					WHERE idModele = @modele
				)
				BEGIN
					SET @codeRetour = 2;
					SET @messageRetour = 'Le modèle ''' + @modele + ''' n''existe pas.';
				END
			ELSE
				BEGIN
					INSERT INTO Lot (dateDemande, etatProduction, etatControle, idModele, nbrPieceDemande) OUTPUT INSERTED.idLot VALUES (GETDATE(), 'Attente', 'Attente', @modele, @quantite);
					SET @codeRetour = 0;
					SET @messageRetour = 'Lot ajouté';
				END
		END TRY
		BEGIN CATCH
			SET @codeRetour = 3;
			SET @messageRetour = 'Erreur base de donnée : ' + ERROR_MESSAGE();
		END CATCH
	RETURN @codeRetour;
END
GO

--===============================================================================
-- Procedure qui permet de modifier les seuils de tolérance des catégories
-- @minPetit est le seuil de tolérance mini de la catégorie petit
-- @maxPetit est le seuil de tolérance maxi de la catégorie petit
-- @minMoyen est le seuil de tolérance mini de la catégorie moyen
-- @maxMoyen est le seuil de tolérance maxi de la catégorie moyen
-- @minGrand est le seuil de tolérance mini de la catégorie grand
-- @maxGrand est le seuil de tolérance maxi de la catégorie grand
-- @messageRetour est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle
--			2. Conflit / incohérence entre les bornes
--			3. Seuils de tolérance verrouillés
--			4. Erreur base de donnée
--===============================================================================
CREATE PROC sp_modifierBornesCategories @minPetit TypeMesure, @maxPetit TypeMesure, @minMoyen TypeMesure, @maxMoyen TypeMesure, @minGrand TypeMesure, @maxGrand TypeMesure, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour TINYINT ;
BEGIN TRY
	BEGIN
		IF EXISTS (SELECT * FROM Lot)
		BEGIN
			SET @codeRetour = 3 ;
			SET @messageRetour = 'Des lots on déjà été créés, les seuils de tolérance des catégories sont maintenant verrouillées.';
		END
		ELSE IF @minPetit IS NULL
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'La borne minimum de la catégorie petit est nulle.';
		END
		ELSE IF @maxPetit IS NULL
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'La borne maximum de la catégorie petit est nulle.';
		END
		ELSE IF @minMoyen IS NULL
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'La borne minimum de la catégorie moyen est nulle.';
		END
		ELSE IF @maxMoyen IS NULL
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'La borne maximum de la catégorie moyen est nulle.';
		END
		ELSE IF @minGrand IS NULL
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'La borne minimum de la catégorie grand est nulle.';
		END
		ELSE IF @maxGrand IS NULL
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'La borne maximum de la catégorie grand est nulle.';
		END
		ELSE IF @minPetit >= @maxPetit
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne maximum de la catégorie petit est inférieure à sa borne minimum.';
		END
		ELSE IF @minMoyen >= @maxMoyen
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne maximum de la catégorie moyen est inférieure à sa borne minimum.';
		END
		ELSE IF @minGrand >= @maxGrand
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne maximum de la catégorie grand est inférieure à sa borne minimum.';
		END
		ELSE IF @minPetit >= @minMoyen
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne minimum de la catégorie petit est supérieure à la borne minimum de la catégorie moyen.';
		END
		ELSE IF @maxMoyen >= @maxGrand
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne maximum de la catégorie moyen est supérieure à la borne maximum de la catégorie grand.';
		END
		ELSE IF @minMoyen > @maxPetit
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne minimum de la catégorie moyen est strictement supérieure à la borne maximum de la catégorie petit.';
		END
		ELSE IF @maxMoyen < @minGrand
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne maximum de la catégorie moyen est strictement inférieure à la borne minimum de la catégorie grand.';
		END
		ELSE IF @maxPetit > @minGrand
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La borne maximum de la catégorie petit est strictement supérieure à la borne minimum de la catégorie grand.';
		END
		ELSE
		BEGIN
			-- Création des catégories petit, moyen, grand, rebut si elles n'existent pas
			IF NOT EXISTS (SELECT* FROM Categorie WHERE idCategorie = 'Petit')
			BEGIN
				INSERT INTO Categorie (idCategorie) VALUES ('Petit') ;
			END
			IF NOT EXISTS (SELECT* FROM Categorie WHERE idCategorie = 'Moyen')
			BEGIN
				INSERT INTO Categorie (idCategorie) VALUES ('Moyen') ;
			END
			IF NOT EXISTS (SELECT* FROM Categorie WHERE idCategorie = 'Grand')
			BEGIN
				INSERT INTO Categorie (idCategorie) VALUES ('Grand') ;
			END
			IF NOT EXISTS (SELECT* FROM Categorie WHERE idCategorie = 'Rebut')
			BEGIN
				INSERT INTO Categorie (idCategorie) VALUES ('Rebut') ;
			END

			-- Affectation des valeurs des seuils de tolérance
			UPDATE Categorie
			SET toleranceMini = @minPetit, toleranceMaxi = @maxPetit
			WHERE idCategorie = 'Petit';
			UPDATE Categorie
			SET toleranceMini = @minMoyen, toleranceMaxi = @maxMoyen
			WHERE idCategorie = 'Moyen';
			UPDATE Categorie
			SET toleranceMini = @minGrand, toleranceMaxi = @maxGrand
			WHERE idCategorie = 'Grand';

			SET @codeRetour = 0 ;
			SET @messageRetour = 'Les valeurs des seuils de tolérances ont été mis à jour.';
		END
	END
END TRY
BEGIN CATCH
	SET @codeRetour = 4 ;
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
END CATCH
RETURN @codeRetour ;
GO
--initialisation des tolérances pour les catégories
DECLARE @msg TypeMessageRetour;
EXECUTE sp_modifierBornesCategories -0.1, -0.01, -0.05, 0.05, 0.01, 0.1, @msg OUTPUT;

GO
/****************************************************************************************************
 * retourne un résultat contenant le nom des rôles affectés à l'utilisateur actuellement connecté.  *
 * les rôles sont ceux définis dans la base de données (cf 'sql/sql_roles.sql')                     *
 * le code de retour est toujours 0                                                                 *
 ****************************************************************************************************/
CREATE PROCEDURE sp_retournerRole
AS
BEGIN
	DECLARE @codeRetour int;
	SET @codeRetour = 0;
	SELECT r.name
	FROM sys.database_role_members as rm
	JOIN sys.database_principals as r ON rm.role_principal_id = r.principal_id
	WHERE rm.member_principal_id = DATABASE_PRINCIPAL_ID();
	RETURN @codeRetour
END

GO
--===============================================================================
-- Procedure sortie caisse du stock
--===============================================================================

CREATE PROCEDURE sp_sortieStock(@idModele TypeIDModele, @idCategorie TypeCategorie, @qtSortie int, @msg varchar(250) OUTPUT)
AS
	declare @return int;

	begin try
	-- verification des données entrées
		if @idModele = NULL OR @idModele = ''
		begin
			set @return = 1;
			set @msg = 'Id du modele null ou manquant !'
		end
		
		else if @idCategorie = NULL OR @idCategorie = ''
		begin
			set @return = 1;
			set @msg = 'Id du categorie null ou manquant !'
		end
		
		else if @qtSortie = NULL or @qtSortie <= 0
		begin
			set @return = 1;
			set @msg = 'Quantitée sortie invalide !'
		end
		else if not exists(
							SELECT idModele, idCategorie
							FROM Stock
							where idModele = @idModele	AND idCategorie = @idCategorie
							)
			begin
				set @return = 2;
				set @msg = 'Pas de stock pour ce modele et cette categorie !'
			end

		else if @qtSortie > (SELECT qtStock FROM Stock where Stock.idModele = @idModele AND Stock.idCategorie = @idCategorie)
			begin
				set @return = 2;
				set @msg = 'Stock insuffisant !'
			end
		else if @idCategorie <> 'Petit' AND @idCategorie <> 'Moyen' AND @idCategorie <> 'Grand' 
		begin
			set @return = 2;
			set @msg = 'Categorie invalide !'
		end

		else
		begin
			UPDATE Stock
			set qtStock = qtStock - @qtSortie
			where Stock.idModele = @idModele AND Stock.idCategorie = @idCategorie
			set @return = 0;
			set @msg = 'Quantitée sortie du stock'
		end
	end try

	begin catch
			set @return = 3;
			set @msg = 'Exception' + ERROR_MESSAGE();
	end catch

	RETURN @return;
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 01/03/2018
-- Description: Récupérer les stats détaillées d'un lot
-- @IdLot : Id du Lot dont on veut récupérer les stats 
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => Lancement impossible
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/
CREATE PROC sp_statLotReduit (@idLot int, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idLot IS NULL OR @idLot = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du lot manquant';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @idLot
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''existe pas';
		END
		-- Vérifier que le lot a fini d'être verifié
		ELSE IF EXISTS (
						SELECT Lot.idLot
						FROM Lot
						WHERE Lot.idLot = @idLot
						AND Lot.etatControle = 'Attente'
						)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''a pas fini d''être vérifié';
		END
		ELSE
		BEGIN
			SELECT idLot, moyenneHL, moyenneHT, moyenneBL, moyenneBL, moyenneBT, miniHl, miniHT, miniBL, miniBT, maxiHL, maxiHT, maxiBL, maxiBT, ecartTypeHL, ecartTypeHT, ecartTypeBL, ecartTypeBT, nbrPieceDemande
			FROM Lot
			SET @retour = 0;
			SET @msg = ''
		END
	END TRY
	BEGIN CATCH
		SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 01/03/2018
-- Description: Récupérer les stats detaillées d'un lot
-- @IdLot : Id du Lot dont on veut récupérer les stats 
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => Lancement impossible
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/
CREATE PROC sp_statLotDetail (@idLot int, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idLot IS NULL OR @idLot = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du lot manquant';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @idLot
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''existe pas';
		END
		-- Vérifier que le lot a fini d'être verifié
		ELSE IF EXISTS (
						SELECT Lot.idLot
						FROM Lot
						WHERE Lot.idLot = @idLot
						AND Lot.etatControle = 'Attente'
						)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''a pas fini d''être vérifié';
		END
		ELSE
		BEGIN
			SELECT *
			FROM Piece
			JOIN Lot ON Piece.idLot = Lot.idLot
			WHERE Lot.idLot = @idLot
			ORDER BY Piece.idCategorie
		END
	END TRY
	BEGIN CATCH
		SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 27/02/2018
-- Description: Terminer le control d'un Lot

-- @IdLot : Id du Lot dont on veut terminer la production
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => N'éxiste pas 
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/

CREATE PROC sp_terminerControle (@idLot int, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idLot IS NULL OR @idLot = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du lot manquant';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @IdLot
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''existe pas';
		END
		ELSE IF NOT EXISTS (
						SELECT lot.idLot
						FROM Lot 
						WHERE Lot.idLot = @IdLot
						AND Lot.etatControle = 'EnCour'
						)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Le controle de ce lot n''a pas commencé ou est déjà terminé'
		END 
		-- Vérifier que la production est terminée
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @IdLot
							AND Lot.etatProduction = 'Termine'
							)
		BEGIN 
			SET @retour = 2;
			SET @msg = 'La production n''est par terminée';
		END
		ELSE 
		BEGIN
			UPDATE Lot 
			SET Lot.etatControle = 'Termine'
			WHERE Lot.idLot = @IdLot
			SET @retour = 0;
			SET @msg = 'Etat du controle mis à jour de "En Cour" à "Terminé" '
		END
	END TRY

		BEGIN CATCH
			SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 27/02/2018
-- Description: Terminer la production d'un Lot

-- @IdLot : Id du Lot dont on veut terminer la production
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => N'éxiste pas 
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/

CREATE PROC sp_terminerProd (@idLot int, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idLot IS NULL OR @idLot = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du lot manquant';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @idLot
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''existe pas';
		END
		-- Vérifier que la production n'a pas déjà été lancée ou est déjà terminé
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @idLot
							AND Lot.etatProduction = 'EnCours'
							)
		BEGIN 
			SET @retour = 2;
			SET @msg = 'La production de ce lot n''a pas été lancée ou a déjà été terminée';
		END
		ELSE 
		BEGIN
			UPDATE Lot 
			SET Lot.etatProduction = 'Termine'
			WHERE Lot.idLot = @idLot
			SET @retour = 0;
			SET @msg = 'Etat de la production mise à jour de En Cour à Terminé'
		END
	END TRY

		BEGIN CATCH
			SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

--===============================================================================
-- Procedure qui permet de créer une nouvelle machine dans la base
-- @libellePresse est le nom de la presse de la machine à créer
-- @idCree en retour est l'id de la machine créée, qui vaut -1 si erreur.
-- @message est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant machine existant
--			3. Exception
--===============================================================================
CREATE PROC sp_creerMachine @libellePresse TypeNomPresse, @iDcree INT output, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		SET @iDcree = -1 ;
		IF @libellePresse IS NULL OR @libellePresse = ''
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nom de la presse nul ou vide.';
		END
		ELSE IF EXISTS (SELECT libellePresse FROM Machine WHERE libellePresse = @libellePresse)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'Nom de la presse déjà existant.';
		END
		ELSE
		BEGIN
		-- Par défaut lors de la création de la machine, le BIT En service vaut 1
			INSERT INTO Machine (libellePresse, enService) VALUES (@libellePresse, 1);
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Machine ajouté dans la table avec succès.';
			SET @iDcree = (SELECT idPresse FROM MACHINE WHERE libellePresse = @libellePresse) ;
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 3 ;
END CATCH

RETURN @codeRetour;
GO


--===============================================================================
-- Procedure qui permet de supprimer une machine dans la base
-- @idPresse est le nom de la machine à supprimer
-- @messageRetour est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant machine non reconnu
--			3. Machine encore liée à une autre table
--			4. Exception
--===============================================================================
CREATE PROC sp_supprimerMachine @idPresse INT, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		IF @idPresse IS NULL OR @idPresse <= 0
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'ID presse nul ou négatif.';
		END
		ELSE IF NOT EXISTS (SELECT idPresse FROM Machine WHERE idPresse = @idPresse)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'Cette presse n''existe pas dans la table.';
		END
		ELSE IF EXISTS (SELECT idPresse FROM Lot WHERE idPresse = @idPresse)
		BEGIN
			SET @codeRetour = 3 ;
			SET @messageRetour = 'Cette presse est encore liée à des lots existants.';
		END
		ELSE
		BEGIN
			DELETE FROM Machine
			WHERE idPresse = @idPresse ;
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Machine supprimée de la table avec succès.';
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 4 ;
END CATCH

RETURN @codeRetour;
GO

--===============================================================================
-- Procedure qui permet de renommer une machine dans la base
-- @idPresseARenommer est le nom de la machine à renommer
-- @nouveauNom est le nouveau nom à affecter à la machine
-- @messageRetour est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant machine non reconnu
--			3. Nouveau nom déjà existant
--			4. Exception
--===============================================================================
CREATE PROC sp_renommerMachine @idPresseARenommer INT, @nouveauNom TypeIDModele, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		IF @idPresseARenommer IS NULL OR @idPresseARenommer <= 0
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Numéro de presse à renommer nul ou négatif.';
		END
		ELSE IF @nouveauNom IS NULL OR @nouveauNom = ''
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nouveau nom de presse nul ou vide.';
		END
		ELSE IF NOT EXISTS (SELECT idPresse FROM Machine WHERE idPresse = @idPresseARenommer)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La presse à renommer n''existe pas dans la table.';
		END
		ELSE IF EXISTS (SELECT idPresse FROM Machine WHERE libellePresse = @nouveauNom)
		BEGIN
			SET @codeRetour = 3 ;
			SET @messageRetour = 'Le nouveau nom à affecter à la presse existe déjà dans la table.';
		END
		ELSE
		BEGIN
			UPDATE Machine
			SET libellePresse = @nouveauNom
			WHERE idPresse = @idPresseARenommer ;
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Presse renommée avec succès.';
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 4 ;
END CATCH

RETURN @codeRetour;
GO


--===============================================================================
-- Procedure qui permet changer le statut d'une machine (obsolète ou non)
-- @idPresse est le nom de la machine à updater
-- @statut est le statut à affecter à la machine (0 en service, 1 hors service)
-- @messageRetour est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant machine non reconnu
--			3. Machine en cours de production d'un lot
--			4. Exception
--===============================================================================
CREATE PROC sp_changerStatutMachine @idPresse INT, @enService TINYINT, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		IF @idPresse IS NULL OR @idPresse <= 0
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nom de la presse à mettre à jour nul ou vide.';
		END
		ELSE IF @enService IS NULL OR (@enService <> 0 AND @enService <> 1)
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Le statut à affecter est nul ou du mauvais format.';
		END
		ELSE IF NOT EXISTS (SELECT idPresse FROM Machine WHERE idPresse = @idPresse)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'La presse à mettre à jour n''existe pas dans la table.';
		END
		ELSE IF EXISTS (SELECT idLot FROM Lot WHERE etatProduction = 'EnCours' AND idPresse = @idPresse)
		BEGIN
			SET @codeRetour = 3 ;
			SET @messageRetour = 'Il y a des lots en cours de production sur cette presse.';
		END
		ELSE
		BEGIN
			UPDATE Machine
			SET enService = @enService
			WHERE idPresse = @idPresse ;
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Statut mis à jour avec succès.';
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 4 ;
END CATCH

RETURN @codeRetour;
GO

--===============================================================================
-- Procedure qui permet de créer un nouveau modèle dans la base
-- @idModele est le nom du modèle à créer
-- @diametre est le diamètre nominal du modèle
-- @message est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant modèle existant
--			3. Exception
--===============================================================================
CREATE PROC sp_creerModele @idModele TypeIDModele, @diametre TypeMesure,  @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		IF @idModele IS NULL OR @idModele = ''
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nom du modèle nul ou vide.';
		END
		ELSE IF @diametre IS NULL OR @diametre <= 0
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Le diamètre renseigné est nul ou négatif.';
		END
		ELSE IF EXISTS (SELECT idModele FROM Modele WHERE idModele = @idModele)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'Nom du modèle déjà existant.';
		END
		ELSE
		BEGIN
		-- Par défaut lors de la création du modèle, le BIT Obsolete vaut 0
			INSERT INTO Modele VALUES (@idModele, @diametre, 0);
			INSERT INTO Stock VALUES(@idModele, 'Petit', 0, 4);
			INSERT INTO Stock VALUES(@idModele, 'Moyen', 0, 4);
			INSERT INTO Stock VALUES(@idModele, 'Grand', 0, 4);
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Modèle ajouté dans la table avec succès.';
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 3 ;
END CATCH

RETURN @codeRetour;
GO


--===============================================================================
-- Procedure qui permet de supprimer un modèle dans la base
-- @idModele est le nom du modèle à supprimer
-- @messageRetour est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant modèle non reconnu
--			3. Modèle encore lié à une autre table
--			4. Exception
--===============================================================================
CREATE PROC sp_supprimerModele @idModele TypeIDModele, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		IF @idModele IS NULL OR @idModele = ''
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nom du modèle nul ou vide.';
		END
		ELSE IF NOT EXISTS (SELECT idModele FROM Modele WHERE idModele = @idModele)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'Ce modèle n''existe pas dans la table.';
		END
		ELSE IF EXISTS (SELECT idModele FROM Lot WHERE idModele = @idModele)
		BEGIN
			SET @codeRetour = 3 ;
			SET @messageRetour = 'Ce modèle est encore lié à des lots existants.';
		END
		ELSE IF EXISTS (SELECT idModele FROM Stock WHERE idModele = @idModele)
		BEGIN
			SET @codeRetour = 3 ;
			SET @messageRetour = 'Ce modèle est encore lié à des pièces en stock.';
		END
		ELSE
		BEGIN
			DELETE Modele
			FROM Modele
			WHERE idModele = @idModele ;
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Modèle supprimé de la table avec succès.';
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 4 ;
END CATCH

RETURN @codeRetour;
GO

--===============================================================================
-- Procedure qui permet de renommer un modèle dans la base
-- @idModeleARenommer est le nom du modèle à renommer
-- @nouveauNom est le nouveau nom à affecter au modèle
-- @messageRetour est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant modèle non reconnu
--			3. Nouveau nom déjà existant
--			4. Exception
--===============================================================================
CREATE PROC sp_renommerModele @idModeleARenommer TypeIDModele, @nouveauNom TypeIDModele, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		IF @idModeleARenommer IS NULL OR @idModeleARenommer = ''
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nom du modèle à renommer nul ou vide.';
		END
		ELSE IF @nouveauNom IS NULL OR @nouveauNom = ''
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nouveau nom de modèle nul ou vide.';
		END
		ELSE IF NOT EXISTS (SELECT idModele FROM Modele WHERE idModele = @idModeleARenommer)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'Le modèle à renommer n''existe pas dans la table.';
		END
		ELSE IF EXISTS (SELECT idModele FROM Modele WHERE idModele = @nouveauNom)
		BEGIN
			SET @codeRetour = 3 ;
			SET @messageRetour = 'Le nouveau nom à affecter au modèle existe déjà dans la table.';
		END
		ELSE
		BEGIN
			UPDATE Modele
			SET idModele = @nouveauNom
			WHERE idModele = @idModeleARenommer ;
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Modèle renommé avec succès.';
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 4 ;
END CATCH

RETURN @codeRetour;
GO


--===============================================================================
-- Procedure qui permet changer le statut d'un modèle (obsolète ou non)
-- @idModele est le nom du modèle à updater
-- @statut est le statut à affecter au modèle (0 en service, 1 obsolète)
-- @messageRetour est le message retour
-- Code retour :
--			0. OK
--			1. Paramètre d'entrée à valeur nulle ou incohérente
--			2. Identifiant modèle non reconnu
--			3. Exception
--===============================================================================
CREATE PROC sp_changerStatutModele @idModele TypeIDModele, @statut TINYINT, @messageRetour TypeMessageRetour output
AS
DECLARE @codeRetour Tinyint;

BEGIN TRY
	BEGIN
		IF @idModele IS NULL OR @idModele = ''
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Nom du modèle à mettre à jour nul ou vide.';
		END
		ELSE IF @statut IS NULL OR (@statut <> 0 AND @statut <> 1)
		BEGIN
			SET @codeRetour = 1 ;
			SET @messageRetour = 'Le statut à affecter est nul ou du mauvais format.';
		END
		ELSE IF NOT EXISTS (SELECT idModele FROM Modele WHERE idModele = @idModele)
		BEGIN
			SET @codeRetour = 2 ;
			SET @messageRetour = 'Le modèle n''existe pas dans la table.';
		END
		ELSE
		BEGIN
			UPDATE Modele
			SET Obsolete = @statut
			WHERE idModele = @idModele ;
			SET @codeRetour = 0 ;
			SET @messageRetour = 'Statut mis à jour avec succès.';
		END
	END
END TRY
BEGIN CATCH
	SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE() ;
	SET @codeRetour = 4 ;
END CATCH

RETURN @codeRetour;
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 27/02/2018
-- Description: Permet de modifier un lot si et seulement s'il est encore en attente de production 
-- @IdLot : Id du Lot dont on veut terminer la production
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => N'éxiste pas 
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/
CREATE PROC sp_modifierLot (@idLot int, @qtLot int, @idModele TypeIDModele, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idLot IS NULL OR @idLot = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du lot manquant';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @idLot
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce lot n''existe pas';
		END 
		-- Verification que la quantité saisie est bonne 
		ELSE IF @qtLot IS NULL OR @qtLot = '' OR @qtLot = 0
		BEGIN
			SET @retour = 1;
			SET @msg = 'Quantité invalide !';
		END
		-- Verification que les donnée ne sont pas null
		ELSE IF @idModele IS NULL OR @idModele = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du modèle manquant';
		END
		-- Verification de l'existance du lot
		ELSE IF NOT EXISTS (
							SELECT Modele.idModele
							FROM Modele
							WHERE Modele.idModele = @idModele
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce Modele n''existe pas';
		END
		-- Vérifier que la production n'a pas été lancé
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idLot = @idLot
							AND Lot.etatProduction = 'Attente'
							)
		BEGIN 
			SET @retour = 2;
			SET @msg = 'La production de ce lot a déjà été lancée, il n''est pas modifiable';
		END
		ELSE 
		BEGIN
			UPDATE Lot 
			SET Lot.idModele = @idModele,
			Lot.nbrPieceDemande = @qtLot
			WHERE Lot.idLot = @idLot
			SET @retour = 0;
			SET @msg = 'Le lot a bien été modifié'
		END
	END TRY

		BEGIN CATCH
			SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

/*
 * Procédure qui met à jour les stats réduites d'un lot dont le controle n'est pas terminé.
 * @idLot : identifiant du lot à mettre à jour. Le paramètre ne doit pas être NULL et doit identifié un lot existant
 * 	dont le contrôle est en cours et pour lequel des pièces on été saisies.
 * @msg - sortie : message d'information sur le déroulement de la procédure
 * retour : 0 si la procédure s'est déroulée avec succès
 */
CREATE PROCEDURE sp_mettreAJourStatsLot
(
	@idLot int,
	@messageRetour TypeMessageRetour OUTPUT
)
AS
BEGIN
	DECLARE @codeRetour int = -1;
	SET @messageRetour = 'Non implémenté';
	IF @idLot IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 1 (@idLot) ne peut être NULL';
		END
	ELSE
		BEGIN TRY
			DECLARE @etatControle TypeEtat;
			SELECT @etatControle = etatControle FROM Lot WHERE idLot = @idLot;
			IF @etatControle = 'Attente'
				BEGIN
					SET @codeRetour = 2;
					SET @messageRetour = 'Stats indisponibles. Le contrôle du lot ''' + CONVERT(varchar(10), @idLot) + ''' n''a pas encore été commencé.';
				END
			ELSE IF @etatControle = 'Termine'
				BEGIN
					SET @codeRetour = 2;
					SET @messageRetour = 'Impossible de mettre à jour les stats car le contrôle du lot ''' + CONVERT(varchar(10), @idLot) + ''' est déjà terminé.';
				END
			ELSE
				BEGIN
					DECLARE @nombrePieces int;
					SELECT @nombrePieces = COUNT(idPiece) FROM Piece WHERE idLot = @idLot;
					IF @nombrePieces = 0
						BEGIN
							SET @codeRetour = 2;
							SET @messageRetour = 'Stats indisponibles car aucune pièce n''a encore été saisie pour le lot ''' + CONVERT(varchar(10), @idLot) + '.';
						END
					ELSE
						BEGIN
							DECLARE @ecartTypeBL TypeStat, @ecartTypeBT TypeStat, @ecartTypeHL TypeStat, @ecartTypeHT TypeStat,
								@maxiBL TypeStat, @maxiBT TypeStat, @maxiHL TypeStat, @maxiHT TypeStat,
								@miniBL TypeStat, @miniBT TypeStat, @miniHL TypeStat, @miniHT TypeStat,
								@moyenneBL TypeStat, @moyenneBT TypeStat, @moyenneHL TypeStat, @moyenneHT TypeStat;
							SELECT @ecartTypeBL = STDEV(p.dimensionBL), @ecartTypeBT = STDEV(p.dimensionBT), @ecartTypeHL = STDEV(p.dimensionHL), @ecartTypeHT = STDEV(p.dimensionHT),
								@maxiBL = MAX(p.dimensionBL), @maxiBT = MAX(p.dimensionBT), @maxiHL = MAX(p.dimensionHL), @maxiHT = MAX(p.dimensionHT),
								@miniBL = MIN(p.dimensionBL), @miniBT = MIN(p.dimensionBT), @miniHL = MIN(p.dimensionHL), @miniHT = MIN(p.dimensionHT),
								@moyenneBL = AVG(p.dimensionBL), @moyenneBT = AVG(p.dimensionBT), @moyenneHL = AVG(p.dimensionHL), @moyenneHT = AVG(p.dimensionHT)
							FROM Piece p
							WHERE p.idLot = @idLot;
							UPDATE Lot
							SET ecartTypeBL = @ecartTypeBL, ecartTypeBT = @ecartTypeBT, ecartTypeHL = @ecartTypeHL, ecartTypeHT = @ecartTypeHT,
								maxiBL = @maxiBL, maxiBT = @maxiBT, maxiHL = @maxiHL, maxiHT = @maxiHT,
								miniBL = @miniBL, miniBT = @miniBT, miniHL = @miniHL, miniHT = @miniHT,
								moyenneBL = @moyenneBL, moyenneBT = @moyenneBT, moyenneHL = @moyenneHL, moyenneHT = @moyenneHT
							WHERE idLot = @idLot;
							SET @codeRetour = 0;
							SET @messageRetour = 'Les statistiques réduites du lot ''' + CONVERT(varchar(10), @idLot) + ' ont été mises à jour';
						END
				END
		END TRY
		BEGIN CATCH
			SET @codeRetour = 3;
			SET @messageRetour = 'Erreur base de données : ' + ERROR_MESSAGE();
		END CATCH
	RETURN @codeRetour;
END
GO

/*-- ==========================================================
-- Author:		Grégoire BRUN
-- Create date: 02/03/2018
-- Description: Permet de donner pour une presse et un modèle les stat réduit sur une periode 
-- @idPresse : Id de la presse dont on veut récupérer les stats 
-- @idModele : Id du modèle dont on veut récupérer les stats sur la pièce
-- @dateDebut : Date à partir de laquelle on veut concidérer les pièces
-- @dateFin : Date jusqu'à laquelle on veut concidérer les pièves 
-- @retour = Retourne	0 => Ok
						1 => Le champ n'a pas été renseigné
						2 => N'éxiste pas 
						3 => Erreur base de donnée (Exception)
-- Sortie : @mes = Contient le message d'erreur ou de réussite 
-- =============================================================*/

CREATE PROC sp_statPresse (@idPresse int, @idModele TypeIDModele, @dateDebut datetime, @dateFin datetime, @msg varchar(250) OUTPUT)
AS
	DECLARE @retour int;
	BEGIN TRY
		-- Verification que les données ne sont pas null
		IF @idPresse IS NULL OR @idPresse = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id de la presse manquante';
		END
		-- Verification de l'existance de la machine
		ELSE IF NOT EXISTS (
							SELECT Machine.idPresse
							FROM Machine
							WHERE Machine.idPresse = @idPresse
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Cette presse n''existe pas';
		END 
		-- Verification que les données ne sont pas null
		ELSE IF @idModele IS NULL OR @idModele = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Id du modèle manquant';
		END
		-- Verification de l'existance de la machine
		ELSE IF NOT EXISTS (
							SELECT Modele.idModele
							FROM Modele
							WHERE Modele.idModele = @idModele
							)
		BEGIN
			SET @retour = 2;
			SET @msg = 'Ce modèle n''existe pas';
		END
		-- Verification qu'il existe un lot pour ce model 
		ELSE IF NOT EXISTS (
							SELECT Lot.idLot
							FROM Lot
							WHERE Lot.idModele = @idModele
							)
		BEGIN
			SET @retour = 1;
			SET @msg = 'Il n''existe aucun lot pour ce modèle !'
		END
		-- Verification que les données ne sont pas null
		IF @dateDebut IS NULL OR @dateDebut = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Date de début manquante';
		END
		-- Verificatrion que les données ne sont pas null
		ELSE IF @dateFin IS NULL OR @dateFin = ''
		BEGIN
			SET @retour = 1;
			SET @msg = 'Date de fin manquante' ;
		END
		-- Verification que la date de début ou de fin n'est pas plus ancienne que la date du jour 
		ELSE IF @dateFin > GETDATE() OR @dateDebut > GETDATE()
		BEGIN
			SET @retour = 1;
			SET @msg = 'Saisie d''une date invalide, on ne peut pas demander une date plus qui n''est pas encore passée';
		END
		-- Vérification que la date de fin est plus récente que la date de début 
		ELSE IF @dateDebut > @dateFin
		BEGIN
			SET @retour = 1;
			SET @msg = 'Une date de Fin ne peut pas être avant une date de début !';
		END
		ELSE 
		BEGIN
			SELECT Lot.idLot, Lot.moyenneHL, Lot.moyenneHT, Lot.moyenneBL, Lot.moyenneBT, Lot.miniHL, Lot.miniHT, Lot.miniBL, Lot.miniBT, Lot.maxiHL, Lot.maxiHT, Lot.maxiBL, Lot.maxiBT, Lot.ecartTypeHL, Lot.ecartTypeHT, Lot.ecartTypeBL, Lot.ecartTypeBL 
			FROM Lot
			WHERE Lot.dateProduction >= @dateDebut
			AND Lot.dateProduction <= @dateFin
			AND Lot.etatControle = 'Termine'
			AND Lot.idModele = @idModele
			AND Lot.idPresse = @idPresse
			SET @retour = 0;
			SET @msg = 'Voici les statistiques demandées !';
		END
	END TRY

		BEGIN CATCH
			SET @retour = 3;
			SET @msg = 'Exception' + ERROR_MESSAGE();
	END CATCH
	RETURN @retour;
GO

/* Trigger de mise à jour du cumul */

CREATE TRIGGER tr_incrementerCumul ON Piece
FOR INSERT
AS
	UPDATE Cumul
	SET nbrPiece = nbrPiece + 1
	FROM inserted
	WHERE Cumul.idCategorie = inserted.idCategorie AND inserted.idLot = Cumul.idLot;
	IF @@ROWCOUNT = 0
		INSERT INTO Cumul (idLot, idCategorie, nbrPiece) SELECT inserted.idLot, inserted.idCategorie, 1 FROM inserted;
GO
