package com.dokanne.DokaneeBackend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(unique = true)
    private String domainName;
    @Column(unique = true)
    @NonNull
    private String subDomainName;
    @NonNull
    private String storeCategory;

    private String templateId;

    private String storeImage;

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
