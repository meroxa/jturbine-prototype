package io.meroxa.turbine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.meroxa.turbine.core.InitRequest;
import io.quarkus.grpc.GrpcService;
import org.jboss.logging.Logger;

import static io.meroxa.turbine.core.TurbineServiceGrpc.TurbineServiceImplBase;

@GrpcService
public class TurbineService extends TurbineServiceImplBase {
    private static final Logger logger = Logger.getLogger(TurbineService.class);

    private Config config;

    @Override
    public void init(InitRequest request, StreamObserver<Empty> responseObserver) {
        try {
            config = Config.parse(request.getConfigFilePath());
            responseObserver.onNext(Empty.newBuilder().build());
        } catch (Exception e) {
            logger.error("failed parsing config", e);
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }
}
