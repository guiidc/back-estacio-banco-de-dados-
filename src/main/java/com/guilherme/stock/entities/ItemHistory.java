package com.guilherme.stock.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "items_history")
@Data
public class ItemHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_item", referencedColumnName = "id")
    @JsonBackReference
    private StockItem item;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @JsonBackReference
    private UserApp user;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
