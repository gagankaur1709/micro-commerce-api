package com.gagan.commerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gagan.commerce.controller.OrderController;
import com.gagan.commerce.domain.Order;
import com.gagan.commerce.domain.ShippingType;
import com.gagan.commerce.exception.OrderNotFoundException;
import com.gagan.commerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    public void whenCreateOrder_thenReturns201Created() throws Exception {
        Order orderRequest = Order.builder()
                .customer_name("Gagan")
                .base_price(new BigDecimal("100.0"))
                .zipCode("90210")
                .build();

        Order expectedResponse = Order.builder()
                .id(1L)
                .customer_name("Gagan")
                .base_price(new BigDecimal("100.0"))
                .total(new BigDecimal("125.0"))
                .zipCode("90210")
                .build();

        when(orderService.createOrder(any(Order.class), eq(ShippingType.EXPRESS)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/order?shippingType=EXPRESS")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customer_name").value("Gagan"))
                .andExpect(jsonPath("$.total").value(125.0));
    }

    @Test
    public void givenOrderExists_whenGetOrderById_thenReturns200OK() throws Exception {
        Order order = Order.builder()
                .id(1L)
                .customer_name("Gagan")
                .total(new BigDecimal("125.0"))
                .build();

        when(orderService.getOrderForId(1L)).thenReturn(order);


        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customer_name").value("Gagan"));
    }

    @Test
    public void givenOrderDoesNotExist_whenGetOrderById_thenReturns404NotFound() throws Exception {
        when(orderService.getOrderForId(99L))
                .thenThrow(new OrderNotFoundException("Order not found with id: 99"));

        mockMvc.perform(get("/api/order/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenOrderExists_whenUpdateOrder_thenReturns200OK() throws Exception {
        Order orderDetails = Order.builder()
                .customer_name("Gagan Updated")
                .total(new BigDecimal("200.0"))
                .build();

        Order updatedOrder = Order.builder()
                .id(1L)
                .customer_name("Gagan Updated")
                .total(new BigDecimal("200.0"))
                .build();

        when(orderService.editOrderForId(eq(1L), any(Order.class)))
                .thenReturn(updatedOrder);

        mockMvc.perform(post("/api/order/1")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(orderDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer_name").value("Gagan Updated"))
                .andExpect(jsonPath("$.total").value(200.0));
    }

    @Test
    public void givenOrderExists_whenDeleteOrder_thenReturns204NoContent() throws Exception {
        doNothing().when(orderService).deleteOrderForId(1L);
        mockMvc.perform(delete("/api/order/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenGetAllOrders_thenReturns200OKAndListOfOrders() throws Exception {
        Order order1 = Order.builder().id(1L).customer_name("Gagan").build();
        Order order2 = Order.builder().id(2L).customer_name("Mona").build();

        when(orderService.getAllOrder()).thenReturn(List.of(order1, order2));

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].customer_name").value("Gagan"))
                .andExpect(jsonPath("$[1].customer_name").value("Mona"));
    }
}
