use Pitson
go

--===============================================================================
-- View qui permet de vérifier les catégories de piéces produites par une machine
--===============================================================================

CREATE view StatMachines
AS
SELECT m.idPresse, m.libellePresse, p.idCategorie, COUNT(p.idPiece) as production
FROM Machine m
JOIN Lot l ON l.idPresse = m.idPresse
JOIN Piece p on p.idLot = l.idLot
GROUP BY m.idPresse, m.libellePresse, p.idCategorie
