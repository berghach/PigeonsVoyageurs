package com.example.PigeonsVoyageurs.organiser.service;

import com.example.PigeonsVoyageurs.organiser.Organiser;
import com.example.PigeonsVoyageurs.organiser.OrganiserDTO;
import com.example.PigeonsVoyageurs.organiser.OrganiserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganiserServiceImpl implements OrganiserService {
    private final OrganiserRepository organiserRepository;

    @Autowired
    public OrganiserServiceImpl(OrganiserRepository organiserRepository) {
        this.organiserRepository = organiserRepository;
    }

    @Override
    public Organiser register(OrganiserDTO organiserDTO) {

        String hashedPassword = BCrypt.hashpw(organiserDTO.getPassword(), BCrypt.gensalt());

        Organiser organiser = new Organiser(organiserDTO.getUserName(), hashedPassword, organiserDTO.getEmail());
        return organiserRepository.save(organiser);
    }

    @Override
    public Optional<Organiser> findByEmail(String email) {
        return organiserRepository.findByEmail(email);
    }

    @Override
    public Optional<String> login(String email, String password) {
        Optional<Organiser> organiserOpt = findByEmail(email);
        if (organiserOpt.isPresent()) {
            Organiser organiser = organiserOpt.get();
            // Vérification du mot de passe avec jBCrypt
            if (BCrypt.checkpw(password, organiser.getPassword())) {
                return Optional.of("Login successful");
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Organiser> findById(Long id) {
        return organiserRepository.findById(id);
    }

    @Override
    public List<Organiser> findAll() {
        return organiserRepository.findAll();
    }

    @Override
    public Organiser update(Long id, OrganiserDTO organiserDTO) {
        Organiser organiser = organiserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organiser not found"));
        organiser.setUserName(organiserDTO.getUserName());

        if (organiserDTO.getPassword() != null && !organiserDTO.getPassword().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(organiserDTO.getPassword(), BCrypt.gensalt());
            organiser.setPassword(hashedPassword);
        }

        organiser.setEmail(organiserDTO.getEmail());
        return organiserRepository.save(organiser);
    }

    @Override
    public void delete(Long id) {
        organiserRepository.deleteById(id);
    }
}