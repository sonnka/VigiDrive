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
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

        createKeycloakRealm(keycloak);

        createKeycloakClient(realmResource);

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