package org.mabartos.meetmethere.integration.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mabartos.meetmethere.api.model.EventModel;
import org.mabartos.meetmethere.api.model.UserModel;
import org.mabartos.meetmethere.api.session.MeetMeThereSession;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KeycloakUserAdapter implements UserModel {
    private final MeetMeThereSession session;
    private final UserRepresentation userRepresentation;
    private final UserResource userResource;
    private final String client;

    public KeycloakUserAdapter(@NotNull MeetMeThereSession session,
                               @NotNull Keycloak keycloak,
                               @NotNull String realm,
                               @NotNull String client,
                               @NotNull UserRepresentation userRepresentation) {
        this.session = session;
        this.userRepresentation = userRepresentation;
        this.userResource = keycloak.realm(realm).users().get(userRepresentation.getId());
        this.client = client;
    }

    @Override
    public Map<String, String> getAttributes() {
        return userRepresentation.getAttributes()
                .entrySet()
                .stream()
                .map(f -> Map.entry(f.getKey(), f.getValue().stream().findFirst().orElse("")))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        final Map<String, List<String>> kcAttributes = attributes.entrySet()
                .stream()
                .map(f -> Map.entry(f.getKey(), Collections.singletonList(f.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        userRepresentation.setAttributes(kcAttributes);
        userResource.update(userRepresentation);
    }

    @Override
    public String getId() {
        return userRepresentation.getId();
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException("Cannot change user's ID");
    }

    @Override
    public String getUsername() {
        return userRepresentation.getUsername();
    }

    @Override
    public void setUsername(String username) {
        throw new UnsupportedOperationException("Cannot change username");
    }

    @Override
    public String getFirstName() {
        return userRepresentation.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        userRepresentation.setFirstName(firstName);
        userResource.update(userRepresentation);
    }

    @Override
    public String getLastName() {
        return userRepresentation.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        userRepresentation.setLastName(lastName);
        userResource.update(userRepresentation);
    }

    @Override
    public String getEmail() {
        return userRepresentation.getEmail();
    }

    @Override
    public void setEmail(String email) {
        throw new UnsupportedOperationException("Cannot change user's email");
    }

    @Override
    public Set<EventModel> getOrganizedEvents() {
        return session.eventStorage().getEventsByOrganizator(userRepresentation.getId());
    }

    @Override
    public void setOrganizedEvents(Set<EventModel> events) {
        events.forEach(f -> {
            final Set<UserModel> organizers = f.getOrganizers();
            organizers.add(this);
            f.setOrganizers(organizers);
        });
    }

    @Override
    public Set<String> getRoles() {
        final Set<String> roles = new HashSet<>(userRepresentation.getRealmRoles());
        roles.addAll(new HashSet<>(userRepresentation.getClientRoles().get(client)));
        return roles;
    }

    @Override
    public void setRoles(Set<String> roles) {
        throw new UnsupportedOperationException("Cannot set roles");
    }
}
