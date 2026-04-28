package com.mohemeokji.mohemeokji.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpiryPolicyDto {
    private boolean trackExpiry;
    private Integer alertThresholdDays;
    private String displayMode;
    private String storageType;
}
