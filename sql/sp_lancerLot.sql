use Pitson;
go

/********************************************************************************************
 * Crée une demande de production de lot, qui sera en attente de production et de controle. *
 * @modele est l'ID du modèle dont créer le lot. Doit faire référence à un modèle existant, *
 *      ne peut être NULL.                                                                  *
 * @quantité est la quantité désirée. ce paramètre ne peut être NULL et doit être un entier *
 *      strictement positif.                                                                *
 ********************************************************************************************/
ALTER PROCEDURE sp_lancerLot
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

DECLARE @code int;
DECLARE @msg TypeMessageRetour;

EXECUTE @code = sp_lancerLot 'V8', 10, @msg OUTPUT;
PRINT convert(varchar(6), @code) + ' - ' + @msg;

EXECUTE @code = sp_lancerLot 'NOMODEL', 10, @msg OUTPUT;
PRINT convert(varchar(6), @code) + ' - ' + @msg;

EXECUTE @code = sp_lancerLot NULL, 10, @msg OUTPUT;
PRINT convert(varchar(6), @code) + ' - ' + @msg;

EXECUTE @code = sp_lancerLot 'V8', NULL, @msg OUTPUT;
PRINT convert(varchar(6), @code) + ' - ' + @msg;

EXECUTE @code = sp_lancerLot 'V8', -654, @msg OUTPUT;
PRINT convert(varchar(6), @code) + ' - ' + @msg;
