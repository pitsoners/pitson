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
