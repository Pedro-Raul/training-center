package co.ufps.trainingcenter.application.port.in;

import co.ufps.trainingcenter.account.ExternalAccount;

public interface CompleteCodeforcesLinkUseCase {

    ExternalAccount completeLink(String code, String state);
}