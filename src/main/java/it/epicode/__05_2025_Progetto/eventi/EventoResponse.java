package it.epicode.__05_2025_Progetto.eventi;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoResponse {
    private Long id;
    private String titolo;
    private String descrizione;
    private String luogo;
    private LocalDate data;
    private int numeroPostiDisponibili;
    private String creatoreUsername;
}
