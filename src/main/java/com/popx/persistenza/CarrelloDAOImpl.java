package com.popx.persistenza;

import com.popx.modello.CarrelloBean;
import com.popx.modello.ProdottoBean;
import com.popx.modello.ProdottoCarrelloBean;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class CarrelloDAOImpl implements CarrelloDAO {
    private static final DataSource ds;

    static {
        try {
            Context ctx = new InitialContext();
            Context env = (Context) ctx.lookup("java:comp/env");
            ds = (DataSource) env.lookup("jdbc/Popix");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void salvaCarrello(CarrelloBean carrello) {
        String queryCarrello = "INSERT INTO Carrello (cliente_email) VALUES (?)";
        String queryProdottoCarrello = "INSERT INTO ProdottoCarrello (carrello_id, prodotto_id, quantity, unitary_cost) VALUES (?, ?, ?, ?)";

        try (Connection connection = ds.getConnection()) {
            try (PreparedStatement psCarrello = connection.prepareStatement(queryCarrello)) {
                psCarrello.setString(1, carrello.getClienteEmail());
                psCarrello.executeUpdate();

                for (ProdottoCarrelloBean prodotto : carrello.getProdottiCarrello()) {
                    try (PreparedStatement psProdottoCarrello = connection.prepareStatement(queryProdottoCarrello)) {
                        psProdottoCarrello.setString(1, carrello.getClienteEmail());
                        psProdottoCarrello.setString(2, prodotto.getProdottoId());
                        psProdottoCarrello.setInt(3, prodotto.getQuantity());
                        psProdottoCarrello.setFloat(4, prodotto.getUnitaryCost());
                        psProdottoCarrello.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CarrelloBean ottieniCarrelloPerEmail(String email) {
        String queryCarrello = "SELECT * FROM Carrello WHERE cliente_email = ?";
        String queryProdotti = "SELECT * FROM ProdottoCarrello WHERE carrello_id = ?";

        List<ProdottoCarrelloBean> prodottiCarrello = new ArrayList<>();
        try (Connection connection = ds.getConnection()) {
            try (PreparedStatement psCarrello = connection.prepareStatement(queryCarrello)) {
                psCarrello.setString(1, email);
                ResultSet rsCarrello = psCarrello.executeQuery();

                if (rsCarrello.next()) {
                    try (PreparedStatement psProdotti = connection.prepareStatement(queryProdotti)) {
                        psProdotti.setString(1, rsCarrello.getString("cliente_email"));
                        ResultSet rsProdotti = psProdotti.executeQuery();

                        while (rsProdotti.next()) {
                            ProdottoCarrelloBean prodotto = new ProdottoCarrelloBean(
                                    rsCarrello.getString("cliente_email"),
                                    rsProdotti.getString("prodotto_id"),
                                    rsProdotti.getInt("quantity"),
                                    rsProdotti.getFloat("unitary_cost")
                            );
                            prodottiCarrello.add(prodotto);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new CarrelloBean(email, prodottiCarrello);
    }


}
