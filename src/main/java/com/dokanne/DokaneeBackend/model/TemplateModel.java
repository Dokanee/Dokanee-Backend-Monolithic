package com.dokanne.DokaneeBackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data

@Entity
@Table
public class TemplateModel {
    @Id
    private String id;

    private String storeId;

    private String subDomain;

    private String templateId;

    private String primaryColor;

    private String secondaryColor;

    @ElementCollection
    @CollectionTable
    private List<String> sliderImages;
}
