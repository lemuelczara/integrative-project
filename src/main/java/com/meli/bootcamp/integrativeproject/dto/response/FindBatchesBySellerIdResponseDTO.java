package com.meli.bootcamp.integrativeproject.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindBatchesBySellerIdResponseDTO {
    private Integer batchNumber;
    private String sectionName;
    private String warehouseName;
    private List<ProductResponseDTO> products;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class ProductResponseDTO {
        private String name;
    }
}
