package com.dokanne.DokaneeBackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table
public class SubCategoryModel {
    @Id
    String SubCategoryId;

    String SubCategoryName;

    String slug;
}

//Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoamhhYmliMjRAZ21haWwuY29tc3RyaW5nIiwic2NvcGVzIjoiT1dORVIiLCJpYXQiOjE2MDEyNTAyNjEsImV4cCI6MTYwMzM1MDI2MX0._KpzMR9VAuwTbhmVnjpYH_n9s_hdXmGdRM2iPCNI06VRZZK8YnAQvwggQhJI32kRf0-kTgad3XkGN86D1jnKoQ

//6e50bab7-f4b5-4034-8945-26b3f32ac4b7

//1fb76f90-a49e-4d63-85d8-52f0d08cb6e7

