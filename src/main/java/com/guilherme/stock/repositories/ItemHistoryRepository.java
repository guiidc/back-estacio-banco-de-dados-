package com.guilherme.stock.repositories;

import com.guilherme.stock.entities.ItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemHistoryRepository extends JpaRepository<ItemHistory, Long> {
}
