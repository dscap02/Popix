package com.popx.persistenza;

import com.popx.modello.UserBean;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    UserBean getUserByEmail(String email) throws SQLException;
    boolean saveUser(UserBean user) throws SQLException;
    List<UserBean> getAllUsers() throws SQLException;
}