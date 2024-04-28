package com.example.xujie.service.impl;

import com.example.xujie.dto.MemberDTO;
import com.example.xujie.dto.MemberStatisticsDTO;
import com.example.xujie.dto.OrderDTO;
import com.example.xujie.dto.ResultDTO;
import com.example.xujie.entity.mysql.Members;
import com.example.xujie.entity.mysql.MemberStatistics;
import com.example.xujie.entity.mysql.Orders;
import com.example.xujie.enums.ResultEnum;
import com.example.xujie.repository.mysql.MemberRepository;
import com.example.xujie.repository.mysql.MemberStatisticsRepository;
import com.example.xujie.repository.mysql.OrderRepository;
import com.example.xujie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final MemberStatisticsRepository memberStatisticsRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, OrderRepository orderRepository, MemberStatisticsRepository memberStatisticsRepository, ModelMapper modelMapper) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.memberStatisticsRepository = memberStatisticsRepository;
        this.modelMapper = modelMapper;
    }

    // 新增訂單
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<OrderDTO>createOrder(Long memberId, OrderDTO orderDTO) {
        ResultDTO<OrderDTO> resultDTO = new ResultDTO<>();
        try {
            Optional<Members> memberOptional = memberRepository.findById(memberId);
            // 如果找不到對應的會員，選擇拋出一個異常或返回一個預設值
            if (memberOptional.isPresent()) {
                Members member = memberOptional.get();
                Orders order = convertToOrder(orderDTO);
                ZoneId zoneId = ZoneId.of("Asia/Taipei");
                LocalDateTime localDateTime = LocalDateTime.now();
                order.setPurchaseDate(Date.from(localDateTime.atZone(zoneId).toInstant()));
                order.setMember(member);
                OrderDTO convertedDTO = this.convertToOrderDTO( orderRepository.save(order));
                resultDTO.setData(convertedDTO);
                resultDTO.setSuccess(true);
                resultDTO.setCode(ResultEnum.SUCCESS.getCode());
                resultDTO.setMsg(ResultEnum.SUCCESS.getMessage());
            } else {
                resultDTO.setData(null);
                resultDTO.setSuccess(false);
                resultDTO.setCode(ResultEnum.NOT_FOUND.getCode());
                resultDTO.setMsg("Member not found with id: " + memberId);
                return resultDTO;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setData(null);
            resultDTO.setSuccess(false);
            resultDTO.setCode(ResultEnum.SYSTEM_FAILURE.getCode());
            resultDTO.setMsg(ResultEnum.SYSTEM_FAILURE.getMessage());
            log.error("Error occurred while creating order for member with ID:{}, exception message:{}", memberId, e.getMessage());
            return resultDTO;
        }
        return resultDTO;
    }

    @Override
    public Page<OrderDTO> findOrders(String orderNumber, String productName, LocalDate purchaseDate, Pageable pageable) {
        LocalDateTime startOfDay = purchaseDate.atStartOfDay();
        Timestamp timestamp = Timestamp.valueOf(startOfDay);
        Page<Orders> orderEntities = orderRepository.searchOrders(
                orderNumber, productName, timestamp, pageable);

        List<OrderDTO> orderDTOList = orderEntities.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(orderDTOList, pageable, orderEntities.getTotalElements());
    }

    @Override
    public ResultDTO<List<MemberStatisticsDTO>> getOrderStatistics(Integer n) {
        ResultDTO<List<MemberStatisticsDTO>> resultDTO = new ResultDTO<>();
        try {
            List<MemberStatistics> statisticsList = memberStatisticsRepository.getMemberStatistics(n);
            if (!statisticsList.isEmpty()) {

               List<MemberStatisticsDTO> memberStatisticsDTOList = this.convertToOrderStatisticsDTOList(statisticsList);
                resultDTO.setData(memberStatisticsDTOList);
                resultDTO.setSuccess(true);
                resultDTO.setCode(ResultEnum.SUCCESS.getCode());
                resultDTO.setMsg(ResultEnum.SUCCESS.getMessage());
            }else {
                resultDTO.setData(null);
                resultDTO.setSuccess(false);
                resultDTO.setCode(ResultEnum.NOT_FOUND.getCode());
            }
            return resultDTO;

        }catch (Exception e){
            resultDTO.setData(null);
            resultDTO.setSuccess(false);
            resultDTO.setCode(ResultEnum.SYSTEM_FAILURE.getCode());
            resultDTO.setMsg(ResultEnum.SYSTEM_FAILURE.getMessage());
            log.error("Error occurred while getting order statistics for n={}, exception message: {}", n, e.getMessage());
            return resultDTO;
        }
    }

    private OrderDTO convertToOrderDTO(Orders order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(order.getOrderNumber());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setProductId(order.getProductId());
        orderDTO.setProductName(order.getProductName());
        orderDTO.setPurchaseDate(order.getPurchaseDate());
        return orderDTO;
    }
    private Orders convertToOrder(OrderDTO orderDTO) {
        Orders order = new Orders();
        order.setOrderNumber(orderDTO.getOrderNumber());
        order.setPrice(orderDTO.getPrice());
        order.setQuantity(orderDTO.getQuantity());
        order.setProductId(orderDTO.getProductId());
        order.setProductName(orderDTO.getProductName());
        return order;
    }

    private MemberDTO convertToMemberDTO(Members member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setAge(member.getAge());
        memberDTO.setGender(member.getGender());
        return memberDTO;
    }

    private OrderDTO convertToDTO(Orders order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(order.getOrderNumber());
        orderDTO.setProductId(order.getProductId());
        orderDTO.setProductName(order.getProductName());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setPurchaseDate(order.getPurchaseDate());
        return orderDTO;
    }

    private List<MemberStatisticsDTO> convertToOrderStatisticsDTOList(List<MemberStatistics> statisticsList) {
        return statisticsList.stream()
                .map(statistic -> modelMapper.map(statistic, MemberStatisticsDTO.class))
                .collect(Collectors.toList());
    }
}
