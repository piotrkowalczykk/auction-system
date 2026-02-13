package org.kowal.auctionservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.kowal.enums.AuctionStatus;
import org.kowal.enums.AuctionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "auctions")
public class Auction {
    @Id
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
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;
    @Enumerated(EnumType.STRING)
    private AuctionType auctionType;
    private String winnerId;
    private Instant createdAt;

    @PrePersist
    public void prePersist(){
        if(id == null)
            id = UUID.randomUUID().toString();

        createdAt = Instant.now();

        if(currentPrice == null)
            currentPrice = startPrice;

        if(auctionStatus == null)
            auctionStatus = AuctionStatus.ACTIVE;
    }
}
