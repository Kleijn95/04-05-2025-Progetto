package it.epicode.__05_2025_Progetto.eventi;

import it.epicode.__05_2025_Progetto.auth.AppUser;
import it.epicode.__05_2025_Progetto.auth.AppUserService;
import it.epicode.__05_2025_Progetto.common.CommonResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private AppUserService appUserService;  // Servizio per gestire gli utenti (AppUser)

    // get all
    @GetMapping
    public Page<EventoResponse> findAll(@ParameterObject Pageable pageable) {
        return eventoService.findAll(pageable);
    }

    // get by id
    @GetMapping("/{id}")
    public EventoResponse findById(@PathVariable Long id) {
        return eventoService.findById(id);
    }

    // create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse save(@Valid @RequestBody EventoRequest request, @AuthenticationPrincipal AppUser appUser) {
        return eventoService.save(request, appUser.getId());  // Usa AppUser al posto di Utente
    }

    // update
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE') and @eventoService.isOrganizzatore(#id, authentication.name)")
    @PutMapping("/{id}")
    public EventoResponse update(@PathVariable Long id, @Valid @RequestBody EventoRequest request, @AuthenticationPrincipal AppUser appUser) {
        return eventoService.update(id, request, appUser.getId());  // Usa AppUser al posto di Utente
    }



    // delete
    @PreAuthorize("hasRole('ROLE_ORGANIZZATORE') and @eventoService.isOrganizzatore(#id, authentication.name)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal AppUser appUser) {
        eventoService.delete(id, appUser.getId());  // Usa AppUser al posto di Utente
    }


    @PreAuthorize("hasRole('ROLE_UTENTE_NORMALE')")
    @PutMapping("/{id}/partecipa")
    public void partecipa(@PathVariable Long id, @AuthenticationPrincipal AppUser appUser) {
        eventoService.aggiungiPartecipante(id, appUser.getId());
    }

    @PreAuthorize("hasRole('ROLE_UTENTE_NORMALE')")
    @DeleteMapping("/{id}/partecipa")
    public void rimuoviPartecipazione(@PathVariable Long id, @AuthenticationPrincipal AppUser appUser) {
        eventoService.rimuoviPartecipante(id, appUser.getId());
    }

}
