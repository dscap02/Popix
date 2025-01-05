package com.popx.persistenza;

import com.popx.modello.CarrelloBean;
import com.popx.modello.ProdottoBean;

import java.sql.SQLException;
import java.util.List;

public interface CarrelloDAO {
    void salvaCarrello(CarrelloBean carrello);
    CarrelloBean ottieniCarrelloPerEmail(String email);
    void clearCartByUserEmail(String email);

}

