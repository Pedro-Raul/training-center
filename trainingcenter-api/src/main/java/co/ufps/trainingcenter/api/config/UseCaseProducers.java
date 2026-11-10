package co.ufps.trainingcenter.api.config;

import co.ufps.trainingcenter.application.port.in.ContestUseCase;
import co.ufps.trainingcenter.application.port.in.GetDailyActivityMapUseCase;
import co.ufps.trainingcenter.application.port.in.GetExercisesUseCase;
import co.ufps.trainingcenter.application.port.in.RegisterUserUseCase;
import co.ufps.trainingcenter.application.service.ContestService;
import co.ufps.trainingcenter.application.service.DailyActivityService;
import co.ufps.trainingcenter.application.service.ExerciseService;
import co.ufps.trainingcenter.application.service.RegisterUserService;
import co.ufps.trainingcenter.application.service.UserSubmissionService;
import co.ufps.trainingcenter.application.port.out.ContestRepository;
import co.ufps.trainingcenter.application.port.out.DailyActivityRepository;
import co.ufps.trainingcenter.application.port.out.ExerciseRepository;
import co.ufps.trainingcenter.application.port.out.PasswordHasher;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import co.ufps.trainingcenter.application.port.out.UserRepository;
import co.ufps.trainingcenter.application.port.out.UserSubmissionRepository;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class UseCaseProducers {

    @Inject
    private ContestRepository contestRepository;

    @Inject
    private DailyActivityRepository dailyActivityRepository;

    @Inject
    private UserProfileRepository userProfileRepository;

    @Inject
    private UserSubmissionRepository userSubmissionRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordHasher passwordHasher;

    @Inject
    private ExerciseRepository exerciseRepository;

    @Produces
    public ContestUseCase contestUseCase() {
        return new ContestService(contestRepository);
    }

    @Produces
    public GetDailyActivityMapUseCase dailyActivityMapUseCase() {
        return new DailyActivityService(
                dailyActivityRepository,
                userProfileRepository
        );
    }

    @Produces
    public UserSubmissionService recordSubmissionUseCase() {
        return new UserSubmissionService(
                userSubmissionRepository,
                userProfileRepository
        );
    }

    @Produces
    public RegisterUserUseCase registerUserUseCase() {
        return new RegisterUserService(userRepository, passwordHasher);
    }

    @Produces
    public GetExercisesUseCase getExercisesUseCase() {
        return new ExerciseService(exerciseRepository);
    }
}
