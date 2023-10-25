package com.meroxa.turbine;

import java.nio.charset.StandardCharsets;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class OpenCDCPayload {
    private byte[] before;
    private byte[] after;

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
