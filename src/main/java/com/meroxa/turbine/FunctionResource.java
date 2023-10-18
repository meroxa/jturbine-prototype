package com.meroxa.turbine;

import java.util.List;

import com.meroxa.turbine.deploy.DeployTurbine;
import com.meroxa.turbine.funtime.proto.Record;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/")
public class FunctionResource {
    @Inject
    Instance<DeployTurbine> turbine;

    @POST
    public List<Record> process(List<Record> records) {
        return records;
    }
}
