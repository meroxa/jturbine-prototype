package com.meroxa.turbine;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class FunctionRecord {
    public String key;
    public String value;
    public LocalDateTime timestamp;
    public Map<String, Object> structured;
}
