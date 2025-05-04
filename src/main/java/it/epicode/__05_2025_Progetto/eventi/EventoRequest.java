package it.epicode.__05_2025_Progetto.eventi;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "Il titolo non può essere vuoto")
    private String titolo;

    @NotBlank(message = "La descrizione non può essere vuota")
    private String descrizione;

    @NotBlank(message = "Il luogo non può essere vuoto")
    private String luogo;

    @NotNull(message = "La data non può essere null")
    @Future(message = "La data deve essere nel futuro")
    private LocalDate data;

    @Min(value = 1, message = "Deve esserci almeno un posto disponibile")
    private int numeroPostiDisponibili;
}
