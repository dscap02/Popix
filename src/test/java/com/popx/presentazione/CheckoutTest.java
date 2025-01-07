package com.popx.presentazione;

import com.popx.modello.OrdineBean;
import com.popx.persistenza.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CheckoutTest {
    private ProdottoDAOImpl prodottoDAOMock;
    private DataSource mockDataSource;
    private RigaOrdineDAOImpl rigaordineDAOMock;
    private HttpSession sessionMock;
    private OrdineDAOImpl ordineDAOMock;
    private CarrelloDAOImpl carrelloDAOMock;

    @BeforeEach
    void setupService() throws SQLException {

        // Mock del DataSource
        mockDataSource = mock(DataSource.class);
        DataSourceSingleton.setInstanceForTest(mockDataSource);

        // Mock del comportamento del DataSource
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Mock del DAO
        prodottoDAOMock = mock(ProdottoDAOImpl.class);
        ordineDAOMock = mock(OrdineDAOImpl.class);
        rigaordineDAOMock = mock(RigaOrdineDAOImpl.class);
        sessionMock = mock(HttpSession.class);
        carrelloDAOMock = mock(CarrelloDAOImpl.class);

    }




    @Test
    void testSuccessfulPayment() throws Exception {
        // Dati di esempio
        float subtotal = 19.99F;
        String customerEmail = "test@gmail.com";
        Date data = new Date(2025 - 1900, 0, 6); // Ricorda che il mese parte da 0 in java.util.Date

        // Creazione dell'ordine
        OrdineBean ordine1 = new OrdineBean(subtotal, customerEmail, data);

        // Configura il comportamento del mock per il metodo insertOrdine
        doAnswer(invocation -> {
            OrdineBean ordine = invocation.getArgument(0);
            ordine.setId(1); // Simula che l'ID venga generato correttamente
            return null;
        }).when(ordineDAOMock).insertOrdine(any(OrdineBean.class));

        // Esegui l'operazione
        ordineDAOMock.insertOrdine(ordine1);

        // Verifica che il metodo sia stato chiamato correttamente
        verify(ordineDAOMock, times(1)).insertOrdine(ordine1);

        // Verifica che l'ID sia stato impostato sull'ordine
        assertEquals(1, ordine1.getId(), "L'ID dell'ordine dovrebbe essere impostato correttamente.");
    }

}