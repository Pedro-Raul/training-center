package co.ufps.trainingcenter.api.security;

import co.ufps.trainingcenter.api.error.ErrorResponse;
import co.ufps.trainingcenter.application.port.in.ResolveAuthenticatedUserUseCase;
import co.ufps.trainingcenter.domain.account.UserId;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Principal;
import java.util.Optional;

@Provider
@AuthenticatedUser
@Priority(Priorities.AUTHENTICATION + 10)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ResolveAuthenticatedUserUseCase resolveUser;

    @Inject
    public AuthenticationFilter(ResolveAuthenticatedUserUseCase resolveUser) {
        this.resolveUser = resolveUser;
    }

    @Override
    public void filter(ContainerRequestContext context) {
        String header = context.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            reject(context);
            return;
        }

        Optional<UserId> user = resolveUser.authenticate(header.substring(BEARER_PREFIX.length()));
        if (user.isEmpty()) {
            reject(context);
            return;
        }

        final String userId = user.get().value();
        final SecurityContext previous = context.getSecurityContext();
        context.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> userId;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return previous != null && previous.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }

    private void reject(ContainerRequestContext context) {
        context.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("UNAUTHORIZED", "Autenticación requerida",
                        context.getUriInfo().getPath()))
                .build());
    }
}