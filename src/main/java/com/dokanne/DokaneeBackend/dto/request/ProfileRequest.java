package com.dokanne.DokaneeBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

public class ProfileRequest {

    String firstName;

    String lastName;

    String dob;

    String address;

}
