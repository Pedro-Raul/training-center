package co.ufps.trainingcenter.application.port.out;

public interface CodeforcesOidcClient {

    String buildAuthorizationUrl(String state, String nonce);

    CodeforcesIdentity exchangeCode(String code, String expectedNonce);
}
