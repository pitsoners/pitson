USE Pitson;
go

-- affecte un employé à un role. crée l'utilisateur dans la base de donnée et l'affecte au rôle donné.
-- @employe est le nom systeme de login à affecter
-- @role est le nom systeme du role a affecter. Un employé ne peut etre affecté qu'à un seul role.
-- Si @role vaut NULL, l'employe n'est plus affecté à aucun role, et l'utilisateur de base de donnée correspondant est supprimé.
-- retourne 0 lorsque l'opération est un succès
ALTER PROCEDURE assignerEmploye
(
	@employe sysname,
	@role sysname,
	@messageRetour TypeMessageRetour OUTPUT
)
AS
BEGIN
	DECLARE @codeRetour int;
	SET @codeRetour = -1;
	SET @messageRetour = 'non implémenté';
	IF @employe IS NULL
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 1 (@employe) ne peut être null';
		END
	ELSE IF @employe = 'dbo'
		BEGIN
			SET @codeRetour = 2;
			SET @messageRetour = 'Le paramètre 1 (@employe) ne peut être ''dbo''. Le propriétaire de la base est un utilisateur réservé.';
		END
	ELSE IF @role IS NOT NULL AND @role NOT IN ('ResponsableApplication', 'ResponsableAtelier', 'ResponsablePresse', 'Controleur', 'Magasinier', 'ResponsableQualite')
		BEGIN
			SET @codeRetour = 1;
			SET @messageRetour = 'Le paramètre 2 (@role = ''' + @role + ''') est incorrect. (doit valoir ''ResponsableApplication'', ''ResponsableAtelier'', ''ResponsablePresse'', ''Controleur'', ''Magasinier'', ''ResponsableQualite'' ou NULL)';
		END
	ELSE 
	BEGIN
		BEGIN TRANSACTION
		DECLARE @sql nvarchar(512);
		BEGIN TRY
			-- suppresion préalable de l'utilisateur s'il existe. ce qui revient à le retirer de tout role auquel il était affecté
			SET @sql = 'DROP USER ' + @employe;
			EXECUTE sp_executesql @sql;
		END TRY
		BEGIN CATCH
			-- si une erreur se produit, c'est que l'utilisateur n'existait pas.
		END CATCH
		-- création de l'utilisateur
		BEGIN TRY
		IF @role IS NOT NULL
			BEGIN
				SET @sql = 'CREATE USER ' + @employe;
				EXECUTE sp_executesql @sql;
				SET @sql = 'ALTER ROLE ' + @role + ' ADD MEMBER ' + @employe;
				EXECUTE sp_executesql @sql;
				COMMIT TRANSACTION
				SET @codeRetour = 0;
				SET @messageRetour = 'L''utilisateur ''' + @employe + ''' est affecté au role ''' + @role + '''.';
			END
		ELSE
			BEGIN
				SET @codeRetour = 0;
				SET @messageRetour = 'L''utilisateur ''' + @employe + ''' a bien été supprimé.';
				COMMIT TRANSACTION;
			END
		END TRY
		BEGIN CATCH
			-- si erreur creation, abandon!
			SET @codeRetour = 3;
			SET @messageRetour = 'Une erreur de base de données est survenue lors de la création de l''utilisateur ''' + @employe + ''' : ' + ERROR_MESSAGE();
			ROLLBACK TRANSACTION;
		END CATCH
	END
	RETURN @codeRetour;
END

GO

DECLARE  @code int;
DECLARE @message TypeMessageRetour;

EXECUTE @code = assignerEmploye 'preda', NULL, @message OUTPUT;
PRINT convert(varchar(10), @code) + ' - ' + @message;

EXECUTE @code = assignerEmploye 'preda', 'Controleur', @message OUTPUT;
PRINT convert(varchar(10), @code) + ' - ' + @message;

EXECUTE @code = assignerEmploye 'preda', 'Magasinier', @message OUTPUT;
PRINT convert(varchar(10), @code) + ' - ' + @message;

EXECUTE @code = assignerEmploye NULL, 'Controleur', @message OUTPUT;
PRINT convert(varchar(10), @code) + ' - ' + @message;

EXECUTE @code = assignerEmploye 'preda', 'Pirouette', @message OUTPUT;
PRINT convert(varchar(10), @code) + ' - ' + @message;
