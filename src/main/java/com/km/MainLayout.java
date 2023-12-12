package com.km;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;

public class MainLayout extends AppLayout {

    private transient final AuthenticationContext authContext;

    public MainLayout(AuthenticationContext authContext) {
        this.authContext = authContext;
        HorizontalLayout header = authContext.getAuthenticatedUser(UserDetails.class)
                .map(this::getHorizontalLayout)
                .orElseThrow();
        addToNavbar(header);
    }

    private HorizontalLayout getHorizontalLayout(UserDetails user) {
        Button logout = new Button("Logout", click -> authContext.logout());
        H2 loggedUser = new H2("Logged in as " + user.getUsername());
        return new HorizontalLayout(loggedUser, logout);
    }
}
