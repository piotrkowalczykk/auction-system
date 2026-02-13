package org.kowal.auctionservice.repository;

import org.kowal.auctionservice.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, String> {
    List<Auction> findBySellerId(String sellerId);
}
