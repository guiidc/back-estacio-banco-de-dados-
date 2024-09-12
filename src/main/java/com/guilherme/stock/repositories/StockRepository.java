package com.guilherme.stock.repositories;

import com.guilherme.stock.entities.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockItem, Long> {
}
