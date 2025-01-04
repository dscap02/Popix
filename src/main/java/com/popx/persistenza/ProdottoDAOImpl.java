package com.popx.persistenza;

import com.popx.modello.ProdottoBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
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
        String query = "INSERT INTO Prodotto (id, name, description, cost, pieces_in_stock, brand, img, figure) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, prodotto.getId());
            statement.setString(2, prodotto.getName());
            statement.setString(3, prodotto.getDescription());
            statement.setDouble(4, prodotto.getCost());
            statement.setInt(5, prodotto.getPiecesInStock());
            statement.setString(6, prodotto.getBrand());
            statement.setBytes(7, prodotto.getImg());
            statement.setString(8, prodotto.getFigure());  // Set the figure field
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
                        resultSet.getBytes("img"),
                        resultSet.getString("figure")  // Retrieve the figure field
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
                        resultSet.getBytes("img"),
                        resultSet.getString("figure")  // Retrieve the figure field
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
                        resultSet.getBytes("img"),
                        resultSet.getString("figure")  // Retrieve the figure field
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
                        resultSet.getBytes("img"),
                        resultSet.getString("figure")  // Retrieve the figure field
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
        String query = "SELECT * FROM Prodotto ORDER BY id";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setId(rs.getString("id"));
                prodotto.setName(rs.getString("name"));
                prodotto.setDescription(rs.getString("description"));
                prodotto.setCost(rs.getDouble("cost"));
                prodotto.setPiecesInStock(rs.getInt("pieces_in_stock")); // Recupero del valore
                prodotto.setBrand(rs.getString("brand"));
                prodotto.setImg(rs.getBytes("img"));
                prodotto.setFigure(rs.getString("figure"));
                products.add(prodotto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    @Override
    public List<ProdottoBean> getRandomProducts(int limit) throws SQLException {
        List<ProdottoBean> products = new ArrayList<>();
        String query = "SELECT * FROM prodotto ORDER BY RAND() LIMIT ?";  // Usato ORDER BY RAND() per ottenere risultati casuali

        try (Connection con = ds.getConnection(); // Assicurati che ds sia un DataSource valido
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, limit);  // Imposta il parametro limit dinamicamente

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProdottoBean product = new ProdottoBean();
                    product.setId(rs.getString("id"));  // Verifica che 'id' sia di tipo String, altrimenti cambia a Integer
                    product.setName(rs.getString("name"));
                    product.setBrand(rs.getString("brand"));
                    product.setCost(rs.getDouble("cost"));
                    product.setImg(rs.getBytes("img"));
                    product.setDescription(rs.getString("description"));
                    product.setFigure(rs.getString("figure"));  // Set the figure field
                    products.add(product);
                }
            }
        }
        return products;
    }

    @Override
    public void updateProductQtyInCart(HttpSession session, String productId, int qty) {
        List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");
        if (cart != null) {
            for (ProdottoBean product : cart) {
                if (product.getId().equals(productId)) {
                    product.setQty(qty);
                    break;
                }
            }
        }
    }

    @Override
    public int getProductQtyInCart(HttpSession session, String productId) {
        List<ProdottoBean> cart = (List<ProdottoBean>) session.getAttribute("cart");
        if (cart != null) {
            for (ProdottoBean product : cart) {
                if (product.getId().equals(productId)) {
                    return product.getQty();
                }
            }
        }
        return 0;
    }

    @Override
    public void saveCartToDatabase(String userEmail, List<ProdottoBean> cart) throws SQLException {
        String insertCartQuery = "INSERT INTO Carrello (cliente_email) VALUES (?) " +
                "ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)";

        String insertProductCartQuery = "INSERT INTO ProdottoCarrello (carrello_id, prodotto_id, quantity, unitary_cost) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = VALUES(quantity), unitary_cost = VALUES(unitary_cost)";

        try (Connection connection = ds.getConnection();
             PreparedStatement cartStmt = connection.prepareStatement(insertCartQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement productCartStmt = connection.prepareStatement(insertProductCartQuery)) {

            // Inserisci o aggiorna il carrello
            cartStmt.setString(1, userEmail);
            cartStmt.executeUpdate();

            // Ottieni l'ID del carrello
            int cartId;
            try (ResultSet rs = cartStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cartId = rs.getInt(1);
                } else {
                    throw new SQLException("Impossibile ottenere l'ID del carrello.");
                }
            }

            // Inserisci o aggiorna i prodotti nel carrello
            for (ProdottoBean product : cart) {
                productCartStmt.setInt(1, cartId);
                productCartStmt.setString(2, product.getId());
                productCartStmt.setInt(3, product.getQty());
                productCartStmt.setDouble(4, product.getCost());

                productCartStmt.executeUpdate();
            }
        }
    }

    @Override
    public List<ProdottoBean> getCartByUserEmail(String userEmail) throws SQLException {
        List<ProdottoBean> cart = new ArrayList<>();
        String query = "SELECT p.id, p.name, p.pieces_in_stock, p.cost, pc.quantity " +
                "FROM Prodotto p " +
                "JOIN ProdottoCarrello pc ON p.id = pc.prodotto_id " +
                "JOIN Carrello c ON pc.carrello_id = c.id " +
                "WHERE c.cliente_email = ?";


        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProdottoBean prodotto = new ProdottoBean();
                    prodotto.setId(rs.getString("id"));
                    prodotto.setName(rs.getString("name"));
                    prodotto.setPiecesInStock(rs.getInt("pieces_in_stock"));
                    prodotto.setCost(rs.getDouble("cost"));
                    prodotto.setQty(rs.getInt("quantity"));
                    cart.add(prodotto);
                }
            }
        }
        return cart;
    }

    public void updateCartProductQuantityInDatabase(String userEmail, String productId, int qty) throws SQLException {
        String query = "UPDATE ProdottoCarrello pc " +
                "JOIN Carrello c ON pc.carrello_id = c.id " +
                "SET pc.quantity = ? " +
                "WHERE c.cliente_email = ? AND pc.prodotto_id = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, qty);
            stmt.setString(2, userEmail);
            stmt.setString(3, productId);

            stmt.executeUpdate();
        }
    }

    public void removeProductFromCart(String userEmail, String productId) throws SQLException {
        String query = "DELETE pc FROM ProdottoCarrello pc " +
                "JOIN Carrello c ON pc.carrello_id = c.id " +
                "WHERE c.cliente_email = ? AND pc.prodotto_id = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userEmail);
            stmt.setString(2, productId);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteProductById(String id) throws SQLException {
        String deleteProductQuery = "DELETE FROM Prodotto WHERE id = ?";
        String deleteFromCartQuery = "DELETE FROM ProdottoCarrello WHERE prodotto_id = ?";

        try (Connection connection = ds.getConnection()) {
            // Rimuovi il prodotto dalla tabella ProdottoCarrello
            try (PreparedStatement cartStatement = connection.prepareStatement(deleteFromCartQuery)) {
                cartStatement.setString(1, id);
                cartStatement.executeUpdate();
            }

            // Rimuovi il prodotto dalla tabella Prodotto
            try (PreparedStatement productStatement = connection.prepareStatement(deleteProductQuery)) {
                productStatement.setString(1, id);
                productStatement.executeUpdate();
            }
        }
    }

    @Override
    public boolean updateProduct(ProdottoBean prodottoBean) {
        String queryProdotto = "UPDATE prodotto SET name = ?, cost = ?, brand = ?, figure = ?, pieces_in_stock = ?, img = ?, description = ? WHERE id = ?";
        String queryProdottoCarrello = "UPDATE prodottocarrello SET unitary_cost = ? WHERE prodotto_id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmtProdotto = conn.prepareStatement(queryProdotto);
             PreparedStatement stmtProdottoCarrello = conn.prepareStatement(queryProdottoCarrello)) {

            // Aggiorna la tabella 'prodotto'
            stmtProdotto.setString(1, prodottoBean.getName());
            stmtProdotto.setDouble(2, prodottoBean.getCost());
            stmtProdotto.setString(3, prodottoBean.getBrand());
            stmtProdotto.setString(4, prodottoBean.getFigure());
            stmtProdotto.setInt(5, prodottoBean.getPiecesInStock());
            stmtProdotto.setBytes(6, prodottoBean.getImg());
            stmtProdotto.setString(7, prodottoBean.getDescription());
            stmtProdotto.setString(8, prodottoBean.getId());

            int rowsUpdatedProdotto = stmtProdotto.executeUpdate();

            // Aggiorna la tabella 'prodotto_carrello'
            stmtProdottoCarrello.setDouble(1, prodottoBean.getCost()); // Cost da aggiornare in ProdottoCarrello
            stmtProdottoCarrello.setString(2, prodottoBean.getId());

            int rowsUpdatedProdottoCarrello = stmtProdottoCarrello.executeUpdate();

            return rowsUpdatedProdotto > 0 && rowsUpdatedProdottoCarrello > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
