package com.keycloak.spi.metrics;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

public class MetricsEventListener implements EventListenerProvider {

    private static final Logger logger = Logger.getLogger(MetricsEventListener.class);

    @Override
    public void onEvent(Event event) {
        this.logEventDetails(event);

        switch (event.getType()) {
            case LOGIN:
                PrometheusExporter.instance().recordLogin(event);
                break;
            case REGISTER:
                PrometheusExporter.instance().recordRegistration(event);
                break;
            case LOGIN_ERROR:
                PrometheusExporter.instance().recordLoginError(event);
                break;
            default:
                PrometheusExporter.instance().recordGenericEvent(event);
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        this.logAdminEventDetails(event);

        PrometheusExporter.instance().recordGenericAdminEvent(event);
    }

    private void logEventDetails(Event event) {
        logger.infof("Received user event of type %s in realm %s",
                event.getType().name(),
                event.getRealmId());
    }

    private void logAdminEventDetails(AdminEvent event) {
        logger.infof("Received admin event of type %s (%s) in realm %s",
                event.getOperationType().name(),
                event.getResourceType().name(),
                event.getRealmId());
    }

    @Override
    public void close() {
        // unused
    }
}
