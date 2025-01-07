package com.popx.persistenza;

import com.popx.modello.OrdineBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
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
    public boolean insertOrdine(OrdineBean ordine) {
        String query = "INSERT INTO Ordine (subtotal, customer_email, status, data_ordine) VALUES (?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setFloat(1, ordine.getSubtotal());
            ps.setString(2, ordine.getCustomerEmail());
            ps.setString(3, ordine.getStatus());
            ps.setDate(4, new java.sql.Date(ordine.getDataOrdine().getTime()));

            int affectedRows = ps.executeUpdate();

            // Recupera l'ID auto-generato
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ordine.setId(rs.getInt(1));
            }

            // Restituisce true se sono state modificate righe (quindi l'inserimento è riuscito)
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Restituisce false in caso di errore
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

    @Override
    public int countOrdiniByCliente(String email) {
        int count = 0;
        try (Connection connection = ds.getConnection()) {
            String query = "SELECT COUNT(*) FROM Ordine WHERE customer_email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<OrdineBean> getOrdiniByClientePaginati(String email, int currentPage, int itemsPerPage) {
        List<OrdineBean> ordini = new ArrayList<>();
        try (Connection connection = ds.getConnection()) {
            int offset = (currentPage - 1) * itemsPerPage;
            String query = "SELECT id, subtotal, status, data_ordine FROM Ordine WHERE customer_email = ? ORDER BY data_ordine DESC LIMIT ? OFFSET ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setInt(2, itemsPerPage);
                preparedStatement.setInt(3, offset);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    OrdineBean ordine = new OrdineBean();
                    ordine.setId(resultSet.getInt("id"));
                    ordine.setSubtotal(resultSet.getFloat("subtotal"));
                    ordine.setStatus(resultSet.getString("status"));
                    ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                    ordini.add(ordine);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }

    @Override
    public List<OrdineBean> getOrdiniPaginati(int currentPage, int recordsPerPage) {
        List<OrdineBean> ordini = new ArrayList<>();
        String query = "SELECT * FROM Ordine LIMIT ? OFFSET ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, recordsPerPage);
            pstmt.setInt(2, (currentPage - 1) * recordsPerPage);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrdineBean ordine = new OrdineBean();
                    ordine.setId(rs.getInt("id"));
                    ordine.setSubtotal(rs.getFloat("subtotal"));
                    ordine.setCustomerEmail(rs.getString("customer_email"));
                    ordine.setStatus(rs.getString("status"));
                    ordine.setDataOrdine(rs.getDate("data_ordine"));
                    ordini.add(ordine);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordini;
    }

    @Override
    public int countTuttiOrdini() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM Ordine";

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public boolean updateStatus(int id, String newStatus) {
        String sql = "UPDATE Ordine SET status = ? WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
