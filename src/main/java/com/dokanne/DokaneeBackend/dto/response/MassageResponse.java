package com.dokanne.DokaneeBackend.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor

@Getter
@Setter

public class MassageResponse {
    @NonNull
    String massage;

    Object body;

    @NonNull
    int statusCode;
}
