package com.dokanne.DokaneeBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Data
@Builder
public class ProfileResponse {

    String firstName;

    String lastName;

    String email;

    String phone;

    String dob;

    String nid;

    String address;

    String userName;

    String photoLink;

    boolean isVerified;

    List<String> storeIds;

}
