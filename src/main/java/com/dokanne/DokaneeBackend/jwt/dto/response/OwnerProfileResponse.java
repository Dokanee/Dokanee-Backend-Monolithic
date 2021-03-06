package com.dokanne.DokaneeBackend.jwt.dto.response;

import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class OwnerProfileResponse {
    @NonNull
    String massage;
    @NonNull
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
    String address;

    @NonNull
    List<String> storeIds;
}
