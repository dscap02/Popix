package com.popx.persistenza;

import com.popx.modello.UserBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO<UserBean> {
    private DataSource ds;


    public UserDAOImpl() {
        this.ds = DataSourceSingleton.getInstance();
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
        String userQuery = "INSERT INTO UtenteRegistrato (username, email, password, role) VALUES (?, ?, ?, ?)";
        String clienteQuery = "INSERT INTO Cliente (utente_registrato_email) VALUES (?)";

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false); // Disabilita l'auto-commit per supportare le transazioni

            try (PreparedStatement userStmt = conn.prepareStatement(userQuery);
                 PreparedStatement clienteStmt = conn.prepareStatement(clienteQuery)) {

                // Inserimento in UtenteRegistrato
                userStmt.setString(1, user.getUsername());
                userStmt.setString(2, user.getEmail());
                userStmt.setString(3, com.popx.servizio.SecurityService.hashPassword(user.getPassword()));
                userStmt.setString(4, "User"); // Ruolo di default 'User'
                userStmt.executeUpdate();

                // Inserimento in Cliente
                clienteStmt.setString(1, user.getEmail());
                clienteStmt.executeUpdate();

                conn.commit(); // Commit delle modifiche
                return true;
            } catch (SQLException e) {
                conn.rollback(); // Rollback in caso di errore
                throw e;
            }
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

