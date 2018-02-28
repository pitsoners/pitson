use Pitson
go

--===============================================================================
-- Procedure qui permet de verifier si il y a des machines libres
--===============================================================================

CREATE Procedure sp_machineLibre
AS
BEGIN

SELECT *
FROM Machine
Where enService = 1 
AND NOT EXISTS (SELECT etatProduction FROM Lot Where etatProduction = 'EnCours' AND lot.idPresse = Machine.idPresse)

END
