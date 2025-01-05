package com.popx.persistenza;


import com.popx.modello.InfoPersonaliBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoPersonaliDAOImpl implements InfoPersonaliDAO {

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
    public void addInfoPersonali(InfoPersonaliBean infoPersonali) {
        String sql = "INSERT INTO InfoPersonali (cliente_email, name, surname, country, city, address, zip, birthday_date, cell) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, infoPersonali.getClienteEmail());
            stmt.setString(2, infoPersonali.getName());
            stmt.setString(3, infoPersonali.getSurname());
            stmt.setString(4, infoPersonali.getCountry());
            stmt.setString(5, infoPersonali.getCity());
            stmt.setString(6, infoPersonali.getAddress());
            stmt.setString(7, infoPersonali.getZip());
            stmt.setDate(8, infoPersonali.getBirthdayDate());
            stmt.setString(9, infoPersonali.getCell());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InfoPersonaliBean getInfoPersonali(String clienteEmail) {
        String sql = "SELECT * FROM InfoPersonali WHERE cliente_email = ?";
        InfoPersonaliBean infoPersonali = null;

        try (Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, clienteEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                infoPersonali = new InfoPersonaliBean();
                infoPersonali.setClienteEmail(rs.getString("cliente_email"));
                infoPersonali.setName(rs.getString("name"));
                infoPersonali.setSurname(rs.getString("surname"));
                infoPersonali.setCountry(rs.getString("country"));
                infoPersonali.setCity(rs.getString("city"));
                infoPersonali.setAddress(rs.getString("address"));
                infoPersonali.setZip(rs.getString("zip"));
                infoPersonali.setBirthdayDate(rs.getDate("birthday_date"));
                infoPersonali.setCell(rs.getString("cell"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return infoPersonali;
    }
}
