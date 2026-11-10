package co.ufps.trainingcenter.api.config;

import co.ufps.trainingcenter.application.port.out.DailyActivityRepository;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import co.ufps.trainingcenter.application.service.DailyActivityService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import co.ufps.trainingcenter.application.port.out.UserSubmissionRepository;
import co.ufps.trainingcenter.application.service.UserSubmissionService;
import co.ufps.trainingcenter.application.port.out.UserSubmissionRepository;
import co.ufps.trainingcenter.application.service.UserSubmissionService;

@ApplicationScoped
public class UseCaseProducers {

    @Produces
    @Singleton
    public DailyActivityService dailyActivityService(
            DailyActivityRepository dailyActivityRepository,
            UserProfileRepository userProfileRepository
    ) {
        return new DailyActivityService(dailyActivityRepository, userProfileRepository);
    }

    @Produces
    @Singleton
    public UserSubmissionService userSubmissionService(UserSubmissionRepository userSubmissionRepository) {
        return new UserSubmissionService(userSubmissionRepository);
    }

}