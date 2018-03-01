use Pitson;
go

ALTER PROCEDURE sp_annulerLot
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

DECLARE @code int;
DECLARE @msg TypeMessageRetour;

EXECUTE @code = sp_annulerLot 1, @msg OUTPUT;
PRINT 'AnnulerLot' + convert(varchar(3), @code) + ' - ' + @msg;

EXECUTE @code = sp_annulerLot NULL, @msg OUTPUT;
PRINT 'AnnulerLot' + convert(varchar(3), @code) + ' - ' + @msg;
