package com.popx.persistenza;

import com.popx.modello.CarrelloBean;
import com.popx.modello.ProdottoBean;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public void salvaCarrello(String clienteEmail, List<ProdottoBean> carrello, Map<String, Integer> quantitaMap) {
        try (Connection connection = ds.getConnection()) {
            // Verifica se il carrello esiste
            String queryCheckCarrello = "SELECT id FROM Carrello WHERE cliente_email = ?";
            String carrelloId;
            try (PreparedStatement psCheck = connection.prepareStatement(queryCheckCarrello)) {
                psCheck.setString(1, clienteEmail);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        carrelloId = rs.getString("id");
                    } else {
                        // Crea un nuovo carrello
                        carrelloId = UUID.randomUUID().toString();
                        String queryCreateCarrello = "INSERT INTO Carrello (id, cliente_email) VALUES (?, ?)";
                        try (PreparedStatement psCreate = connection.prepareStatement(queryCreateCarrello)) {
                            psCreate.setString(1, carrelloId);
                            psCreate.setString(2, clienteEmail);
                            psCreate.executeUpdate();
                        }
                    }
                }
            }

            // Aggiungi i prodotti al carrello
            String queryInsertProdottoCarrello = "INSERT INTO ProdottoCarrello (id, carrello_id, prodotto_id, quantity, unitary_cost) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE quantity = VALUES(quantity)";
            try (PreparedStatement psInsert = connection.prepareStatement(queryInsertProdottoCarrello)) {
                for (ProdottoBean prodotto : carrello) {
                    psInsert.setString(1, UUID.randomUUID().toString());
                    psInsert.setString(2, carrelloId);
                    psInsert.setString(3, prodotto.getId());
                    psInsert.setInt(4, quantitaMap.get(prodotto.getId()));
                    psInsert.setDouble(5, prodotto.getCost());
                    psInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Integer> recuperaCarrello(String clienteEmail) {
        Map<String, Integer> carrello = new HashMap<>();
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT pc.prodotto_id, pc.quantity " +
                    "FROM ProdottoCarrello pc " +
                    "INNER JOIN Carrello c ON pc.carrello_id = c.id " +
                    "WHERE c.cliente_email = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, clienteEmail);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        carrello.put(rs.getString("prodotto_id"), rs.getInt("quantity"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carrello;
    }

    @Override
    public void rimuoviCarrello(String clienteEmail) {
        try (Connection connection = ds.getConnection()) {
            // Recupera il carrello ID
            String queryGetCarrello = "SELECT id FROM Carrello WHERE cliente_email = ?";
            String carrelloId = null;
            try (PreparedStatement psGet = connection.prepareStatement(queryGetCarrello)) {
                psGet.setString(1, clienteEmail);
                try (ResultSet rs = psGet.executeQuery()) {
                    if (rs.next()) {
                        carrelloId = rs.getString("id");
                    }
                }
            }

            if (carrelloId != null) {
                // Elimina i prodotti associati al carrello
                String queryDeleteProdotti = "DELETE FROM ProdottoCarrello WHERE carrello_id = ?";
                try (PreparedStatement psDelete = connection.prepareStatement(queryDeleteProdotti)) {
                    psDelete.setString(1, carrelloId);
                    psDelete.executeUpdate();
                }

                // Elimina il carrello
                String queryDeleteCarrello = "DELETE FROM Carrello WHERE id = ?";
                try (PreparedStatement psDeleteCarrello = connection.prepareStatement(queryDeleteCarrello)) {
                    psDeleteCarrello.setString(1, carrelloId);
                    psDeleteCarrello.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
