package com.meroxa.turbine;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class OpenCDCRecord {
    private byte[] position;
    private String operation;
    private Map<String, String> metadata;
    private byte[] key;
    private OpenCDCPayload payload;
}
