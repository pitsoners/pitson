USE Pitson;
GO

/************************************************************************************
 * Saisie d'une pièce d'un lot														*
 * ---------------------------														*
 * @idLot = id du lot de la pièce													*
 * @hl, ht, bl, bt = cotes mesurées, non NULL										*
 * @defautVisuel = 0 si pas de défaut constaté; 1 si un défaut est constaté			*
 *					si NULL, alors considéré comme valant 0							*
 * @commentaire = contient des précisions sur l'état de la pièce (facultatif)		*
 * SORTIE @messageRetour contient un information sur l'exécution de la procédure	*
 * RETOUR = 0 si la saisie s'est correctement déroulée								*
 ************************************************************************************/
ALTER PROCEDURE sp_saisirPiece
(
	@idLot int,
	@ht TypeMesure,
	@hl TypeMesure,
	@bt TypeMesure,
	@bl TypeMesure,
	@defautVisuel bit,
	@commentaire TypeCommentaire,
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
								EXECUTE dbo.sp_categoriePiece @ht, @hl,@bt, @bl, @defautVisuel, @cat OUTPUT;
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

DECLARE @code int;
DECLARE @msg TypeMessageRetour;
DECLARE @idPiece int;

EXECUTE @code = sp_saisirPiece 2, -2.5,-1.2,-2.3,-1.4,0, NULL, @idPiece OUTPUT, @msg OUTPUT;
PRINT 'Saisir piece : ' + convert(varchar(3), @code) + ' - ' + @msg;

EXECUTE @code = sp_saisirPiece 2, 2,0,3,0,0, NULL, @idPiece OUTPUT,@msg OUTPUT;
PRINT 'Saisir piece : ' + convert(varchar(3), @code) + ' - ' + @msg;

EXECUTE @code = sp_saisirPiece 2, 0,0,0,0,1, NULL, @idPiece OUTPUT,@msg OUTPUT;
PRINT 'Saisir piece : ' + convert(varchar(3), @code) + ' - ' + @msg;


SELECT * FROM Piece
