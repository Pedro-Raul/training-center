package co.ufps.trainingcenter.domain.exercise;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException() {
        super("El ejercicio no existe.");
    }
}