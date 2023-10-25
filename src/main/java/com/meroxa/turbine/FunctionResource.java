package com.meroxa.turbine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import com.meroxa.turbine.deploy.DeployTurbine;
import com.meroxa.turbine.run.LocalTurbine;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;

@Path("/")
public class FunctionResource {
    private static final Logger logger = Logger.getLogger(LocalTurbine.class);

    @Inject
    Instance<DeployTurbine> turbine;

    @SneakyThrows
    @POST
    public OpenCDCRecord process(OpenCDCRecord record) {
        logger.infof("FunctionResource.process got %s", record);

        List<TurbineRecord> turbineRecords = List.of(
            TurbineRecord.builder()
                .key(new String(record.getKey()))
                .payload(record.getPayload().toString())
                .timestamp(LocalDateTime.now())
                .build()
        );

        List<TurbineRecord> processed = turbine.get().getProcessor().apply(turbineRecords);
        var tr = processed.get(0);

        logger.infof("transformed turbine record: %s", tr);

        return OpenCDCRecord.builder()
            .key(tr.getKey().getBytes(StandardCharsets.UTF_8))
            .payload(OpenCDCPayload.fromString(tr.getPayload()))
            .build();
    }

    public List<FunctionRecord> processOld(List<FunctionRecord> records) {
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
