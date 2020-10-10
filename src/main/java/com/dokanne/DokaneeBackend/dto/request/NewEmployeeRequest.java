package com.dokanne.DokaneeBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class NewEmployeeRequest {

    String email;

    String phoneNo;

    String nid;

    String password;

}
