package it.epicode.__05_2025_Progetto.eventi;

import it.epicode.__05_2025_Progetto.auth.AppUser;
import it.epicode.__05_2025_Progetto.auth.AppUserRepository;
import it.epicode.__05_2025_Progetto.auth.Role;
import it.epicode.__05_2025_Progetto.common.CommonResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@Validated
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private AppUserRepository appUserRepository;  // Usa il repository di AppUser


    public boolean isOrganizzatore(Long idEvento, String username) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
        return evento.getOrganizzatore().getUsername().equals(username);
    }


    public void aggiungiPartecipante(Long idEvento, Long idAppUser) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        AppUser appUser = appUserRepository.findById(idAppUser)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        // Verifica che l'utente non sia già registrato come partecipante
        if (evento.getPartecipanti().contains(appUser)) {
            throw new IllegalArgumentException("L'utente è già registrato a questo evento.");
        }

        if (evento.getNumeroPostiDisponibili() <= 0) {
            throw new IllegalArgumentException("Non ci sono più posti disponibili per questo evento.");
        }

        evento.getPartecipanti().add(appUser);  // Aggiungi AppUser come partecipante
        evento.setNumeroPostiDisponibili(evento.getNumeroPostiDisponibili() - 1);
        eventoRepository.save(evento);
    }

    public void rimuoviPartecipante(Long eventoId, Long utenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        AppUser utente = appUserRepository.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if (!evento.getPartecipanti().contains(utente)) {
            throw new IllegalStateException("L'utente non partecipa a questo evento");
        }

        evento.getPartecipanti().remove(utente);
        evento.setNumeroPostiDisponibili(evento.getNumeroPostiDisponibili() + 1);
        eventoRepository.save(evento);
    }




    // Find by id
    public EventoResponse findById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));
        return fromEntity(evento);
    }

    // Find All
    public Page<EventoResponse> findAll(Pageable pageable) {
        return eventoRepository.findAll(pageable)
                .map(this::fromEntity);
    }

    // Update
    public EventoResponse update(Long idEvento, EventoRequest request, Long idAppUser) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        if (!evento.getOrganizzatore().getId().equals(idAppUser)) {
            throw new IllegalStateException("Puoi modificare solo eventi creati da te");
        }

        BeanUtils.copyProperties(request, evento, "id", "organizzatore");
        eventoRepository.save(evento);

        return fromEntity(evento);
    }

    // Create
    public CommonResponse save(@Valid EventoRequest request, Long idAppUser) {
        if (request.getData().isBefore(LocalDate.now())) {
            throw new IllegalStateException("La data dell'evento non può essere nel passato");
        }
        AppUser appUser = appUserRepository.findById(idAppUser)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        // Controllo sul ruolo
        if (!appUser.getRoles().contains(Role.ROLE_ORGANIZZATORE)) {
            throw new IllegalStateException("Solo un organizzatore può creare eventi");
        }



        Evento evento = new Evento();
        BeanUtils.copyProperties(request, evento);
        evento.setOrganizzatore(appUser);  // Usa AppUser come organizzatore

        eventoRepository.save(evento);
        return new CommonResponse(evento.getId());
    }

    // Delete
    public void delete(Long idEvento, Long idAppUser) {
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        if (!evento.getOrganizzatore().getId().equals(idAppUser)) {
            throw new IllegalStateException("Puoi eliminare solo eventi creati da te");
        }

        eventoRepository.delete(evento);
    }

    public EventoResponse fromEntity(Evento evento) {
        EventoResponse response = new EventoResponse();
        BeanUtils.copyProperties(evento, response);
        response.setCreatoreUsername(evento.getOrganizzatore().getUsername());
        return response;
    }

    public List<EventoResponse> fromEntity(List<Evento> eventi) {
        return eventi
                .stream()
                .map(this::fromEntity)
                .toList();
    }
}
