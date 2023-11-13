package com.VigiDrive.config;

import jakarta.ws.rs.core.Response;
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
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
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

    private String roleUser;

    private String roleAdmin;

    private String secret;

    private static UserRepresentation getUserRepresentation() {
        CredentialRepresentation adminCredentials = new CredentialRepresentation();
        adminCredentials.setType(CRED_TYPE);
        adminCredentials.setValue(ADMIN_PASSWORD);

        UserRepresentation admin = new UserRepresentation();
        admin.setUsername(ADMIN_USERNAME);
        admin.setEmail(ADMIN_USERNAME);
        admin.setFirstName("Harry");
        admin.setLastName("Potter");
        admin.setEmailVerified(Boolean.TRUE);
        admin.setCredentials(List.of(adminCredentials));
        admin.setEnabled(Boolean.TRUE);
        return admin;
    }

    @Bean
    public Keycloak keycloak() {
        ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilderImpl();

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(OAuth2Constants.PASSWORD)
                .realm("master")
                .clientId("admin-cli")
                .username(username)
                .password(password)
                .resteasyClient(resteasyClientBuilder.connectionPoolSize(10).build())
                .build();

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        RealmRepresentation newRealm = new RealmRepresentation();
        newRealm.setId(realm);
        newRealm.setRealm(realm);
        newRealm.setEnabled(true);

        try {
            keycloak.realms().create(newRealm);
        } catch (Exception e) {
            log.error("Something went wrong when creating the realm : {}", e.getMessage());
        }


        IdentityProvidersResource identityProvidersResource = realmResource.identityProviders();

        IdentityProviderRepresentation googleProvider = new IdentityProviderRepresentation();
        googleProvider.setAlias("google");
        googleProvider.setProviderId("google");
        googleProvider.setEnabled(true);
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


        identityProvidersResource.create(googleProvider);

        IdentityProviderRepresentation localProvider = new IdentityProviderRepresentation();
        localProvider.setAlias("local");
        localProvider.setProviderId("local");
        localProvider.setEnabled(true);
        localProvider.setAuthenticateByDefault(true);

        Map<String, String> config = new HashMap<>();
        config.put("editUsernameAllowed", "true");
        config.put("hideOnLoginPage", "false");

        localProvider.setConfig(config);

        identityProvidersResource.create(localProvider);

        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(clientId);
        clientRepresentation.setName(clientId);
        clientRepresentation.setRootUrl("http://localhost:8080");
        clientRepresentation.setRedirectUris(List.of("http://localhost:8085/realms/myapp/broker/google/endpoint"));
        clientRepresentation.setWebOrigins(List.of("*"));
        clientRepresentation.setStandardFlowEnabled(Boolean.TRUE);
        clientRepresentation.setPublicClient(Boolean.TRUE);
        clientRepresentation.setDirectAccessGrantsEnabled(Boolean.TRUE);

        try (Response response = realmResource.clients().create(clientRepresentation)) {
        } catch (Exception e) {
            log.error("Something went wrong when creating the keycloak client : {}", e.getMessage());
        }

        RoleRepresentation managerRole = new RoleRepresentation();
        managerRole.setId(roleUser);
        managerRole.setName(roleUser);
        managerRole.setClientRole(true);

        RoleRepresentation adminRole = new RoleRepresentation();
        adminRole.setId(roleAdmin);
        adminRole.setName(roleAdmin);
        adminRole.setClientRole(true);

        try {
            realmResource.roles().create(managerRole);
            realmResource.roles().create(adminRole);
        } catch (Exception e) {
            log.error("Something went wrong when creating the realm role : {}", e.getMessage());
        }

        return keycloak;
    }
}
