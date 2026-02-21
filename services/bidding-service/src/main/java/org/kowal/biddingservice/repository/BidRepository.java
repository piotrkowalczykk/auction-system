package org.kowal.biddingservice.repository;

import org.kowal.biddingservice.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, String> {
    Optional<Bid> findTopByAuctionIdOrderByAmountDesc(String auctionId);
}
