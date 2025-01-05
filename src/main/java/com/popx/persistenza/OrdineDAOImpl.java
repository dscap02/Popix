package com.popx.persistenza;

import com.popx.modello.OrdineBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAOImpl implements OrdineDAO {

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
    public void insertOrdine(OrdineBean ordine) {
        String query = "INSERT INTO Ordine (subtotal, customer_email, status, data_ordine) VALUES (?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setFloat(1, ordine.getSubtotal());
            ps.setString(2, ordine.getCustomerEmail());
            ps.setString(3, ordine.getStatus());
            ps.setDate(4, new java.sql.Date(ordine.getDataOrdine().getTime()));

            ps.executeUpdate();

            // Recupera l'ID auto-generato
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ordine.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrdineBean getOrdineById(int id) {
        String query = "SELECT * FROM Ordine WHERE id = ?";
        OrdineBean ordine = null;

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ordine = new OrdineBean();
                ordine.setId(rs.getInt("id"));
                ordine.setSubtotal(rs.getFloat("subtotal"));
                ordine.setCustomerEmail(rs.getString("customer_email"));
                ordine.setStatus(rs.getString("status"));
                ordine.setDataOrdine(rs.getDate("data_ordine"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordine;
    }

    @Override
    public List<OrdineBean> getAllOrdini() {
        String query = "SELECT * FROM Ordine";
        List<OrdineBean> ordini = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setId(rs.getInt("id"));
                ordine.setSubtotal(rs.getFloat("subtotal"));
                ordine.setCustomerEmail(rs.getString("customer_email"));
                ordine.setStatus(rs.getString("status"));
                ordine.setDataOrdine(rs.getDate("data_ordine"));
                ordini.add(ordine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordini;
    }

    @Override
    public List<OrdineBean> getOrdiniByCliente(String clienteEmail) {
        String query = "SELECT * FROM Ordine WHERE customer_email = ?";
        List<OrdineBean> ordini = new ArrayList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, clienteEmail);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setId(rs.getInt("id"));
                ordine.setSubtotal(rs.getFloat("subtotal"));
                ordine.setCustomerEmail(rs.getString("customer_email"));
                ordine.setStatus(rs.getString("status"));
                ordine.setDataOrdine(rs.getDate("data_ordine"));
                ordini.add(ordine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordini;
    }
}
