package co.ufps.trainingcenter.domain.exercise;

public class ExerciseAccessDeniedException extends RuntimeException {
    public ExerciseAccessDeniedException() {
        super("Solo el creador puede eliminar este ejercicio.");
    }
}