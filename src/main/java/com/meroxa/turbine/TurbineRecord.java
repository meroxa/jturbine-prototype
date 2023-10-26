package com.meroxa.turbine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class TurbineRecord {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String key;
    private String payload;
    private LocalDateTime timestamp;

    public TurbineRecord copy() {
        return this.toBuilder().build();
    }

    public void jsonSet(String path, Object value) {
        var newPayload = JsonPath.parse(base64DecodeAfter())
            .set(path, value)
            .jsonString();

        setPayload(base64EncodeAfter(newPayload));
    }

    public void jsonAdd(String path, String key, Object value) {
        var newPayload = JsonPath.parse(base64DecodeAfter())
            .put(path, key, value)
            .jsonString();
        setPayload(base64EncodeAfter(newPayload));
    }

    public Object jsonGet(String path) {
        return JsonPath.read(base64DecodeAfter(), path);
    }

    @SneakyThrows
    private String base64DecodeAfter() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = (ObjectNode) mapper.readTree(getPayload());
        // after is a Base64 encoded string
        String after = json.get("after").asText();
        String afterDec = new String(Base64.getDecoder().decode(after));
        json.put("after", mapper.readTree(afterDec));

        return mapper.writeValueAsString(json);
    }

    @SneakyThrows
    private String base64EncodeAfter(String payload) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = (ObjectNode) mapper.readTree(payload);

        String after = mapper.writeValueAsString(json.get("after"));
        String afterEnc = Base64.getEncoder().encodeToString(after.getBytes(StandardCharsets.UTF_8));
        json.put("after", afterEnc);

        return mapper.writeValueAsString(json);
    }
}

