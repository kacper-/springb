package com.km;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class WelcomeView extends VerticalLayout {
    public WelcomeView() {
        add(new H1("Welcome"));
    }
}
