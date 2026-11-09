package co.ufps.trainingcenter.api.config;

import co.ufps.trainingcenter.application.port.out.DailyActivityRepository;
import co.ufps.trainingcenter.application.port.out.UserProfileRepository;
import co.ufps.trainingcenter.application.service.DailyActivityService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

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
}