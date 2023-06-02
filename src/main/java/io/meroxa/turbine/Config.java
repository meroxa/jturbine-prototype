package io.meroxa.turbine;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.nio.file.Paths;
import java.util.Map;

@Data
public class Config {
    private static final ObjectMapper mapper = new ObjectMapper();

    private String name;
    private String language;
    private Map<String, String> resources;

    @SneakyThrows
    public static Config parse(String path) {
        return mapper.readValue(Paths.get(path).toFile(), Config.class);
    }
}
