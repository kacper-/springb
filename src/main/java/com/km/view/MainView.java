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
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.shared.communication.PushMode;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {
    private Thread thread;
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

    private static <T> T get(Class<T> serviceType) {
        return WebApplicationContextUtils
                .getWebApplicationContext(VaadinServlet.getCurrent().getServletContext())
                .getBean(serviceType);
    }

    private static KafkaService getKS() {
        return get(KafkaService.class);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        thread = new Thread(new UpdateThread(attachEvent.getUI(), produceStart, produceStop, consumeStart, consumeStop));
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        thread.interrupt();
        thread = null;
    }

    private static class UpdateThread implements Runnable {
        private final UI ui;
        private final Button produceStart;
        private final Button produceStop;
        private final Button consumeStart;
        private final Button consumeStop;

        public UpdateThread(UI ui, Button produceStart, Button produceStop, Button consumeStart, Button consumeStop) {
            this.ui = ui;
            this.produceStart = produceStart;
            this.produceStop = produceStop;
            this.consumeStart = consumeStart;
            this.consumeStop = consumeStop;
        }

        private void setButtonsState() {
            produceStart.setEnabled(!getKS().getProducerRunner().isRunning());
            produceStop.setEnabled(getKS().getProducerRunner().isRunning());
            consumeStart.setEnabled(!getKS().getConsumerRunner().isRunning());
            consumeStop.setEnabled(getKS().getConsumerRunner().isRunning());
            ui.push();
        }
        @Override
        public void run() {
            try {
                while(true) {
                    Thread.sleep(500);
                    ui.access(this::setButtonsState);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Unrecoverable state");
            }
        }
    }
}
