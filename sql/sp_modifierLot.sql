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
