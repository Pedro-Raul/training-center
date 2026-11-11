package co.ufps.trainingcenter.infrastructure.persistence.exercise;

import co.ufps.trainingcenter.application.port.out.ExerciseRepository;
import co.ufps.trainingcenter.domain.exercise.Difficulty;
import co.ufps.trainingcenter.domain.exercise.Exercise;
import co.ufps.trainingcenter.domain.exercise.ExerciseId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador del repositorio de ejercicios usando JPA/Hibernate.
 */
@ApplicationScoped
public class ExerciseRepositoryJpa implements ExerciseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Exercise save(Exercise exercise) {
        entityManager.merge(toEntity(exercise));
        return exercise;
    }

    @Override
    public Optional<Exercise> findById(ExerciseId id) {
        ExerciseJpa entity = entityManager.find(ExerciseJpa.class, id.value());
        return Optional.ofNullable(entity).map(this::toDomain);
    }

    @Override
    public List<Exercise> findByDifficulty(Difficulty difficulty) {
        return entityManager
                .createQuery(
                        "SELECT e FROM ExerciseJpa e WHERE e.difficulty = :difficulty ORDER BY e.difficultyRating ASC",
                        ExerciseJpa.class)
                .setParameter("difficulty", difficulty)
                .getResultList().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Exercise> findByTopic(String topic) {
        return entityManager
                .createQuery(
                        "SELECT e FROM ExerciseJpa e WHERE LOWER(e.topic) LIKE LOWER(:topic) ORDER BY e.difficultyRating ASC",
                        ExerciseJpa.class)
                .setParameter("topic", "%" + topic + "%")
                .getResultList().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // â”€â”€ Mapeo: Domain â†’ JPA â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private ExerciseJpa toEntity(Exercise exercise) {
        ExerciseJpa entity = new ExerciseJpa();
        entity.setExerciseId(exercise.getId().value());
        entity.setTitle(exercise.getTitle());
        entity.setTopic(exercise.getTopic());
        entity.setDifficulty(exercise.getDifficulty());
        entity.setDifficultyRating(exercise.getDifficultyRating());
        entity.setPlatform(exercise.getPlatform());
        entity.setExternalUrl(exercise.getExternalUrl());
        entity.setCreatedAt(exercise.getCreatedAt());
        return entity;
    }

    // â”€â”€ Mapeo: JPA â†’ Domain â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private Exercise toDomain(ExerciseJpa entity) {
        return Exercise.restore(
                ExerciseId.of(entity.getExerciseId()),
                entity.getTitle(),
                entity.getTopic(),
                entity.getDifficulty(),
                entity.getDifficultyRating(),
                entity.getPlatform(),
                entity.getExternalUrl(),
                entity.getCreatedAt()
        );
    }
}
