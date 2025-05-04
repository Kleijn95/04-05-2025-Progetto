package it.epicode.__05_2025_Progetto.eventi;

import it.epicode.__05_2025_Progetto.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eventi")

public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;
    @Column(nullable = false)
    private String titolo;
    @Column(nullable = false)
    private String descrizione;
    @Column(nullable = false)
    private String luogo;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private int numeroPostiDisponibili;
    @ManyToOne
    @JoinColumn(name = "organizzatore_id")
    private AppUser organizzatore;
    @ManyToMany
    @JoinTable(
            name = "evento_partecipanti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private Set<AppUser> partecipanti = new HashSet<>();



}