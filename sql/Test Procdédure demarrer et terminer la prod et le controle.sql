/** TEST PROCEDURE DEMARRER - TERMINER PROD/CONTROL **/

USE Pitson
GO

DECLARE @idLot int;
DECLARE @idMachine int;
DECLARE @retour int;
DECLARE @message varchar (255);


/* Test Procédure sp_demarrerProd */
PRINT 'Test Procédure sp_demarrerProd';
PRINT '';
PRINT '';
PRINT '';

-- Test @IdLot NULL
PRINT 'Test @IdLot NULL';
SET @idLot = NULL;
SET @idMachine = 1;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot = ''
PRINT 'Test @IdLot = ''''';
SET @idLot = '';
SET @idMachine = 1;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot inexistant
PRINT 'Test @IdLot =  10';
SET @idLot = 10;
SET @idMachine = 1;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @idMachine NULL
PRINT 'Test @idMachine NULL';
SET @idLot = 1;
SET @idMachine = NULL;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @idMachine = ''
PRINT 'Test @idMachine = ''''';
SET @idLot = 1;
SET @idMachine = '';

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @idMachine inexistant
PRINT 'Test @idMachine inexistant';
SET @idLot = 1;
SET @idMachine = 10;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot = 1
PRINT 'Test @IdLot = 1';
SET @idLot = 1;
SET @idMachine = 1;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test Lot déja en production 
PRINT 'Test Lot déja en production';
SET @idLot = 1;
SET @idMachine = 1;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test Lot en cours de test
PRINT 'Test Lot en cours de test';
SET @idLot = 1;
SET @idMachine = 1;

EXEC @retour = dbo.sp_demarrerProd @idLot, @idMachine, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';



/********************************************************************************/
/* Test Procédure sp_demarrerConrtrole */
PRINT 'Test Procédure sp_demarrerControle';
PRINT '';
PRINT '';
PRINT '';

-- Test @IdLot NULL
PRINT 'Test @IdLot NULL';
SET @idLot = NULL;

EXEC @retour = dbo.sp_demarrerControle @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot = ''
PRINT 'Test @IdLot = ''''';
SET @idLot = '';
SET @idMachine = 1;

EXEC @retour = dbo.sp_demarrerControle @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot inexistant
PRINT 'Test @IdLot =  10';
SET @idLot = 10;

EXEC @retour = dbo.sp_demarrerControle @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot = 1
PRINT 'Test @IdLot = 1';
SET @idLot = 1;

EXEC @retour = dbo.sp_demarrerControle @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

