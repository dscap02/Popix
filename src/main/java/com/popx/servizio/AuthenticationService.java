package com.popx.servizio;

import com.popx.modello.UserBean;
import com.popx.persistenza.UserDAO;
import com.popx.persistenza.UserDAOImpl;

public class AuthenticationService {
    private UserDAO userDAO = new UserDAOImpl();

    public UserBean login(String email, String password) throws Exception {
        UserBean user = userDAO.getUserByEmail(email);
        if (user != null && SecurityService.checkPassword(password, user.getPassword())) {
            return user;
        }
        throw new Exception("Invalid credentials");
    }

    public boolean registerUser(UserBean user) throws Exception {
        if (userDAO.getUserByEmail(user.getEmail()) == null) {
            user.setPassword(SecurityService.hashPassword(user.getPassword()));
            return userDAO.saveUser(user);
        }
        throw new Exception("User already exists");
    }
}