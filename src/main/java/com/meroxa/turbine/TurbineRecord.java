package com.meroxa.turbine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class TurbineRecord {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String key;
    private String payload;
    private LocalDateTime timestamp;

    public TurbineRecord copy() {
        return this.toBuilder().build();
    }

    public void jsonSet(String path, Object value) {
        var newPayload = JsonPath.parse(getPayload())
            .set(path, value)
            .jsonString();
        setPayload(newPayload);
    }

    public void jsonAdd(String path, String key, Object value) {
        var newPayload = JsonPath.parse(getPayload())
            .put(path, key, value)
            .jsonString();
        setPayload(newPayload);
    }

    public Object jsonGet(String path) {
        return JsonPath.read(getPayload(), path);
    }
}
