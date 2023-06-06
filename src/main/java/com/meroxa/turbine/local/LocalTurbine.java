package com.meroxa.turbine.local;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meroxa.turbine.proto.GetResourceRequest;
import com.meroxa.turbine.proto.InitRequest;
import com.meroxa.turbine.proto.Language;
import com.meroxa.turbine.proto.Resource;
import io.grpc.ManagedChannelBuilder;
import com.meroxa.turbine.Turbine;
import com.meroxa.turbine.proto.TurbineServiceGrpc;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;

import java.nio.file.Paths;

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
    public com.meroxa.turbine.Resource resource(String name) {
        GetResourceRequest get = GetResourceRequest
            .newBuilder()
            .setName(name)
            .build();
        Resource resource = stub.getResource(get);
        return new LocalResource(stub, resource);
    }

    @Override
    public void registerSecret(String name) {

    }
}
