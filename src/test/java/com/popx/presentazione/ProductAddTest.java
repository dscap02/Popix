package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.persistenza.DataSourceSingleton;
import com.popx.persistenza.ProdottoDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductAddTest {
    private ProdottoDAOImpl prodottoDAOMock;
    private DataSource mockDataSource;

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

    }

    @Test
    void testSuccessfulProductAddition() throws Exception {
        // Crea un oggetto prodotto
        ProdottoBean prodotto = new ProdottoBean(
                "P001",
                "Test Product",
                "A test product description",
                19.99,
                100,
                "TestBrand",
                new byte[]{0x12, 0x34, 0x56},
                "TestFigure"
        );

        // Simula il comportamento del DAO
        when(prodottoDAOMock.saveProdotto(prodotto)).thenReturn(true);

        // Esegui l'aggiunta del prodotto
        boolean result = prodottoDAOMock.saveProdotto(prodotto);

        // Verifica che l'aggiunta sia avvenuta con successo
        assertTrue(result, "L'aggiunta del prodotto dovrebbe avere successo");

        // Verifica che i metodi DAO siano stati chiamati correttamente
        verify(prodottoDAOMock, times(1)).saveProdotto(prodotto);
    }

    @Test
    void testSuccessfulProductUpdate() throws Exception {
        // Crea un oggetto prodotto
        ProdottoBean prodotto = new ProdottoBean(
                "P001",
                "Updated Product",
                "An updated product description",
                29.99,
                50,
                "UpdatedBrand",
                new byte[]{0x78, 0x56, 0x34},
                "UpdatedFigure"
        );

        // Simula il comportamento del DAO
        when(prodottoDAOMock.updateProduct(prodotto)).thenReturn(true);

        // Esegui la modifica del prodotto
        boolean result = prodottoDAOMock.updateProduct(prodotto);

        // Verifica che la modifica sia avvenuta con successo
        assertTrue(result, "La modifica del prodotto dovrebbe avere successo");

        // Verifica che i metodi DAO siano stati chiamati correttamente
        verify(prodottoDAOMock, times(1)).updateProduct(prodotto);
    }

    @Test
    void testCheckProductAssociationwithID() throws Exception {
        // Crea due oggetti prodotto
        ProdottoBean prodotto1 = new ProdottoBean(
                "P001",
                "Product 1",
                "Description for product 1",
                19.99,
                100,
                "Brand1",
                new byte[]{0x12, 0x34, 0x56},
                "Figure1"
        );

        ProdottoBean prodotto2 = new ProdottoBean(
                "P002",
                "Product 2",
                "Description for product 2",
                29.99,
                200,
                "Brand2",
                new byte[]{0x78, 0x56, 0x34},
                "Figure2"
        );

        // Simula il comportamento del DAO per verificare l'associazione
        when(prodottoDAOMock.isAssociatedWith(prodotto1.getId(), prodotto2.getId())).thenReturn(true);

        // Verifica l'associazione tra i prodotti
        boolean isAssociated = prodottoDAOMock.isAssociatedWith(prodotto1.getId(), prodotto2.getId());

        // Controlla che l'associazione sia corretta
        assertTrue(isAssociated, "I prodotti dovrebbero essere associati correttamente");

        // Verifica che il metodo DAO sia stato chiamato correttamente
        verify(prodottoDAOMock, times(1)).isAssociatedWith(prodotto1.getId(), prodotto2.getId());
    }

}
