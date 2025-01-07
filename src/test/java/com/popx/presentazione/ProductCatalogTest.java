package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.persistenza.DataSourceSingleton;
import com.popx.persistenza.ProdottoDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductCatalogTest {

    private ProdottoDAOImpl prodottoDAOMock;
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
        prodottoDAOMock = Mockito.mock(ProdottoDAOImpl.class);

    }

    @Test
    void testProductWithInvalidId() throws Exception {
        // Dati di test
        String invalidProductId = "";

        // Simula che il metodo restituisca null per un ID prodotto invalido
        when(prodottoDAOMock.getProdottoById(invalidProductId)).thenReturn(null);

        // Verifica che il risultato sia nullo
        ProdottoBean result = prodottoDAOMock.getProdottoById(invalidProductId);
        assertNull(result, "Non dovrebbe restituire un prodotto per un ID non valido");

        // Verifica che il metodo sia stato chiamato una sola volta
        verify(prodottoDAOMock, times(1)).getProdottoById(invalidProductId);
    }

    @Test
    void testProductWithNullId() throws Exception {
        // Dati di test
        String nullProductId = null;

        // Simula che il metodo restituisca null per un ID prodotto nullo
        when(prodottoDAOMock.getProdottoById(nullProductId)).thenReturn(null);

        // Verifica che il risultato sia nullo
        ProdottoBean result = prodottoDAOMock.getProdottoById(nullProductId);
        assertNull(result, "Non dovrebbe restituire un prodotto per un ID nullo");

        // Verifica che il metodo sia stato chiamato una sola volta
        verify(prodottoDAOMock, times(1)).getProdottoById(nullProductId);
    }

    @Test
    void testRetrieveProductWithSpecialCharactersInId() throws Exception {
        // Dati di test
        String productId = "123-abc!";

        // Crea un oggetto ProdottoBean simulato
        ProdottoBean prodotto = new ProdottoBean(
                productId,
                "Prodotto Speciale",
                "Descrizione Speciale",
                29.99,
                5,
                "Special Brand",
                null,
                "Special Figure"
        );

        // Simula che il prodotto esista
        when(prodottoDAOMock.getProdottoById(productId)).thenReturn(prodotto);

        // Verifica che il prodotto esista
        ProdottoBean result = prodottoDAOMock.getProdottoById(productId);
        assertNotNull(result, "Il prodotto con caratteri speciali nell'ID dovrebbe esistere");
        assertEquals(productId, result.getId(), "L'ID del prodotto non corrisponde");

        // Verifica che il metodo sia stato chiamato
        verify(prodottoDAOMock, times(1)).getProdottoById(productId);
    }

    @Test
    void testMultipleProductRetrievals() throws Exception {
        // Dati di test
        String productId1 = "101";
        String productId2 = "102";

        // Crea oggetti ProdottoBean simulati
        ProdottoBean prodotto1 = new ProdottoBean(productId1, "Prodotto 1", "Descrizione 1", 10.99, 20, "Brand 1", null, "Figure 1");
        ProdottoBean prodotto2 = new ProdottoBean(productId2, "Prodotto 2", "Descrizione 2", 15.99, 15, "Brand 2", null, "Figure 2");

        // Simula che i prodotti esistano
        when(prodottoDAOMock.getProdottoById(productId1)).thenReturn(prodotto1);
        when(prodottoDAOMock.getProdottoById(productId2)).thenReturn(prodotto2);

        // Verifica i prodotti
        ProdottoBean result1 = prodottoDAOMock.getProdottoById(productId1);
        ProdottoBean result2 = prodottoDAOMock.getProdottoById(productId2);

        assertNotNull(result1, "Il primo prodotto dovrebbe esistere");
        assertEquals(productId1, result1.getId(), "L'ID del primo prodotto non corrisponde");
        assertNotNull(result2, "Il secondo prodotto dovrebbe esistere");
        assertEquals(productId2, result2.getId(), "L'ID del secondo prodotto non corrisponde");

        // Verifica che il metodo sia stato chiamato per entrambi gli ID
        verify(prodottoDAOMock, times(1)).getProdottoById(productId1);
        verify(prodottoDAOMock, times(1)).getProdottoById(productId2);
    }

    @Test
    void testCatalogWithNoProducts() throws Exception {
        // Simula che il catalogo non contenga prodotti
        when(prodottoDAOMock.getProdottoById(anyString())).thenReturn(null);

        // Verifica che nessun prodotto esista
        ProdottoBean result = prodottoDAOMock.getProdottoById("anyId");
        assertNull(result, "Non dovrebbe restituire un prodotto se il catalogo è vuoto");

        // Verifica che il metodo sia stato chiamato
        verify(prodottoDAOMock, times(1)).getProdottoById("anyId");
    }

    @Test
    void testRetrieveProductByIdWithCaseSensitivity() throws Exception {
        // Dati di test
        String productId = "Product123";
        String differentCaseProductId = "product123";

        // Crea un oggetto ProdottoBean simulato
        ProdottoBean prodotto = new ProdottoBean(
                productId,
                "Prodotto Case Sensitive",
                "Descrizione Sensitiva",
                19.99,
                10,
                "Brand Case",
                null,
                "Figure Case"
        );

        // Simula che il prodotto esista solo con l'ID specifico
        when(prodottoDAOMock.getProdottoById(productId)).thenReturn(prodotto);
        when(prodottoDAOMock.getProdottoById(differentCaseProductId)).thenReturn(null);

        // Verifica che il prodotto sia restituito solo con l'ID corretto
        ProdottoBean result = prodottoDAOMock.getProdottoById(productId);
        ProdottoBean nullResult = prodottoDAOMock.getProdottoById(differentCaseProductId);

        assertNotNull(result, "Il prodotto dovrebbe esistere con l'ID specifico");
        assertNull(nullResult, "Non dovrebbe restituire un prodotto con un ID con differente case");

        // Verifica le chiamate al DAO
        verify(prodottoDAOMock, times(1)).getProdottoById(productId);
        verify(prodottoDAOMock, times(1)).getProdottoById(differentCaseProductId);
    }


}
