package com.VigiDrive.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.IdentityProviderRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties("keycloak")
@Slf4j
public class KeycloakConfig {

    private static final String ADMIN_USERNAME = "sofiia.kazantseva@faceit.com.ua";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String CRED_TYPE = "Password";

    private String serverUrl;

    private String realm;

    private String clientId;

    private String username;

    private String password;

    private String roleDriver;

    private String roleManager;

    private String roleAdmin;

    private String secret;

    private ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilderImpl();

    @Bean
    public Keycloak keycloak() {
        Keycloak keycloak = createKeycloakConnection(resteasyClientBuilder);

        RealmResource realmResource = keycloak.realm(realm);

        IdentityProvidersResource identityProvidersResource = realmResource.identityProviders();

        createKeycloakRealm(keycloak);

        createKeycloakClient(realmResource);

        createGoogleIdentityProvider(identityProvidersResource);

        createLocalIdentityProvider(identityProvidersResource);

        createRole(roleDriver, realmResource);

        createRole(roleManager, realmResource);

        createRole(roleAdmin, realmResource);

        return keycloak;
    }

    private Keycloak createKeycloakConnection(ResteasyClientBuilder resteasyClientBuilder) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(OAuth2Constants.PASSWORD)
                .realm("master")
                .clientId("admin-cli")
                .username(username)
                .password(password)
                .resteasyClient(resteasyClientBuilder.connectionPoolSize(10).build())
                .build();
    }

    private void createKeycloakRealm(Keycloak keycloak) {
        RealmRepresentation newRealm = new RealmRepresentation();
        newRealm.setId(realm);
        newRealm.setRealm(realm);
        newRealm.setEnabled(true);

        try {
            keycloak.realms().create(newRealm);
        } catch (Exception e) {
            log.error("Something went wrong when creating the realm : {}", e.getMessage());
        }
    }

    private void createKeycloakClient(RealmResource realmResource) {
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(clientId);
        clientRepresentation.setName(clientId);
        clientRepresentation.setRootUrl("http://localhost:8080");
        clientRepresentation.setRedirectUris(List.of("http://localhost:8080/login/oauth2/code/vigi-driver"));
        clientRepresentation.setWebOrigins(List.of("*"));
        clientRepresentation.setStandardFlowEnabled(Boolean.TRUE);
        clientRepresentation.setPublicClient(Boolean.TRUE);
        clientRepresentation.setDirectAccessGrantsEnabled(Boolean.TRUE);

        try {
            realmResource.clients().create(clientRepresentation);
        } catch (Exception e) {
            log.error("Something went wrong when creating the keycloak client : {}", e.getMessage());
        }

    }


    private void createGoogleIdentityProvider(IdentityProvidersResource identityProvidersResource) {
        IdentityProviderRepresentation googleProvider = new IdentityProviderRepresentation();
        googleProvider.setAlias("google");
        googleProvider.setProviderId("google");
        googleProvider.setEnabled(true);
        googleProvider.setTrustEmail(true);
        googleProvider.setUpdateProfileFirstLoginMode("on");

        googleProvider.setConfig(
                Map.of(
                        "clientId", "108088983721-94ri8tnmtprcj88c1cpd7gng1okh4mfk.apps.googleusercontent.com",
                        "clientSecret", "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",
                        "defaultScope", "openid email profile",
                        "authorizationUrl", "https://accounts.google.com/o/oauth2/auth",
                        "tokenUrl", "https://accounts.google.com/o/oauth2/token",
                        "userInfoUrl", "https://www.googleapis.com/oauth2/v3/userinfo",
                        "logoutUrl", "https://accounts.google.com/o/oauth2/logout"
                )
        );

        try {
            identityProvidersResource.create(googleProvider);
        } catch (Exception e) {
            log.error("Something went wrong when creating the keycloak client : {}", e.getMessage());
        }
    }

    private void createLocalIdentityProvider(IdentityProvidersResource identityProvidersResource) {
        IdentityProviderRepresentation localProvider = new IdentityProviderRepresentation();
        localProvider.setAlias("local");
        localProvider.setProviderId("local");
        localProvider.setEnabled(true);
        localProvider.setAuthenticateByDefault(true);

        Map<String, String> config = new HashMap<>();
        config.put("editUsernameAllowed", "true");
        config.put("hideOnLoginPage", "false");

        localProvider.setConfig(config);

        try {
            identityProvidersResource.create(localProvider);
        } catch (Exception e) {
            log.error("Something went wrong when creating the keycloak client : {}", e.getMessage());
        }
    }

    private void createRole(String roleName, RealmResource realmResource) {
        try {
            RoleRepresentation roleRepresentation = new RoleRepresentation();
            roleRepresentation.setId(roleName);
            roleRepresentation.setName(roleName);
            roleRepresentation.setClientRole(Boolean.TRUE);
            realmResource.roles().create(roleRepresentation);
        } catch (Exception e) {
            log.error("Something went wrong when creating the realm role : {}", e.getMessage());
        }
    }

}