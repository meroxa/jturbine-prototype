package com.meroxa.turbine;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import com.meroxa.turbine.deploy.DeployTurbine;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FunctionResourceTest {

    @Test
    void test() {
        var in = OpenCDCRecord.builder()
            .position(Base64.getDecoder().decode("eyJncm91cElEIjoiNDVkYjk1ZGMtOGI2Yi00NDQ5LTkyN2QtYTNlODViZTcyZmQwIiwidG9waWMiOiJjaXNjby1kZW1vLTMtc291cmNlLWZpbGUiLCJwYXJ0aXRpb24iOjAsIm9mZnNldCI6MjJ9"))
            .key(Base64.getDecoder().decode("MTU="))
            .payload(
                OpenCDCPayload.builder()
                    .after("{\"customer_email\": \"HaRis@meroXA.IO\"}".getBytes(StandardCharsets.UTF_8))
                    .build()
            )
            .operation("create")
            .build();

        FunctionResource underTest = new FunctionResource();
        underTest.turbine = newInstance(records -> records.stream()
            .map(r -> {
                var copy = r.copy();
                String email = (String) copy.jsonGet("$.after.customer_email");
                copy.jsonSet("$.after.customer_email", email.toLowerCase());

                return copy;
            })
            .toList());

        byte[] res = underTest.process(in);
        Assertions.assertNotNull(res);
        Assertions.assertEquals("{\"customer_email\":\"haris@meroxa.io\"}", new String(res));
    }

    private Instance<DeployTurbine> newInstance(Processor processor) {
        return new Instance<>() {
            @Override
            public Instance<DeployTurbine> select(Annotation... qualifiers) {
                return null;
            }

            @Override
            public <U extends DeployTurbine> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
                return null;
            }

            @Override
            public <U extends DeployTurbine> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
                return null;
            }

            @Override
            public boolean isUnsatisfied() {
                return false;
            }

            @Override
            public boolean isAmbiguous() {
                return false;
            }

            @Override
            public void destroy(DeployTurbine instance) {

            }

            @Override
            public Handle<DeployTurbine> getHandle() {
                return null;
            }

            @Override
            public Iterable<? extends Handle<DeployTurbine>> handles() {
                return null;
            }

            @Override
            public DeployTurbine get() {
                DeployTurbine t = new DeployTurbine();
                t.setProcessor(processor);
                return t;
            }

            @Override
            public Iterator<DeployTurbine> iterator() {
                return null;
            }
        };
    }
}