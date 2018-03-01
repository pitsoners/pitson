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
-- Description: Récupérer les stats réduits d'un lo
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
