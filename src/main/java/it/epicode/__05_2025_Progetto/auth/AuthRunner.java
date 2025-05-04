package it.epicode.__05_2025_Progetto.auth;

import com.github.javafaker.Faker;
import it.epicode.__05_2025_Progetto.eventi.Evento;
import it.epicode.__05_2025_Progetto.eventi.EventoRepository;
import it.epicode.__05_2025_Progetto.eventi.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Faker faker;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        AppUser admin;
        Optional<AppUser> adminUser = appUserService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            admin = appUserService.registerUser("admin", "adminpwd", Set.of(Role.ROLE_ADMIN));
        } else {
            admin = adminUser.get();
        }
        // creazione organizzatore
        AppUser organizzatore;
        Optional<AppUser> organizzatoreUser = appUserService.findByUsername("organizzatore");
        if (organizzatoreUser.isEmpty()) {
            organizzatore = appUserService.registerUser("organizzatore", "organizzatorepwd", Set.of(Role.ROLE_ORGANIZZATORE));
        } else {
            organizzatore = organizzatoreUser.get();
        }

        // Creazione utenti normali
        Optional<AppUser> normalUser = appUserService.findByUsername("user");
        if (normalUser.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                appUserService.registerUser(faker.name().username(), "userpwd", Set.of(Role.ROLE_UTENTE_NORMALE));
            }
        }

        // Creazione eventi
        for (int i = 0; i < 5; i++) {
            Evento evento = new Evento();
            evento.setTitolo(faker.book().title());
            evento.setDescrizione(faker.lorem().sentence());
            evento.setNumeroPostiDisponibili(100);
            evento.setLuogo(faker.address().city());
            evento.setOrganizzatore(organizzatore); // Ora è definito
            evento.setData(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)));

            eventoRepository.save(evento); // Assicurati che questo metodo esista
        }


    }}
