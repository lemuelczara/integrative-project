package com.meli.bootcamp.integrativeproject.controller;

import com.meli.bootcamp.integrativeproject.dto.response.FindBatchesBySellerIdResponseDTO;
import com.meli.bootcamp.integrativeproject.service.BatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fresh-products/")
public class BatchController {

    private BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/due-date")
    public ResponseEntity<Object> findBySectionName(@RequestParam(name = "sectionName") String sectionName,
                                                    @RequestParam(name = "numberOfDays") Integer numberOfDays, @RequestParam(name = "asc") String asc) {
        return ResponseEntity.ok().body(batchService.findAllBySectionName(sectionName, numberOfDays, asc));
    }

    @GetMapping("/batch")
    public ResponseEntity<List<FindBatchesBySellerIdResponseDTO>> findBatchesBySellerId(@RequestParam(name = "sellerId") Long sellerId,
                                                                                        @RequestHeader(name = "agentId") Long agentId) {
        return ResponseEntity.ok().body(batchService.findBatchesBySellerId(sellerId, agentId));
    }
}
