package com.meroxa.turbine.run;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meroxa.turbine.RecordsCollection;
import com.meroxa.turbine.Turbine;
import com.meroxa.turbine.proto.Configuration;
import com.meroxa.turbine.proto.Configurations;
import com.meroxa.turbine.proto.InitRequest;
import com.meroxa.turbine.proto.Language;
import com.meroxa.turbine.proto.ReadFromSourceRequest;
import com.meroxa.turbine.proto.TurbineServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;

public class LocalTurbine implements Turbine {
    private static final Logger logger = Logger.getLogger(LocalTurbine.class);

    private final TurbineServiceGrpc.TurbineServiceBlockingStub stub;

    public static LocalTurbine create(String turbineCoreServer, String appPath) {
        logger.infof("creating local turbine, server %s, app path %s", turbineCoreServer, appPath);

        String[] split = turbineCoreServer.split(":");
        var channelBuilder = ManagedChannelBuilder.forAddress(split[0], Integer.parseInt(split[1])).usePlaintext();
        var channel = channelBuilder.build();
        var stub = TurbineServiceGrpc.newBlockingStub(channel);

        LocalTurbine turbine = new LocalTurbine(stub);
        turbine.init(appPath);
        return turbine;
    }

    private void init(String appPath) {
        logger.info("initializing");

        stub.init(InitRequest
            .newBuilder()
            .setAppName(readAppName(appPath))
            .setConfigFilePath(appPath)
            .setGitSHA("test-git-sha")
            .setLanguage(Language.JAVA)
            .setTurbineVersion("1.0-SNAPSHOT")
            .build());

        logger.info("init done");
    }

    @SneakyThrows
    private static String readAppName(String appPath) {
        JsonNode appJson = new ObjectMapper().readTree(Paths.get(appPath, "app.json").toFile());
        return appJson.get("name").asText();
    }

    public LocalTurbine(TurbineServiceGrpc.TurbineServiceBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public RecordsCollection fromSource(String plugin, Map<String, String> configs) {
        var configurations = Configurations.newBuilder();

        for (Map.Entry<String, String> kv : configs.entrySet()) {
            var c = Configuration.newBuilder().setField(kv.getKey()).setValue(kv.getValue()).build();
            configurations.addConfiguration(c);
        }

        com.meroxa.turbine.proto.RecordsCollection response = stub.readFromSource(
            ReadFromSourceRequest.newBuilder()
                .setPluginName(plugin)
                .setDirection(ReadFromSourceRequest.Direction.SOURCE)
                .setConfiguration(configurations)
                .build()
        );

        logger.infof("fromSource got stream name %s", response.getStream());
        return LocalRecordsCollection.fromProtoCollection(
            stub,
            response
        );
    }

    @Override
    public Map<String, String> configFromSecret(String secretname) {
        return Collections.emptyMap();
    }

    /*
    public com.meroxa.turbine.Resource resource(String name) {
        GetResourceRequest get = GetResourceRequest
            .newBuilder()
            .setName(name)
            .build();
        Resource resource = stub.getResource(get);
        return new LocalResource(stub, resource);
    }
    */

}
