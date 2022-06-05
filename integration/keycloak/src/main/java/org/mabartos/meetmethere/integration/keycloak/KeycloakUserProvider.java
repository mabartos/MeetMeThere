package org.mabartos.meetmethere.integration.keycloak;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.logging.Logger;

// TODO Properties injection doesn't work ATM
@Transactional
public class KeycloakUserProvider implements UserProvider {
    private static final Logger log = Logger.getLogger(KeycloakUserProvider.class.getName());

    private final MeetMeThereSession session;
    private final Keycloak keycloak;
    private final UsersResource usersResource;

    //@ConfigProperty(name = "keycloak.serverUrl", defaultValue = "http://0.0.0.0:8080/auth")
    private final String SERVER_URL = "http://0.0.0.0:8080/auth";

    //@ConfigProperty(name = "keycloak.realm", defaultValue = "master")
    private final String REALM = "master";

    //@ConfigProperty(name = "keycloak.admin.username", defaultValue = "admin")
    private final String ADMIN_USERNAME = "admin";

    //@ConfigProperty(name = "keycloak.admin.password", defaultValue = "admin")
    private final String ADMIN_PASSWORD = "admin";

    //@ConfigProperty(name = "keycloak.client", defaultValue = "meet-me-there-backend")
    private final String CLIENT = "meet-me-there-backend";

    public KeycloakUserProvider(MeetMeThereSession session) {
        this.session = session;
        log.info(String.format("Keycloak settings: SERVER_URL='%s', REALM='%s', CLIENT='%s'", SERVER_URL, REALM, CLIENT));
        this.keycloak = Keycloak.getInstance(SERVER_URL, REALM, ADMIN_USERNAME, ADMIN_PASSWORD, CLIENT);
        this.usersResource = keycloak.realm(REALM).users();
    }

    @Override
    public UserModel getUserById(String id) {
        return kcAdapter(usersResource.get(id).toRepresentation());
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return usersResource.search(username, true)
                .stream()
                .map(this::kcAdapter)
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserModel getUserByEmail(String email) {
        return usersResource.search(email, 0, 1)
                .stream()
                .map(this::kcAdapter)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<UserModel> getUsers(int firstResult, int maxResults) {
        throw new UnsupportedOperationException("Cannot get all users");
    }

    @Override
    public long getUsersCount() {
        return usersResource.count();
    }

    @Override
    public UserModel createUser(String email, String username) {
        throw new UnsupportedOperationException("Cannot create user here. Visit the Keycloak User Profile.");
    }

    @Override
    public UserModel createUser(UserModel user) throws ModelDuplicateException {
        throw new UnsupportedOperationException("Cannot create user here. Visit the Keycloak User Profile.");
    }

    @Override
    public void removeUser(String id) {
        throw new UnsupportedOperationException("Cannot create user here. Visit the Keycloak User Profile.");
    }

    @Override
    public UserModel updateUser(UserModel user) {
        throw new UnsupportedOperationException("Cannot create user here. Visit the Keycloak User Profile.");
    }

    private UserModel kcAdapter(UserRepresentation userRepresentation) {
        return new KeycloakUserAdapter(session, keycloak, REALM, CLIENT, userRepresentation);
    }
}
