package com.example.xujie.service;

import com.example.xujie.dto.MemberStatisticsDTO;
import com.example.xujie.dto.OrderDTO;
import com.example.xujie.dto.ResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    ResultDTO<OrderDTO> createOrder(Long memberId, OrderDTO orderDTO);
    Page<OrderDTO> findOrders(String orderNumber, String productName, LocalDate purchaseDate, Pageable pageable);
    ResultDTO<List<MemberStatisticsDTO>> getOrderStatistics(Integer n);
}
