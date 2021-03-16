package com.dokanne.DokaneeBackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table
public class StoreModel {

    @NonNull
    @Id
    private String storeId;
    @NonNull
    private String ownerId;
    @NonNull
    private String storeName;

    @NonNull
    private String storeInfo;

    @NonNull
    private String ownerName;

    private String storeLogo;

    @NonNull
    private String facebookLink;

    @NonNull
    private String youtubeLink;

    @NonNull
    private String googleMapLink;

    @Column(unique = true)
    private String domainName;
    @Column(unique = true)
    @NonNull
    private String subDomainName;
    @NonNull
    private String storeCategory;

    @ElementCollection
    @CollectionTable
    private List<String> storeImages;

    private boolean havePhysicalStore;

    private boolean isApproved;

    private boolean isVerified;
    @NonNull
    private String address;
    @NonNull
    private String upzila;
    @NonNull
    private String zila;
    @NonNull
    private String division;

}
