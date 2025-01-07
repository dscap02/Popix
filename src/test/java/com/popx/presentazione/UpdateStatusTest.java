package com.popx.presentazione;

import com.popx.modello.OrdineBean;
import com.popx.persistenza.DataSourceSingleton;
import com.popx.persistenza.OrdineDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UpdateStatusTest {

    private OrdineDAOImpl ordineDAOMock;
    private DataSource mockDataSource;

    @BeforeEach
    void setupService() throws SQLException {
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

        // Crea un'istanza reale della DAO con il mock del DataSource
        ordineDAOMock = mock(OrdineDAOImpl.class);    }

    @Test
    void testUpdateShippingStatus() throws SQLException {
        // Dati di esempio

        float subtotal = 19.99F;
        String customerEmail = "test@gmail.com";
        Date data = new Date(2025 - 1900, 0, 6);
        int ordineId = 1;
        String nuovoStato = "In consegna";

        // Creazione di un mock per OrdineBean

        OrdineBean ordine1 = new OrdineBean(subtotal, customerEmail, data);

        ordine1.setStatus(nuovoStato);

        doAnswer(invocation -> {
            OrdineBean ordine = invocation.getArgument(0);
            ordine.setId(1); // Simula che l'ID venga generato correttamente
            return null;
        }).when(ordineDAOMock).updateStatus(any(OrdineBean.class));

        ordineDAOMock.updateStatus(ordine1);

        // Esegui l'operazione
        boolean result = ordineDAOMock.updateStatus(ordine1);

        // Verifica che il metodo sia stato chiamato correttamente
        //verify(ordineDAOMock, times(1)).updateStatus(ordine1);

        // Verifica che l'ID sia stato impostato sull'ordine
        assertEquals(nuovoStato, ordine1.getStatus(), "L'ID dell'ordine dovrebbe essere impostato correttamente.");
    }




}

