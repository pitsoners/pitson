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
