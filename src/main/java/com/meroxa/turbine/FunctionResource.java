package com.meroxa.turbine;

import java.util.List;
import java.util.stream.Collectors;

import com.meroxa.turbine.deploy.DeployTurbine;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.SneakyThrows;

@Path("/")
public class FunctionResource {
    @Inject
    Instance<DeployTurbine> turbine;

    @SneakyThrows
    @POST
    public List<FunctionRecord> process(List<FunctionRecord> records) {
        List<TurbineRecord> turbineRecords = Utils.toStream(records)
            .map(fr -> TurbineRecord.builder()
                .key(fr.key)
                .payload(fr.value)
                .timestamp(fr.timestamp)
                .build()
            ).toList();

        List<TurbineRecord> processed = turbine.get().getProcessor().apply(turbineRecords);

        return processed.stream()
            .map(tr -> FunctionRecord.builder()
                .key(tr.getKey())
                .value(tr.getPayload())
                .timestamp(tr.getTimestamp())
                .build())
            .toList();
    }
}
