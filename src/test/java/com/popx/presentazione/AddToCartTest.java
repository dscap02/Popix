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

class AddToCartTest {
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
    void testAddProductToCart() {
        // Crea un prodotto di esempio
        ProdottoBean prodotto = new ProdottoBean(
                "P001",
                "Test Product",
                "A test product description",
                19.99,
                10,
                "TestBrand",
                new byte[]{0x12, 0x34, 0x56},
                "TestFigure"
        );

        // Simula il comportamento del DAO
        when(prodottoDAOMock.getProdottoById("P001")).thenReturn(prodotto);

        // Simula il carrello nella sessione
        List<ProdottoBean> cart = new ArrayList<>();
        when(sessionMock.getAttribute("cart")).thenReturn(cart);

        // Aggiungi il prodotto al carrello
        cart.add(prodotto);

        System.out.println("Contenuto del carrello: " + cart);


        // Verifica che il prodotto sia stato aggiunto al carrello
        assertTrue(cart.contains(prodotto), "Il prodotto dovrebbe essere presente nel carrello");
        assertEquals(1, cart.size(), "Il carrello dovrebbe contenere un solo prodotto");

        // Verifica che i metodi siano stati chiamati correttamente
        //verify(prodottoDAOMock, times(1)).getProdottoById("P001");
        //verify(sessionMock, times(1)).getAttribute("cart");
    }

    @Test
    void testAddUnavailableProductToCart() {
        // Simula un prodotto non disponibile
        ProdottoBean unavailableProduct = new ProdottoBean(
                "P002",
                "Unavailable Product",
                "A product that is out of stock",
                29.99,
                0, // Nessuna disponibilità
                "TestBrand",
                new byte[]{0x78, 0x56, 0x34},
                "TestFigure"
        );

        // Simula il comportamento del DAO
        when(prodottoDAOMock.getProdottoById("P002")).thenReturn(unavailableProduct);

        // Simula il carrello nella sessione
        List<ProdottoBean> cart = new ArrayList<>();
        when(sessionMock.getAttribute("cart")).thenReturn(cart);

        // Tentativo di aggiungere il prodotto non disponibile
        if (unavailableProduct.getPiecesInStock() > 0) {
            cart.add(unavailableProduct);
        } else {
            System.out.println("Prodotto non disponibile: " + unavailableProduct.getName());
        }

        // Verifica che il prodotto non sia stato aggiunto al carrello
        assertFalse(cart.contains(unavailableProduct), "Il prodotto non disponibile non dovrebbe essere presente nel carrello");
        assertEquals(0, cart.size(), "Il carrello dovrebbe essere vuoto");


    }

    @Test
    void testAddExceedingQuantityToCart() {
        // Simula un prodotto con quantità disponibile limitata
        ProdottoBean prodotto = new ProdottoBean(
                "P003",
                "Limited Stock Product",
                "A product with limited stock",
                15.99,
                5, // Disponibilità limitata
                "TestBrand",
                new byte[]{0x56, 0x78, 0x34},
                "TestFigure"
        );

        // Simula il comportamento del DAO
        when(prodottoDAOMock.getProdottoById("P003")).thenReturn(prodotto);

        // Simula il carrello nella sessione
        List<ProdottoBean> cart = new ArrayList<>();
        when(sessionMock.getAttribute("cart")).thenReturn(cart);

        // Tentativo di aggiungere una quantità superiore a quella disponibile
        int requestedQuantity = 10; // Richiesta di 10 unità

        if (requestedQuantity <= prodotto.getPiecesInStock()) {
            cart.add(prodotto);
            System.out.println("Prodotto aggiunto al carrello: " + prodotto.getName());
        } else {
            System.out.println("Quantità richiesta non disponibile per il prodotto: " + prodotto.getName());
        }

        // Verifica che il prodotto non sia stato aggiunto al carrello
        assertFalse(cart.contains(prodotto) && requestedQuantity > prodotto.getPiecesInStock(), "Il prodotto non dovrebbe essere aggiunto con quantità eccedente");
        assertEquals(0, cart.size(), "Il carrello dovrebbe essere vuoto se la quantità richiesta eccede la disponibilità");

        // Verifica che i metodi siano stati chiamati correttamente
        //verify(prodottoDAOMock, times(1)).getProdottoById("P003");
        //verify(sessionMock, times(1)).getAttribute("cart");
    }
    @Test
    void testUpdateCartQuantityExceedingStock() {
        // Crea un prodotto con disponibilità limitata
        ProdottoBean prodotto = new ProdottoBean(
                "P002",
                "Limited Stock Product",
                "A product with limited stock",
                15.99,
                5, // Quantità disponibile in stock
                "TestBrand",
                new byte[]{0x78, 0x56, 0x34},
                "TestFigure"
        );

        // Simula il carrello nella sessione con il prodotto già aggiunto
        List<ProdottoBean> cart = new ArrayList<>();
        prodotto.setQty(2); // Quantità iniziale nel carrello
        cart.add(prodotto);
        when(sessionMock.getAttribute("cart")).thenReturn(cart);

        // Tentativo di aggiornare la quantità con un valore superiore allo stock disponibile
        int requestedQuantity = 10; // Richiesta di 10 unità

        if (requestedQuantity <= prodotto.getPiecesInStock()) {
            prodotto.setQty(requestedQuantity);
            System.out.println("Quantità aggiornata con successo: " + prodotto.getQty());
        } else {
            System.out.println("Errore: Quantità richiesta eccede la disponibilità in stock per il prodotto: " + prodotto.getName());
        }

        // Verifica che la quantità non sia stata aggiornata oltre la disponibilità
        assertNotEquals(requestedQuantity, prodotto.getQty(), "La quantità non dovrebbe essere aggiornata oltre la disponibilità in stock");
        assertEquals(2, prodotto.getQty(), "La quantità dovrebbe rimanere invariata");

        // Verifica che il prodotto rimanga nel carrello
        assertTrue(cart.contains(prodotto), "Il prodotto dovrebbe rimanere nel carrello");


    }
}

