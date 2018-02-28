USE Pitson
GO
-- retourne un résultat contenant le nom des rôles affectés à l'utilisateur actuellement connecté.
-- les rôles sont ceux définis dans la base de données (cf 'sql/sql_roles.sql')
-- le code de retour est toujours 0
ALTER PROCEDURE sp_retournerRole
AS
BEGIN
	DECLARE @codeRetour int;
	SET @codeRetour = 0;
	SELECT r.name
	FROM sys.database_role_members as rm
	JOIN sys.database_principals as r ON rm.role_principal_id = r.principal_id
	WHERE rm.member_principal_id = DATABASE_PRINCIPAL_ID();
	RETURN @codeRetour
END
