package com.popx.presentazione;

import com.popx.modello.UserBean;
import com.popx.persistenza.DataSourceSingleton;
import com.popx.persistenza.UserDAOImpl;
import com.popx.servizio.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrazioneServiceTest {

    private AuthenticationService registrazioneService;
    private UserDAOImpl userDAOMock;
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
        userDAOMock = Mockito.mock(UserDAOImpl.class);

        // Inizializza il servizio
        registrazioneService = new AuthenticationService();
    }

    @Test
    void testSuccessfulRegistration() throws Exception {
        // Simula che l'email non sia già registrata
        Mockito.when(userDAOMock.getUserByEmail("test@example.com")).thenReturn(null);

        // Crea un oggetto utente
        UserBean user = new UserBean("Mario", "test@example.com", "ValidPassword123!", "User");

        // Esegui la registrazione
        boolean result = registrazioneService.registerUser(user);

        // Verifica che la registrazione sia avvenuta con successo
        assertTrue(result, "La registrazione dovrebbe avere successo");
        assertNotNull(user, "L'oggetto utente non dovrebbe essere nullo");
        assertEquals("Mario", user.getUsername(), "Il nome utente registrato non corrisponde");
        assertEquals("test@example.com", user.getEmail(), "L'email registrata non corrisponde");
        assertEquals("ValidPassword123!", user.getPassword(), "La password registrata non corrisponde");
        assertEquals("User", user.getRole(), "Il ruolo registrato non corrisponde");

        // Verifica che i metodi DAO siano stati chiamati correttamente
        //verify(userDAOMock, times(1)).getUserByEmail("test@example.com");
    }
}
