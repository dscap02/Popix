package com.popx.persistenza;


import com.popx.modello.InfoPersonaliBean;

public interface InfoPersonaliDAO {
    void addInfoPersonali(InfoPersonaliBean infoPersonali);
    InfoPersonaliBean getInfoPersonali(String clienteEmail);
}
