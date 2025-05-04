package it.epicode.__05_2025_Progetto.eventi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoRequest {
    private String titolo;
    private String descrizione;
    @NotBlank(message = "Il luogo non può essere non essere definito")
    private String luogo;
    @NotNull(message = "La data non può essere null")
    private LocalDate data;
    @NotNull(message = "Il numero di posti disponibili non può essere null")
    private int numeroPostiDisponibili;
}
