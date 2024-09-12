package com.guilherme.stock.controllers;

import com.guilherme.stock.dto.requests.CreateStockItemDTO;
import com.guilherme.stock.dto.requests.UpdateStockItemQuantityDTO;
import com.guilherme.stock.dto.responses.SimpleResponse;
import com.guilherme.stock.entities.StockItem;
import com.guilherme.stock.services.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/v1/stock")
@RequiredArgsConstructor
public class StockController {

   private final StockService stockService;

   @PostMapping({"/item", "/item/"})
   public ResponseEntity<StockItem> createStockItem(@RequestBody @Valid CreateStockItemDTO data) {
      StockItem insertedItem = stockService.createStockItem(data);
      return ResponseEntity.created(null).body(insertedItem);
   }

   @GetMapping({"/list", "/list/"})
    public ResponseEntity<List<StockItem>> listStockItems() {
        List<StockItem> items = stockService.listStockItems();
        return ResponseEntity.ok(items);
    }

    @PutMapping({"/item/{id}", "/item/{id}/"})
    public ResponseEntity<Object> updateStockItem(@PathVariable Long id, @RequestBody @Valid UpdateStockItemQuantityDTO data) {
        StockItem updatedItem = stockService.updateItemQuantity(id, data.getQuantity());
        return ResponseEntity.ok(updatedItem);
    }

    @GetMapping({"/item/{id}", "/item/{id}/"})
    public ResponseEntity<StockItem> findStockItemById(@PathVariable Long id) {
        StockItem item = stockService.findStockItemById(id);
        return ResponseEntity.ok(item);
    }

}
