package com.popx.presentazione;

import com.popx.modello.ProdottoBean;
import com.popx.persistenza.CarrelloDAOImpl;
import com.popx.persistenza.DataSourceSingleton;
import com.popx.persistenza.ProdottoDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UpdateCartTest {
    private ProdottoDAOImpl prodottoDAOMock;
    private DataSource mockDataSource;
    private CarrelloDAOImpl carrelloDAOMock;
    private HttpSession sessionMock;

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
        carrelloDAOMock = mock(CarrelloDAOImpl.class);
        sessionMock = mock(HttpSession.class);

    }

    @Test
    void testUpdateCartQuantitySuccess() {
        // Crea un prodotto di esempio
        ProdottoBean prodotto = new ProdottoBean(
                "P001",
                "Test Product",
                "A test product description",
                19.99,
                10, // Quantità disponibile in stock
                "TestBrand",
                new byte[]{0x12, 0x34, 0x56},
                "TestFigure"
        );

        // Simula il carrello nella sessione con il prodotto già aggiunto
        List<ProdottoBean> cart = new ArrayList<>();
        prodotto.setQty(2); // Quantità iniziale nel carrello
        cart.add(prodotto);
        when(sessionMock.getAttribute("cart")).thenReturn(cart);

        // Nuova quantità da aggiornare
        int newQuantity = 5;

        // Simula l'aggiornamento della quantità
        if (newQuantity <= prodotto.getPiecesInStock()) {
            prodotto.setQty(newQuantity);
            System.out.println("Quantità aggiornata con successo: " + prodotto.getQty());
        } else {
            System.out.println("Errore: Quantità richiesta eccede la disponibilità in stock.");
        }

        // Verifica che la quantità sia stata aggiornata con successo
        assertEquals(newQuantity, prodotto.getQty(), "La quantità del prodotto nel carrello dovrebbe essere aggiornata correttamente");
        assertEquals(1, cart.size(), "Il carrello dovrebbe contenere ancora un solo prodotto");

    }

    @Test
    void testRemoveUnavailableProductFromCart() {
        // Crea un prodotto inizialmente disponibile
        ProdottoBean prodotto = new ProdottoBean(
                "P001",
                "Test Product",
                "A test product description",
                19.99,
                10, // Quantità disponibile in stock inizialmente
                "TestBrand",
                new byte[]{0x12, 0x34, 0x56},
                "TestFigure"
        );

        // Simula il carrello nella sessione con il prodotto già aggiunto
        List<ProdottoBean> cart = new ArrayList<>();
        cart.add(prodotto);
        when(sessionMock.getAttribute("cart")).thenReturn(cart);

        // Simula che il prodotto non sia più disponibile (piecesInStock = 0)
        prodotto.setPiecesInStock(0);
        when(prodottoDAOMock.getProdottoById("P001")).thenReturn(prodotto);

        // Rimuovi il prodotto dal carrello se non è più disponibile
        cart.removeIf(p -> p.getId().equals(prodotto.getId()) && prodotto.getPiecesInStock() == 0);

        // Mostra un messaggio per indicare che il prodotto è stato rimosso
        if (!cart.contains(prodotto)) {
            System.out.println("Il prodotto '" + prodotto.getName() + "' è stato rimosso dal carrello perché non più disponibile.");
        }

        // Verifica che il prodotto sia stato rimosso dal carrello
        assertFalse(cart.contains(prodotto), "Il prodotto non disponibile dovrebbe essere rimosso dal carrello");
        assertEquals(0, cart.size(), "Il carrello dovrebbe essere vuoto dopo la rimozione del prodotto non disponibile");


    }

}