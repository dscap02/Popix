package com.popx.presentazione;

import com.popx.modello.UserBean;
import com.popx.persistenza.DataSourceSingleton;
import com.popx.persistenza.UserDAO;
import com.popx.persistenza.UserDAOImpl;
import com.popx.servizio.AuthenticationService;
import com.popx.servizio.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginTest {

    private AuthenticationService autService;
    private UserDAO<UserBean> userDAOmock;
    private DataSource mockDataSource;

    @BeforeEach
    void setupService() throws SQLException {
        // Mock del DataSource
        mockDataSource = Mockito.mock(DataSource.class);
        DataSourceSingleton.setInstanceForTest(mockDataSource);

        // Mock del comportamento del DataSource
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Mock del DAO
        userDAOmock = Mockito.mock(UserDAOImpl.class);

        // Inizializza il servizio
        autService = new AuthenticationService(userDAOmock);
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        // Dati di test
        String email = "test@example.com";
        String password = "Test123!";

        // Configura il mock del DAO per simulare un utente registrato
        UserBean mockUser = new UserBean("Mario", email, password, "User");
        when(userDAOmock.getUserByEmail(email)).thenReturn(mockUser);

        // Mock di `SecurityService` per verificare la password
        try (MockedStatic<SecurityService> mockedStatic = Mockito.mockStatic(SecurityService.class)) {
            mockedStatic.when(() -> SecurityService.checkPassword(password, password)).thenReturn(true);

            // Esegui il metodo di login
            UserBean loggedInUser = autService.login(email, password);

            // Verifica che il login sia avvenuto con successo
            assertNotNull(loggedInUser, "Il login dovrebbe restituire un utente valido");
            assertEquals(email, loggedInUser.getEmail(), "L'email dell'utente dovrebbe corrispondere");
            assertEquals("User", loggedInUser.getRole(), "Il ruolo dell'utente dovrebbe essere 'User'");

            // Verifica che il metodo `getUserByEmail` sia stato chiamato
            verify(userDAOmock, times(1)).getUserByEmail(email);
        }
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        // Dati di test
        String email = "test@example.com";
        String password = "Test123!";
        String wrongPassword = "WrongPassword";

        // Configura il mock del DAO per simulare un utente registrato
        UserBean mockUser = new UserBean("Mario", email, password, "User");
        when(userDAOmock.getUserByEmail(email)).thenReturn(mockUser);

        // Mock di `SecurityService` per simulare una password errata
        try (MockedStatic<SecurityService> mockedStatic = Mockito.mockStatic(SecurityService.class)) {
            mockedStatic.when(() -> SecurityService.checkPassword(wrongPassword, password)).thenReturn(false);

            // Esegui il metodo di login con una password errata e verifica l'eccezione
            Exception exception = assertThrows(Exception.class, () -> autService.login(email, wrongPassword));

            // Verifica il messaggio dell'eccezione
            assertEquals("Invalid credentials", exception.getMessage(), "Dovrebbe lanciare un'eccezione per credenziali non valide");

            // Verifica che il metodo `getUserByEmail` sia stato chiamato
            verify(userDAOmock, times(1)).getUserByEmail(email);
        }
    }

    @Test
    void testLoginInvalidEmail() throws Exception {
        // Dati di test
        String validEmail = "test@example.com";
        String password = "Test123!";
        String invalidEmail = "wrong@example.com";

        // Configura il mock del DAO per simulare un utente registrato
        UserBean mockUser = new UserBean("Mario", validEmail, password, "User");
        when(userDAOmock.getUserByEmail(validEmail)).thenReturn(mockUser);
        when(userDAOmock.getUserByEmail(invalidEmail)).thenReturn(null); // Email non registrata

        // Esegui il metodo di login con un'email errata e verifica l'eccezione
        Exception exception = assertThrows(Exception.class, () -> autService.login(invalidEmail, password));

        // Verifica il messaggio dell'eccezione
        assertEquals("Invalid credentials", exception.getMessage(), "Dovrebbe lanciare un'eccezione per credenziali non valide");

        // Verifica che il metodo `getUserByEmail` sia stato chiamato con l'email errata
        verify(userDAOmock, times(1)).getUserByEmail(invalidEmail);

        // Assicurati che `getUserByEmail` non sia stato chiamato con l'email valida
        verify(userDAOmock, never()).getUserByEmail(validEmail);
    }

    @Test
    void testLoginEmptyPassword() throws Exception {
        // Dati di test
        String email = "test@example.com";
        String emptyPassword = "";

        // Configura il mock del DAO per simulare un utente registrato
        UserBean mockUser = new UserBean("Mario", email, "Test123!", "User");
        when(userDAOmock.getUserByEmail(email)).thenReturn(mockUser);

        // Mock di `SecurityService` per simulare password vuota
        try (MockedStatic<SecurityService> mockedStatic = Mockito.mockStatic(SecurityService.class)) {
            mockedStatic.when(() -> SecurityService.checkPassword(emptyPassword, "Test123!")).thenReturn(false);

            // Esegui il metodo di login e verifica l'eccezione
            Exception exception = assertThrows(Exception.class, () -> autService.login(email, emptyPassword));

            // Verifica il messaggio dell'eccezione
            assertEquals("Invalid credentials", exception.getMessage(), "Dovrebbe lanciare un'eccezione per credenziali non valide");

            // Verifica che `getUserByEmail` sia stato chiamato
            verify(userDAOmock, times(1)).getUserByEmail(email);
        }
    }

    @Test
    void testLoginEmptyEmail() throws Exception {
        // Dati di test
        String emptyEmail = "";
        String password = "Test123!";

        // Configura il mock del DAO per simulare nessun utente trovato
        when(userDAOmock.getUserByEmail(emptyEmail)).thenReturn(null);

        // Esegui il metodo di login e verifica l'eccezione
        Exception exception = assertThrows(Exception.class, () -> autService.login(emptyEmail, password));

        // Verifica il messaggio dell'eccezione
        assertEquals("Invalid credentials", exception.getMessage(), "Dovrebbe lanciare un'eccezione per credenziali non valide");

        // Verifica che `getUserByEmail` sia stato chiamato con email vuota
        verify(userDAOmock, times(1)).getUserByEmail(emptyEmail);
    }

    @Test
    void testLoginNullEmailAndPassword() throws Exception {
        // Dati di test
        String nullEmail = null;
        String nullPassword = null;

        // Configura il mock del DAO per simulare nessun utente trovato
        when(userDAOmock.getUserByEmail(nullEmail)).thenReturn(null);

        // Esegui il metodo di login e verifica l'eccezione
        Exception exception = assertThrows(Exception.class, () -> autService.login(nullEmail, nullPassword));

        // Verifica il messaggio dell'eccezione
        assertEquals("Invalid credentials", exception.getMessage(), "Dovrebbe lanciare un'eccezione per credenziali non valide");

        // Verifica che `getUserByEmail` sia stato chiamato con email nulla
        verify(userDAOmock, times(1)).getUserByEmail(nullEmail);
    }
}

