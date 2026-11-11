package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;

public interface RegisterUserUseCase {

    UserId register(RegisterUserCommand command);
}