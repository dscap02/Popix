package com.popx.persistenza;

import com.popx.modello.MetodoPagamentoBean;

public interface MetodoPagamentoDAO {
    void addMetodoPagamento(MetodoPagamentoBean metodoPagamento);
    MetodoPagamentoBean getMetodoPagamento(String id);
}