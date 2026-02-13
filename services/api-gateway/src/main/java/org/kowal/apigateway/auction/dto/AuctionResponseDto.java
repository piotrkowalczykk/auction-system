package org.kowal.apigateway.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kowal.enums.AuctionStatus;
import org.kowal.enums.AuctionType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionResponseDto {
    private String id;
    private String title;
    private String description;
    private String sellerId;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private BigDecimal buyNowPrice;
    private BigDecimal minIncrement;
    private Instant startTime;
    private Instant endTime;
    private AuctionStatus auctionStatus;
    private AuctionType auctionType;
    private Instant createdAt;
}
