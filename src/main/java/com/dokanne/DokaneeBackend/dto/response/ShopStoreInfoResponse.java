package com.dokanne.DokaneeBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopStoreInfoResponse {
    String storeName;

    String storeLogo;

    String domain;

    String subDomain;

    String ownerName;

    String storeCategory;

    String address;

    String upzila;

    String zila;
}
