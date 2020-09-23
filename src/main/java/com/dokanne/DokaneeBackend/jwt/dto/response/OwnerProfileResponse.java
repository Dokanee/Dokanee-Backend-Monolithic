package com.dokanne.DokaneeBackend.jwt.dto.response;

import com.dokanne.DokaneeBackend.model.StoreIds;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
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
    @OneToMany(cascade = CascadeType.ALL)
    List<StoreIds> storeIds;
}
