package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.account.UserId;

public interface StartCodeforcesLinkUseCase {

    String startLink(UserId userId);
}