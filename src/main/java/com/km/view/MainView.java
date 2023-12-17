package com.km.view;

import com.km.service.KafkaService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {
    private static final long PUSH_INTERVAL = 500;
    private static final long TIMEOUT = 250;
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    private final Span spanLine1 = new Span();
    private final Span spanLine2 = new Span();
    private final Button produceStart;
    private final Button produceStop;
    private final Button consumeStart;
    private final Button consumeStop;
    private ScheduledExecutorService executor;

    public MainView() {
        HorizontalLayout line1 = new HorizontalLayout();
        line1.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        HorizontalLayout line2 = new HorizontalLayout();
        line2.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        produceStart = new Button("Produce", this::produceStart);
        produceStop = new Button("Stop", this::produceStop);
        consumeStart = new Button("Consume", this::consumeStart);
        consumeStop = new Button("Stop", this::consumeStop);

        line1.add(produceStart);
        line1.add(produceStop);
        line1.add(spanLine1);
        line2.add(consumeStart);
        line2.add(consumeStop);
        line2.add(spanLine2);

        add(line1);
        add(line2);
    }

    private void setButtonsState() {
        produceStart.setEnabled(!getKS().getProducerRunner().isRunning());
        produceStop.setEnabled(getKS().getProducerRunner().isRunning());
        consumeStart.setEnabled(!getKS().getConsumerRunner().isRunning());
        consumeStop.setEnabled(getKS().getConsumerRunner().isRunning());
    }

    private static <T> T get(Class<T> serviceType) {
        return WebApplicationContextUtils
                .getWebApplicationContext(VaadinServlet.getCurrent().getServletContext())
                .getBean(serviceType);
    }

    private static KafkaService getKS() {
        return get(KafkaService.class);
    }

    private void produceStart(ClickEvent<Button> click) {
        getKS().getProducerRunner().start();
        spanLine1.setText("Started");
    }

    private void produceStop(ClickEvent<Button> click) {
        getKS().getProducerRunner().stop();
        spanLine1.setText(String.format("Stopped after %d messages", getKS().getProducerRunner().getCounter()));
    }

    private void consumeStart(ClickEvent<Button> click) {
        getKS().getConsumerRunner().start();
        spanLine2.setText("Started");
    }

    private void consumeStop(ClickEvent<Button> click) {
        getKS().getConsumerRunner().stop();
        spanLine2.setText(String.format("Stopped after %d messages", getKS().getConsumerRunner().getCounter()));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new Push(attachEvent.getUI()), 0, PUSH_INTERVAL, TimeUnit.MILLISECONDS);
        logger.info("Backend push started");
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        executor.shutdown();
        try {
            if (executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS))
                logger.info("Backend push stopped in timely manner");
            else
                logger.warn("Backend push stopped forcefully");
        } catch (InterruptedException e) {
            throw new RuntimeException("Unrecoverable state", e);
        }
        executor = null;
    }

    private class Push implements Runnable {
        private final UI ui;

        public Push(UI ui) {
            this.ui = ui;
        }

        @Override
        public void run() {
            ui.access(MainView.this::setButtonsState);
        }
    }
}
