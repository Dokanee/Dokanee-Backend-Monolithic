package com.dokanne.DokaneeBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class StoreRequest {

    private String storeName;

    private String subDomainName;

    private String storeCategory;

    private boolean havePhysicalStore;

    private String address;

    private String upzila;

    private String zila;

    private String division;
}
