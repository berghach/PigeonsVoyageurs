package com.example.PigeonsVoyageurs.competition;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompetitionRepository extends MongoRepository<Competition, Long> {
}
