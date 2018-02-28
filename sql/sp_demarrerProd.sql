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
USE Pitson
GO

ALTER PROC sp_demarrerProd (@idLot int, @idMachine int, @msg varchar(250) OUTPUT)
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
