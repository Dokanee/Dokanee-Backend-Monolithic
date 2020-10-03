package com.dokanne.DokaneeBackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor

@Entity
@Table
public class ProfileModel {

    @NonNull
    @Id
    String ownerId;

    @NonNull
    String firstName;

    @NonNull
    String lastName;

    @NonNull
    String email;

    @NonNull
    String phone;

    @NonNull
    String dob;

    @NonNull
    String nid;

    @NonNull
    String address;

    @NonNull
    String userName;

    String photoLink;

    boolean isVerified;

    @OneToMany(cascade = CascadeType.ALL)
    List<StoreIds> storeIds;

}
