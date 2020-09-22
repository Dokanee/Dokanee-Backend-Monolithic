package com.dokanne.DokaneeBackend.jwt.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TestResponse {
    String massage;

    String username;
    String email;
    String firstName;
    String lastName;
    String phoneNo;
    Set<String> role;

}
