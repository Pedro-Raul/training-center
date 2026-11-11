CREATE TABLE oidc_state (
    state         VARCHAR(255) PRIMARY KEY,
    nonce         VARCHAR(255),
    code_verifier VARCHAR(255),
    expires_at    TIMESTAMP NOT NULL
);
