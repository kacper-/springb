package com.km.view;

import com.km.service.KafkaService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
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
    private final Button produceStart;
    private final Button produceStop;
    private final Button consumeStart;
    private final Button consumeStop;

    public MainView() {
        HorizontalLayout line1 = new HorizontalLayout();
        line1.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        HorizontalLayout line2 = new HorizontalLayout();
        line2.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        produceStart = new Button("Produce", this::produceStart);
        produceStop = new Button("Stop", this::produceStop);
        produceStop.setEnabled(false);
        consumeStart = new Button("Consume", this::consumeStart);
        consumeStop = new Button("Consume", this::consumeStop);
        consumeStop.setEnabled(false);

        line1.add(produceStart);
        line1.add(produceStop);
        line1.add(spanLine1);
        line2.add(consumeStart);
        line2.add(consumeStop);
        line2.add(spanLine2);

        add(line1);
        add(line2);
    }

    private void produceStart(ClickEvent<Button> click) {
        kafkaService.getProducerRunner().start();
        spanLine1.setText("Started");
        produceStart.setEnabled(false);
        produceStop.setEnabled(true);
    }

    private void produceStop(ClickEvent<Button> click) {
        kafkaService.getProducerRunner().stop();
        spanLine1.setText(String.format("Stopped after %d messages", kafkaService.getProducerRunner().getCounter()));
        produceStart.setEnabled(true);
        produceStop.setEnabled(false);
    }

    private void consumeStart(ClickEvent<Button> click) {
        kafkaService.getConsumerRunner().start();
        spanLine2.setText("Started");
        consumeStart.setEnabled(false);
        consumeStop.setEnabled(true);
    }

    private void consumeStop(ClickEvent<Button> click) {
        kafkaService.getConsumerRunner().stop();
        spanLine2.setText(String.format("Stopped after %d messages", kafkaService.getConsumerRunner().getCounter()));
        consumeStart.setEnabled(true);
        consumeStop.setEnabled(false);
    }
}
