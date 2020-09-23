package com.dokanne.DokaneeBackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table
public class StoreIds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String storeId;
}
