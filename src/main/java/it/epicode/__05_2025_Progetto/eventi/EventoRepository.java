package it.epicode.__05_2025_Progetto.eventi;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByPartecipanti_Id(Long utenteId);

}