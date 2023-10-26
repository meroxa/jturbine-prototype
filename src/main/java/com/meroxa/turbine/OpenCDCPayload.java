package com.meroxa.turbine;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class OpenCDCPayload {
    private byte[] before;
    private byte[] after;

    @SneakyThrows
    public static OpenCDCPayload fromString(String s) {
        return new ObjectMapper().readValue(s, OpenCDCPayload.class);
    }

    @SneakyThrows
    public String jsonString() {
        return new ObjectMapper().writeValueAsString(this);
    }

    public void afterJsonSet(String path, Object value) {
        var newPayload = JsonPath.parse(getAfter())
            .set(path, value)
            .jsonString()
            .getBytes(StandardCharsets.UTF_8);
        setAfter(newPayload);
    }

    public void afterJsonAdd(String path, String key, Object value) {
        var newPayload = JsonPath.parse(getAfter())
            .put(path, key, value)
            .jsonString()
            .getBytes(StandardCharsets.UTF_8);
        setAfter(newPayload);
    }

    public Object afterJsonGet(String path) {
        return JsonPath.read(getAfter(), path);
    }
}
