package com.dokanne.DokaneeBackend.dto.response.shopResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ShopTemplateResponse {

    private String subDomain;

    private String templateId;

    private String primaryColor;

    private String secondaryColor;

    private List<String> sliderImages;
}
