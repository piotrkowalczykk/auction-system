package org.kowal.apigateway.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceBidRequestDto {
    private String auctionId;
    private BigDecimal amount;
}
