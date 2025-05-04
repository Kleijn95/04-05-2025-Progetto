package it.epicode.__05_2025_Progetto.eventi;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventoDTO {
    private String titolo;
    private LocalDate data;
    private String luogo;

    public EventoDTO(String titolo, LocalDate data, String luogo) {
        this.titolo = titolo;
        this.data = data;
        this.luogo = luogo;
    }}
