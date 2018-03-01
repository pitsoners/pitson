/***** PROGRAMME TEST PROCEDURE sp_statLotReduit et sp_statLotDetail *****/

USE Pitson
GO 

DECLARE @idLot int;
DECLARE @retour int;
DECLARE @message varchar(250);

/*PROC sp_statLotReduit */
PRINT '';
PRINT '';
PRINT '--------------------Test Procédure sp_statLotReduit----------------------';
PRINT '';

-- Test @IdLot NULL
PRINT 'Test @IdLot NULL';
SET @idLot = NULL;

EXEC @retour = dbo.sp_statLotReduit @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot = ''
PRINT 'Test @IdLot = ''''';
SET @idLot = '';

EXEC @retour = dbo.sp_statLotReduit @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot inexistant
PRINT 'Test @IdLot =  10';
SET @idLot = 10;

EXEC @retour = dbo.sp_statLotReduit @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant non lancé
PRINT 'Test @IdLot non lancé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotReduit @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant lancé
PRINT 'Test @IdLot lancé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotReduit @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant non controlé
PRINT 'Test @IdLot non controlé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotReduit @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant lancé et controlé
PRINT 'Test @IdLot lancé et controlé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotReduit @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';


/*PROC sp_statLotDetail */
PRINT '';
PRINT '';
PRINT '--------------------Test Procédure sp_statLotDetail----------------------';
PRINT '';

-- Test @IdLot NULL
PRINT 'Test @IdLot NULL';
SET @idLot = NULL;

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot = ''
PRINT 'Test @IdLot = ''''';
SET @idLot = '';

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot inexistant
PRINT 'Test @IdLot =  10';
SET @idLot = 10;

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant
PRINT 'Test @IdLot =  3';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant non lancé
PRINT 'Test @IdLot non lancé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant lancé
PRINT 'Test @IdLot lancé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant non controlé
PRINT 'Test @IdLot non controlé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';

-- Test @IdLot existant lancé et controlé
PRINT 'Test @IdLot lancé et controlé';
SET @idLot = 3;

EXEC @retour = dbo.sp_statLotDetail @idLot, @message OUTPUT;

PRINT 'Code retour = ' + CAST (@retour as char(1));
PRINT @message;
PRINT '';
