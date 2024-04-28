package com.example.xujie.controller;

import com.example.xujie.dto.MemberStatisticsDTO;
import com.example.xujie.dto.OrderDTO;
import com.example.xujie.dto.ResultDTO;
import com.example.xujie.entity.mysql.MemberStatistics;
import com.example.xujie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController<T> {


    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    /**
     * 會員可以訂購產品。
     */
    @PostMapping("/{memberId}/createOrder")
    public ResultDTO<OrderDTO> createOrder(
            @PathVariable Long memberId,
            @RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(memberId, orderDTO);
    }
    /**
     * 會員可以根據訂單編號或產品名稱或購買日期做分頁查詢。
     */
    @GetMapping("/getOrders")
    public Page<OrderDTO> getOrders(
            @RequestParam(name = "orderNumber", required = false) String orderNumber,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "purchaseDate", required = false) LocalDate purchaseDate,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return orderService.findOrders(orderNumber, productName, purchaseDate, pageable);
    }

    /**
     * 統計訂單數大於n的會員資料。
     */
    @GetMapping("/getOrderStatistics")
    public ResultDTO<List<MemberStatisticsDTO>> getOrderStatistics(Integer n) {
        return orderService.getOrderStatistics(n);
    }

}
