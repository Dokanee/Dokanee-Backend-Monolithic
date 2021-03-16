package com.dokanne.DokaneeBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class StoreInfoResponse {
    private String storeId;

    private String storeName;

    private String storeInfo;

    private String storeLogo;

    private String facebookLink;

    private String youtubeLink;

    private String googleMapLink;

    private String ownerName;

    private String domainName;

    private String subDomainName;

    private String storeCategory;

    private boolean havePhysicalStore;

    private String address;

    private String upzila;

    private String zila;

    private String division;
}
