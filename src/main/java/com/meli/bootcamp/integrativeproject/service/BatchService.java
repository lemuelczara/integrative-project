package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.dto.response.BatchSectionNameResponse;
import com.meli.bootcamp.integrativeproject.dto.response.BatchStock;
import com.meli.bootcamp.integrativeproject.dto.response.FindBatchesBySellerIdResponseDTO;
import com.meli.bootcamp.integrativeproject.entity.Batch;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.repositories.BatchRepository;
import com.meli.bootcamp.integrativeproject.repositories.BatchRepository.BatchResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchService {

    private BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public List<FindBatchesBySellerIdResponseDTO> findBatchesBySellerId(Long sellerId) {
        List<FindBatchesBySellerIdResponseDTO> listOfBatchesBySellerId = new ArrayList<>();

        List<Batch> batches = batchRepository.findAllBySellerId(sellerId);

        batches.stream().forEach(batch -> {
            var listOfProductsInTheBatch = batch.getProducts().stream().map(product -> {
                var productInTheBatch = FindBatchesBySellerIdResponseDTO.ProductResponseDTO.builder()
                        .name(product.getName())
                        .build();

                return productInTheBatch;
            }).collect(Collectors.toList());

            var batchBySellerId = FindBatchesBySellerIdResponseDTO.builder()
                    .batchNumber(batch.getBatchNumber())
                    .sectionName(batch.getSection().getCategory().name())
                    .warehouseName(batch.getWarehouse().getName())
                    .products(listOfProductsInTheBatch)
                    .build();

            listOfBatchesBySellerId.add(batchBySellerId);
        });

        return listOfBatchesBySellerId;
    }

    public BatchSectionNameResponse findAllBySectionName(String sectionName, Integer numberOfDays, String asc) {
        ascResponse(asc.toLowerCase());

        List<BatchResponse> batchResponseList = batchRepository.findAllBySectionNameAndDueDate(responseSectionName(sectionName));
        List<BatchStock> batchStockList = new ArrayList<>();

        if (numberOfDays < 0)
            throw new BusinessException("number of days cannot be less than 0");

        batchResponseList.stream().forEach(batchResponse -> {
            if (batchResponse.getDatediff() >= 0 && batchResponse.getDatediff() <= numberOfDays) {
                BatchStock batchStock = BatchStock.builder()
                        .batchNumber(batchResponse.getBatch_number())
                        .productId(batchResponse.getProduct_id())
                        .productTypeId(batchResponse.getProduct_category())
                        .dueDate(batchResponse.getDue_date())
                        .quantity(batchResponse.getQuantity())
                        .build();
                batchStockList.add(batchStock);
            }
        });

        if (asc.equals("true"))
            batchStockList.sort(Comparator.comparing(BatchStock::getDueDate));

        return BatchSectionNameResponse.builder()
                .batchStock(batchStockList).build();
    }

    public String responseSectionName(String sectionName) {
        sectionName = sectionName.toUpperCase();
        switch (sectionName) {
            case "FS":
                sectionName = "FRESCO";
                break;
            case "RF":
                sectionName = "REFRIGERADO";
                break;
            case "FF":
                sectionName = "CONGELADO";
                break;
            default:
                throw new BusinessException("Section " + sectionName + " not found");
        }
        return sectionName;
    }

    public void ascResponse(String asc) throws BusinessException {
        if (asc == null | asc.equals(""))
            asc = "false";

        if (!(asc.equals("true")) & !(asc.equals("false"))) {
            throw new BusinessException("asc can only be true or false");
        }

    }
}
