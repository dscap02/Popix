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
    private DataSource ds;


    public CarrelloDAOImpl() {
        this.ds = DataSourceSingleton.getInstance();
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

        CarrelloBean carrello = null;
        List<ProdottoCarrelloBean> prodottiCarrello = new ArrayList<>();

        try (Connection connection = ds.getConnection()) {
            // Recupera il carrello
            try (PreparedStatement psCarrello = connection.prepareStatement(queryCarrello)) {
                psCarrello.setString(1, email);
                ResultSet rsCarrello = psCarrello.executeQuery();

                if (rsCarrello.next()) {
                    // Recupera l'ID del carrello
                    int carrelloId = rsCarrello.getInt("id");

                    // Recupera i prodotti associati al carrello
                    try (PreparedStatement psProdotti = connection.prepareStatement(queryProdotti)) {
                        psProdotti.setInt(1, carrelloId);
                        ResultSet rsProdotti = psProdotti.executeQuery();

                        while (rsProdotti.next()) {
                            ProdottoCarrelloBean prodotto = new ProdottoCarrelloBean(
                                    email,
                                    rsProdotti.getString("prodotto_id"),
                                    rsProdotti.getInt("quantity"),
                                    rsProdotti.getFloat("unitary_cost")
                            );
                            prodottiCarrello.add(prodotto);
                        }
                    }

                    // Crea il carrello
                    carrello = new CarrelloBean(email, prodottiCarrello);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carrello != null ? carrello : new CarrelloBean(email, prodottiCarrello);
    }

    @Override
    public void clearCartByUserEmail(String email) {
        String queryProdottoCarrello = "DELETE FROM ProdottoCarrello WHERE carrello_id = (SELECT id FROM Carrello WHERE cliente_email = ?)";
        String queryCarrello = "DELETE FROM Carrello WHERE cliente_email = ?";
        Connection connection = null;

        try {
            connection = ds.getConnection();

            // Avvia una transazione
            connection.setAutoCommit(false);

            try (PreparedStatement psProdottoCarrello = connection.prepareStatement(queryProdottoCarrello)) {
                psProdottoCarrello.setString(1, email);
                psProdottoCarrello.executeUpdate();
            }

            try (PreparedStatement psCarrello = connection.prepareStatement(queryCarrello)) {
                psCarrello.setString(1, email);
                psCarrello.executeUpdate();
            }

            // Conferma la transazione
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // In caso di errore, esegui il rollback
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                // Ripristina l'auto-commit
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }




}
