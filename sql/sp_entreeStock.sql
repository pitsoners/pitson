use Pitson
go

--===============================================================================
-- Procedure entrée caisse dans le stock
--===============================================================================

CREATE PROCEDURE sp_entreeStock(@idModele TypeIDModele, @idCategorie TypeCategorie, @qtEntree int, @msg varchar(250))
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
		
		else if @qtEntree = NULL or @qtEntree <= 0
		begin
			set @return = 1;
			set @msg = 'Quantitée entrée invalide !'
		end

		else if not exists(
							SELECT idModele
							FROM Modele
							where idModele = @idModele
							)
		begin
			set @return = 2;
			set @msg = 'Modele inexistant !'
		end

		else if @idCategorie <> 'Petit' OR @idCategorie <> 'Moyen' OR @idCategorie <> 'Grand' 
		begin
			set @return = 2;
			set @msg = 'Categorie invalide !'
		end

		else
		begin
			UPDATE Stock
			set qtStock = qtStock + @qtEntree
			where Stock.idModele = @idModele AND Stock.idCategorie = @idCategorie
			if @@ROWCOUNT = 0
				INSERT INTO Stock(idModele, idCategorie, qtStock) values(@idModele, @idCategorie, @qtEntree)
				set @return = 0;
				set @msg = 'Quantitée ajoutée au stock'
		end
	end try

	begin catch
			set @return = 3;
			set @msg = 'Exception' + ERROR_MESSAGE();
	end catch

	RETURN @return;

GO
