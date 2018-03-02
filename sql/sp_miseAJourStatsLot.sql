use pitson;
go

/*
 * Procédure qui met à jour les stats réduites d'un lot dont le controle n'est pas terminé.
 * @idLot : identifiant du lot à mettre à jour
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

DECLARE @code int;
DECLARE @msg TypeMessageRetour;

EXECUTE @code = sp_mettreAJourStatsLot 2, @msg OUTPUT;
PRINT 'MAJ stats Lot : ' + convert(varchar(6), @code) + ' - ' + @msg;

EXECUTE @code = sp_mettreAJourStatsLot NULL, @msg OUTPUT;
PRINT 'MAJ stats Lot : ' + convert(varchar(6), @code) + ' - ' + @msg;

EXECUTE @code = sp_mettreAJourStatsLot 3, @msg OUTPUT;
PRINT 'MAJ stats Lot : ' + convert(varchar(6), @code) + ' - ' + @msg;

SELECT * FROM LOT
