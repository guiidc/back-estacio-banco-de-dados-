package com.guilherme.stock.services;

import com.guilherme.stock.dto.requests.CreateStockItemDTO;
import com.guilherme.stock.entities.ItemHistory;
import com.guilherme.stock.entities.StockItem;
import com.guilherme.stock.entities.UserApp;
import com.guilherme.stock.httpExceptions.BadRequestException;
import com.guilherme.stock.httpExceptions.NotFoundException;
import com.guilherme.stock.repositories.ItemHistoryRepository;
import com.guilherme.stock.repositories.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final StockRepository stockRepository;
    private final ItemHistoryRepository itemHistoryService;

    public StockItem createStockItem(CreateStockItemDTO data) {
        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StockItem stockItem = new StockItem();
        stockItem.setName(data.getName());
        stockItem.setQuantity(data.getQuantity());
        stockItem.setDescription(data.getDescription());
        stockItem.setPrice(data.getPrice());
        stockItem.setMinQuantityWarning(0);

        ItemHistory itemHistory = new ItemHistory();
        itemHistory.setItem(stockItem);
        itemHistory.setQuantity(data.getQuantity());
        itemHistory.setUser(user);

        stockItem.getItemHistories().add(itemHistory);
        return stockRepository.save(stockItem);
    }

    public List<StockItem> listStockItems() {
        return stockRepository.findAll();
    }

    public StockItem findStockItemById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new NotFoundException("Item não encontrado no estoque"));

    }

    public StockItem updateStockItem(StockItem item) {
        return stockRepository.save(item);
    }

    public StockItem updateItemQuantity(Long id, Integer quantity) {
        StockItem retrievedItem = findStockItemById(id);

        if (retrievedItem == null) {
            throw new NotFoundException("Item não encontrado no estoque");
        }

        if (retrievedItem.getQuantity() + quantity < 0) {
            throw new BadRequestException("A quantidade final do item não pode ser menor que zero");
        }

        retrievedItem.setQuantity(retrievedItem.getQuantity() + quantity);

        UserApp user = (UserApp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ItemHistory history = new ItemHistory();
        history.setItem(retrievedItem);
        history.setQuantity(quantity);
        history.setUser(user);

        retrievedItem.getItemHistories().add(history);
        return updateStockItem(retrievedItem);
    }
}
