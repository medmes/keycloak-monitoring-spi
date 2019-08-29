package com.keycloak.spi.metrics;

import org.keycloak.provider.ServerInfoAwareProviderFactory;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetricsEndpoint implements RealmResourceProvider, ServerInfoAwareProviderFactory {

    // The ID of the provider is also used as the name of the endpoint
    private static final String METRICS_ENDPOINT_ID = "metrics";

    private static final String MERICS_ENDPOINT_ID_VALUE = "prometheus";



    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        final StreamingOutput stream = output -> PrometheusExporter.instance().export(output);
        return Response.ok(stream).build();
    }

    @Override
    public void close() {
        // Nothing to do, no resources to close
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> operationalInfoMap = new LinkedHashMap<>();
        operationalInfoMap.put(METRICS_ENDPOINT_ID, MERICS_ENDPOINT_ID_VALUE);
        return operationalInfoMap;
    }
}
