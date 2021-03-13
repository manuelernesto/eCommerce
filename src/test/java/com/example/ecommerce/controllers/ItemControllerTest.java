package com.example.ecommerce.controllers;

import com.example.ecommerce.TestUtils;
import com.example.ecommerce.model.persistence.Item;
import com.example.ecommerce.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController(null);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("Java Web Book");
        BigDecimal price = BigDecimal.valueOf(7.19);
        item.setPrice(price);
        item.setDescription("Time to refresh to knowledge");

        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepo.findByName("Java Web Book")).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void get_all_items_Test() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }


    @Test
    public void get_item_by_id_Test() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item i = response.getBody();
        assertNotNull(i);
    }

    @Test
    public void get_item_by_id_not_found_Test() {
        ResponseEntity<Item> response = itemController.getItemById(2L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name_Test() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Java Web Book");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void get_items_by_name_not_found_Test() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Java Web");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
