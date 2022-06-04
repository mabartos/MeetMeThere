package org.mabartos.meetmethere.integration.keycloak;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.model.exception.ModelDuplicateException;
import org.mabartos.meetmethere.api.provider.UserProvider;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public class KeycloakUserProvider implements UserProvider {
    private final MeetMeThereSession session;
    private final Keycloak keycloak;
    private final UsersResource usersResource;

    @ConfigProperty(name = "keycloak.serverUrl")
    String SERVER_URL;

    @ConfigProperty(name = "keycloak.realm")
    String REALM;

    @ConfigProperty(name = "keycloak.admin.username")
    String ADMIN_USERNAME;

    @ConfigProperty(name = "keycloak.admin.password")
    String ADMIN_PASSWORD;

    @ConfigProperty(name = "keycloak.client")
    String CLIENT;

    public KeycloakUserProvider(MeetMeThereSession session) {
        this.session = session;
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
        return new KeycloakUserAdapter(session, keycloak, REALM, userRepresentation);
    }
}
