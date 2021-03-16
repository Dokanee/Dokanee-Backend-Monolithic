package com.dokanne.DokaneeBackend.dto.response.shopResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopStoreResponse {

    private String storeName;

    private String storeInfo;

    private String ownerName;

    private String storeLogo;

    private String facebookLink;

    private String youtubeLink;

    private String googleMapLink;

    private String domainName;

    private String subDomainName;

    private String storeCategory;

    private List<String> storeImages;

    private boolean havePhysicalStore;

    private boolean isApproved;

    private boolean isVerified;

    private String address;

    private String upzila;

    private String zila;

    private String division;
}
