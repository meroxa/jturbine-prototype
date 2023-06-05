package io.meroxa.turbine;

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
    private String key;
    private String payload;
    private LocalDateTime timestamp;

    public TurbineRecord copy() {
        return this.toBuilder().build();
    }
}
