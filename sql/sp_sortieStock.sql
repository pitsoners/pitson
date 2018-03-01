use Pitson
go

--===============================================================================
-- Procedure sortie caisse du stock
--===============================================================================

CREATE PROCEDURE sp_sortieStock(@idModele TypeIDModele, @idCategorie TypeCategorie, @qtSortie int, @msg varchar(250))
AS
	declare @return int;

	begin try
	-- verification des données entrées
		if @idModele = NULL OR @idModele = ''
		begin
			set @return = 1;
			set @msg = 'Id du modele null ou manquant !'
		end
		
		else if @idCategorie = NULL OR @idCategorie = ''
		begin
			set @return = 1;
			set @msg = 'Id du categorie null ou manquant !'
		end
		
		else if @qtSortie = NULL or @qtSortie <= 0
		begin
			set @return = 1;
			set @msg = 'Quantitée sortie invalide !'
		end

		else if Exists (SELECT qtStock FROM Stock where Stock.idModele = @idModele AND Stock.idCategorie = @idCategorie)
			begin
				if @qtSortie > (SELECT qtStock FROM Stock where Stock.idModele = @idModele AND Stock.idCategorie = @idCategorie)
				begin
					set @return = 2;
					set @msg = 'Stock insuffisant !'
				end
			end

		else if not exists(
							SELECT idModele, idCategorie
							FROM Stock
							where idModele = @idModele	AND idCategorie = @idCategorie
							)
		begin
			set @return = 2;
			set @msg = 'Pas de stock pour ce modele et cette categorie !'
		end

		else if @idCategorie <> 'Petit' OR @idCategorie <> 'Moyen' OR @idCategorie <> 'Grand' 
		begin
			set @return = 2;
			set @msg = 'Categorie invalide !'
		end

		else
		begin
			UPDATE Stock
			set qtStock = qtStock - @qtSortie
			where Stock.idModele = @idModele AND Stock.idCategorie = @idCategorie
			set @return = 0;
			set @msg = 'Quantitée sortie du stock'
		end
	end try

	begin catch
			set @return = 3;
			set @msg = 'Exception' + ERROR_MESSAGE();
	end catch

	RETURN @return;

GO
