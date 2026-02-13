package org.kowal.apigateway.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kowal.enums.AuctionType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAuctionRequestDto {
    private String title;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal buyNowPrice;
    private BigDecimal minIncrement;
    private Instant endTime;
    private AuctionType auctionType;
}
