package com.dokanne.DokaneeBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AllEmployeeResponse {
    String name;

    String email;

    String phoneNo;

    String photoLink;
}
