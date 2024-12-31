package com.popx.persistenza;

import com.popx.modello.UserBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO<UserBean> {
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
    public UserBean getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM UtenteRegistrato WHERE email = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserBean(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        }
        return null;
    }

    @Override
    public boolean saveUser(UserBean user) throws SQLException {
        String query = "INSERT INTO UtenteRegistrato (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, com.popx.servizio.SecurityService.hashPassword( user.getPassword()));
            stmt.setString(4, user.getRole());
            return stmt.executeUpdate() > 0;
        }
    }

    // Metodo disponibile solo in UserDAOImpl
    public List<UserBean> getAllUsers() throws SQLException {
        List<UserBean> users = new ArrayList<>();
        String query = "SELECT * FROM UtenteRegistrato";
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new UserBean(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        }
        return users;
    }
}
