package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.domain.account.UserId;

public interface StartCodeforcesLinkUseCase {

    String startLink(UserId userId);
}