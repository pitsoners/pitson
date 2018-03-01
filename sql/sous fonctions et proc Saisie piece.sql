use Pitson;
go

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

SELECT * FROM Categorie;

DECLARE @cat TypeCategorie;

EXECUTE sp_categoriePiece 1, 1, 1, 1, 0, @cat OUTPUT;
PRINT @cat;

EXECUTE sp_categoriePiece -5, 1, 1, 1, 0, @cat OUTPUT;
PRINT @cat;

EXECUTE sp_categoriePiece -3, -2.5, -2.5, -2.5, 0, @cat OUTPUT;
PRINT @cat;

EXECUTE sp_categoriePiece -3, -1.5, -1.5, -1.5, 0, @cat OUTPUT;
PRINT @cat;

EXECUTE sp_categoriePiece 0.5, 2.5, 0.65, 1, 0, @cat OUTPUT;
PRINT @cat;

EXECUTE sp_categoriePiece 1.5, 1.2, 1.5, 1.6, 0, @cat OUTPUT;
PRINT @cat;
