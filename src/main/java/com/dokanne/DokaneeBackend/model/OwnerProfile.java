package com.dokanne.DokaneeBackend.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor

@Entity
@Table
public class OwnerProfile {

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

    String photoLink;

    boolean isVerified;

}
