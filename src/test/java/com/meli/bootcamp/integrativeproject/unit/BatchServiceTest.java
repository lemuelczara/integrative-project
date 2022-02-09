package com.meli.bootcamp.integrativeproject.unit;

import com.meli.bootcamp.integrativeproject.entity.*;
import com.meli.bootcamp.integrativeproject.enums.Category;
import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import com.meli.bootcamp.integrativeproject.mocks.BatchServiceMocks;
import com.meli.bootcamp.integrativeproject.repositories.AgentRepository;
import com.meli.bootcamp.integrativeproject.repositories.BatchRepository;
import com.meli.bootcamp.integrativeproject.repositories.SellerRepository;
import com.meli.bootcamp.integrativeproject.service.BatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class BatchServiceTest {

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private AgentRepository agentRepository;

    private BatchService service;

    private String validSectionName = "FF";
    private Integer validNumberOfDays = 1;
    private String validAscParameter = "true";

    private Product fakeProduct;

    private Warehouse fakeWarehouse;

    private Batch fakeBatch;

    private Section fakeSection;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);

        fakeProduct = BatchServiceMocks.makeFakeProduct();
        fakeWarehouse = BatchServiceMocks.makeFakeWarehouse();
        fakeBatch = BatchServiceMocks.makeFakeBatch();
        fakeSection = BatchServiceMocks.makeFakeSection();

        service = new BatchService(batchRepository, sellerRepository, agentRepository);
    }

    @Test
    public void shouldBeThrowIfAscParameterNotBeTrueOrFalse() {
        String invalidAscParameter = "INVALID_ASC_PARAMETER";

        BusinessException exception = assertThrows(BusinessException.class, () ->
                service.findAllBySectionName(validSectionName, validNumberOfDays, invalidAscParameter));

        assertEquals("asc can only be true or false", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfSectionNameNotFound() {
        String invalidSectionName = "INVALID_SECTION_NAME";

        BusinessException exception = assertThrows(BusinessException.class, () ->
                service.findAllBySectionName(invalidSectionName, validNumberOfDays, validAscParameter));

        assertEquals("Section " + invalidSectionName + " not found", exception.getMessage());
    }

    @Test
    public void shouldBeThrowIfNumberOfDaysLessThan0() {
        Integer invalidNumberOfDays = -1;

        BusinessException exception = assertThrows(BusinessException.class, () ->
                service.findAllBySectionName(validSectionName, invalidNumberOfDays, validAscParameter));

        assertEquals("number of days cannot be less than 0", exception.getMessage());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsFilteredByFrescoSectionNameOrderedByAscDueDate() {
        when(batchRepository.findAllBySectionNameAndDueDate(anyString())).thenReturn(BatchServiceMocks.makeFakeListOfBatchResponseWithFrescoSection());

        validSectionName = "FS";

        validAscParameter = "true";
        validNumberOfDays = 28;
        var response1 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "false";
        validNumberOfDays = 28;
        var response2 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "";
        validNumberOfDays = 21;
        var response3 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        assertEquals(2, response1.getBatchStock().size());
        assertEquals(6, response1.getBatchStock().get(0).getProductId());

        assertEquals(2, response2.getBatchStock().size());
        assertEquals(5, response2.getBatchStock().get(0).getProductId());

        assertEquals(1, response3.getBatchStock().size());
        assertEquals(6, response3.getBatchStock().get(0).getProductId());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsFilteredByCongeladoSectionNameOrderedByAscDueDate() {
        when(batchRepository.findAllBySectionNameAndDueDate(anyString())).thenReturn(BatchServiceMocks.makeFakeListOfBatchResponseWithCongeladoSection());

        validSectionName = "FF";

        validAscParameter = "true";
        validNumberOfDays = 9;
        var response1 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "false";
        validNumberOfDays = 9;
        var response2 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "";
        validNumberOfDays = 3;
        var response3 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        assertEquals(2, response1.getBatchStock().size());
        assertEquals(2, response1.getBatchStock().get(0).getProductId());

        assertEquals(2, response2.getBatchStock().size());
        assertEquals(1, response2.getBatchStock().get(0).getProductId());

        assertEquals(1, response3.getBatchStock().size());
        assertEquals(2, response3.getBatchStock().get(0).getProductId());
    }

    @Test
    public void shouldBeReturnsListOfAllProductsFilteredByRefrigeradoSectionNameOrderedByAscDueDate() {
        when(batchRepository.findAllBySectionNameAndDueDate(anyString())).thenReturn(BatchServiceMocks.makeFakeListOfBatchResponseWithRefrigeradoSection());

        validSectionName = "RF";

        validAscParameter = "true";
        validNumberOfDays = 16;
        var response1 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "false";
        validNumberOfDays = 16;
        var response2 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        validAscParameter = "";
        validNumberOfDays = 15;
        var response3 = service.findAllBySectionName(validSectionName, validNumberOfDays, validAscParameter);

        assertEquals(2, response1.getBatchStock().size());
        assertEquals(4, response1.getBatchStock().get(0).getProductId());

        assertEquals(2, response2.getBatchStock().size());
        assertEquals(3, response2.getBatchStock().get(0).getProductId());

        assertEquals(1, response3.getBatchStock().size());
        assertEquals(4, response3.getBatchStock().get(0).getProductId());
    }

    @Test
    public void shouldBeThrowsIfAgentNotFound() {
        var validSellerId = 1L;
        var invalidAgentId = 999L;

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                service.findBatchesBySellerId(validSellerId, invalidAgentId));

        assertEquals("Agent not found!", exception.getMessage());
    }

    @Test
    public void shouldBeThrowsIfSellerNotFound() {
        var invalidSellerId = 999L;
        var validAgentId = 1L;

        when(agentRepository.findById(anyLong())).thenReturn(Optional.of(new Agent()));

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                service.findBatchesBySellerId(invalidSellerId, validAgentId));

        assertEquals("Seller not found!", exception.getMessage());
    }

    @Test
    public void shouldBeReturnsListOfBatchesFilteredBySellerId() {
        var validSellerId = 1L;
        var validAgentId = 1L;

        fakeSection.setCategory(Category.CONGELADO);

        fakeWarehouse.setName("Armazém de São Paulo");

        fakeProduct.setName("Frango");

        fakeBatch.setSection(fakeSection);
        fakeBatch.setWarehouse(fakeWarehouse);
        fakeBatch.setProducts(Arrays.asList(fakeProduct));

        when(agentRepository.findById(anyLong())).thenReturn(Optional.of(new Agent()));
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(new Seller()));
        when(batchRepository.findAllBySellerId(anyLong())).thenReturn(Arrays.asList(fakeBatch));

        var response = service.findBatchesBySellerId(validSellerId, validAgentId);

        assertEquals(12345, response.get(0).getBatchNumber());
        assertEquals("CONGELADO", response.get(0).getSectionName());
        assertEquals("Armazém de São Paulo", response.get(0).getWarehouseName());
        assertEquals("Frango", response.get(0).getProducts().get(0).getName());
    }
}
