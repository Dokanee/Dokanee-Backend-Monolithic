package com.dokanne.DokaneeBackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table
public class StoreModel {


    @Id
    private String storeId;

    private String ownerId;

    private String storeName;

    private String storeInfo;

    private String ownerName;

    private String storeLogo;

    private String facebookLink;

    private String youtubeLink;

    private String googleMapLink;

    @Column(unique = true)
    private String domainName;
    @Column(unique = true)

    private String subDomainName;

    private String storeCategory;

    @ElementCollection
    @CollectionTable
    private List<String> storeImages;

    private boolean havePhysicalStore;

    private boolean isApproved;

    private boolean isVerified;

    private String address;

    private String upzila;

    private String zila;

    private String division;

}
