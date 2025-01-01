package com.popx.persistenza;

import com.popx.modello.ProdottoBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAOImpl implements ProdottoDAO {

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
    public boolean saveProdotto(ProdottoBean prodotto) {
        String query = "INSERT INTO Prodotto (id, name, description, cost, pieces_in_stock, brand, img) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, prodotto.getId());
            statement.setString(2, prodotto.getName());
            statement.setString(3, prodotto.getDescription());
            statement.setDouble(4, prodotto.getCost());
            statement.setInt(5, prodotto.getPiecesInStock());
            statement.setString(6, prodotto.getBrand());
            statement.setBytes(7, prodotto.getImg());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ProdottoBean getProdottoById(String id) {
        String query = "SELECT * FROM Prodotto WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new ProdottoBean(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("cost"),
                        resultSet.getInt("pieces_in_stock"),
                        resultSet.getString("brand"),
                        resultSet.getBytes("img")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProdottoBean> getProdottiByBrand(String brand) {
        List<ProdottoBean> prodotti = new ArrayList<>();
        String query = "SELECT * FROM Prodotto WHERE brand = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, brand);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                prodotti.add(new ProdottoBean(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("cost"),
                        resultSet.getInt("pieces_in_stock"),
                        resultSet.getString("brand"),
                        resultSet.getBytes("img")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotti;
    }

    @Override
    public List<ProdottoBean> getProdottiByBrandAndPrice(String brand, boolean ascending) {
        List<ProdottoBean> prodotti = new ArrayList<>();
        String query = "SELECT * FROM Prodotto WHERE brand = ? ORDER BY cost " + (ascending ? "ASC" : "DESC");
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, brand);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                prodotti.add(new ProdottoBean(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("cost"),
                        resultSet.getInt("pieces_in_stock"),
                        resultSet.getString("brand"),
                        resultSet.getBytes("img")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotti;
    }

    @Override
    public List<ProdottoBean> getProdottiSortedByPrice(boolean ascending) {
        List<ProdottoBean> prodotti = new ArrayList<>();
        String query = "SELECT * FROM Prodotto ORDER BY cost " + (ascending ? "ASC" : "DESC");
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                prodotti.add(new ProdottoBean(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("cost"),
                        resultSet.getInt("pieces_in_stock"),
                        resultSet.getString("brand"),
                        resultSet.getBytes("img")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotti;
    }

    @Override
    public byte[] getProductImageById(String id) {
        String query = "SELECT img FROM Prodotto WHERE id = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBytes("img");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProdottoBean> getAllProducts() {
        List<ProdottoBean> products = new ArrayList<>();
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM prodotto ORDER BY id");) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setId(rs.getString("id"));
                prodotto.setName(rs.getString("name"));
                prodotto.setCost(rs.getDouble("cost"));
                // aggiungi altre colonne se necessario
                products.add(prodotto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

}





