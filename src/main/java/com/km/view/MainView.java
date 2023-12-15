package com.km.view;

import com.km.service.KafkaService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {

    @Autowired
    private KafkaService kafkaService;
    private final Span spanLine1 = new Span();
    private final Span spanLine2 = new Span();

    public MainView() {
        HorizontalLayout line1 = new HorizontalLayout();
        line1.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        HorizontalLayout line2 = new HorizontalLayout();
        line2.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        line1.add(new Button("Produce", this::produceClick));
        line1.add(spanLine1);
        line2.add(new Button("Consume", this::consumeClick));
        line2.add(spanLine2);
        add(line1);
        add(line2);
    }

    private void produceClick(ClickEvent<Button> click) {
        kafkaService.printConfig();
        spanLine1.setText("Done");
    }

    private void consumeClick(ClickEvent<Button> click) {
        kafkaService.printConfig();
        spanLine2.setText("Done");
    }
}
