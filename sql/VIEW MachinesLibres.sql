use Pitson
go

--===============================================================================
-- View qui permet de vérifier si il y a des machines libres
--===============================================================================

CREATE view MachinesLibres
AS

SELECT idPresse, libellePresse
FROM Machine
Where enService = 1 
AND NOT EXISTS (SELECT etatProduction FROM Lot Where etatProduction = 'EnCours' AND lot.idPresse = Machine.idPresse)
