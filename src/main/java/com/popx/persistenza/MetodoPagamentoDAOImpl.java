package com.popx.persistenza;


import com.popx.modello.MetodoPagamentoBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetodoPagamentoDAOImpl implements MetodoPagamentoDAO {

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
    public void addMetodoPagamento(MetodoPagamentoBean metodoPagamento) {
        String sql = "INSERT INTO MetodoPagamento (id, card_number, cvc, owner, expiration, cliente_email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, metodoPagamento.getId());
            stmt.setString(2, metodoPagamento.getCardNumber());
            stmt.setInt(3, metodoPagamento.getCvc());
            stmt.setString(4, metodoPagamento.getOwner());
            stmt.setDate(5, metodoPagamento.getExpiration());
            stmt.setString(6, metodoPagamento.getClienteEmail());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MetodoPagamentoBean getMetodoPagamento(String id) {
        String sql = "SELECT * FROM MetodoPagamento WHERE id = ?";
        MetodoPagamentoBean metodoPagamento = null;

        try (Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                metodoPagamento = new MetodoPagamentoBean();
                metodoPagamento.setId(rs.getString("id"));
                metodoPagamento.setCardNumber(rs.getString("card_number"));
                metodoPagamento.setCvc(rs.getInt("cvc"));
                metodoPagamento.setOwner(rs.getString("owner"));
                metodoPagamento.setExpiration(rs.getDate("expiration"));
                metodoPagamento.setClienteEmail(rs.getString("cliente_email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return metodoPagamento;
    }
}

