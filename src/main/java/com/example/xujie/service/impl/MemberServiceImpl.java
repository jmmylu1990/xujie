package com.example.xujie.service.impl;

import com.example.xujie.dto.MemberDTO;
import com.example.xujie.dto.ResultDTO;
import com.example.xujie.entity.mysql.Members;
import com.example.xujie.enums.ResultEnum;
import com.example.xujie.repository.mysql.MemberRepository;
import com.example.xujie.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<MemberDTO> addMember(Members member) {
        ResultDTO<MemberDTO> resultDTO = new ResultDTO<>();
        try {
            Members memberResponse = memberRepository.save(member);
            MemberDTO memberDTO = this.convertToMemberDTO(member);
            resultDTO.setData(memberDTO);
            resultDTO.setSuccess(true);
            resultDTO.setCode(ResultEnum.SUCCESS.getCode());
            resultDTO.setMsg(ResultEnum.SUCCESS.getMessage());
            log.info("Member added successfully with ID: {}", memberResponse.getId());
            return resultDTO;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setData(null);
            resultDTO.setSuccess(false);
            resultDTO.setCode(ResultEnum.SYSTEM_FAILURE.getCode());
            resultDTO.setMsg(ResultEnum.SYSTEM_FAILURE.getMessage());
            log.error("Error occurred while adding member", e);
            return resultDTO;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<MemberDTO> deleteById(Long memberId) {
        ResultDTO<MemberDTO> resultDTO = new ResultDTO<>();
        try {
            memberRepository.deleteById(memberId);
            resultDTO.setSuccess(true);
            resultDTO.setCode(ResultEnum.SUCCESS.getCode());
            resultDTO.setMsg(ResultEnum.SUCCESS.getMessage());
            return resultDTO;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setData(null);
            resultDTO.setSuccess(false);
            resultDTO.setCode(ResultEnum.SYSTEM_FAILURE.getCode());
            resultDTO.setMsg(ResultEnum.SYSTEM_FAILURE.getMessage());
            log.error("Error occurred while deleting member with ID:{}, exception message:{}", memberId, e.getMessage());
            return resultDTO;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDTO<MemberDTO> updateMember(Long memberId, Members updatedMember) {
        ResultDTO<MemberDTO> resultDTO = new ResultDTO<>();
        try {
            Optional<Members> optionalMember = memberRepository.findById(memberId);
            if (optionalMember.isPresent()) {
                Members existingMember = optionalMember.get();
                existingMember.setName(updatedMember.getName());
                existingMember.setEmail(updatedMember.getEmail());
                Members member = memberRepository.save(existingMember);
                MemberDTO memberDTO = this.convertToMemberDTO(member);
                resultDTO.setData(memberDTO);
                resultDTO.setSuccess(true);
                resultDTO.setCode(ResultEnum.SUCCESS.getCode());
                resultDTO.setMsg(ResultEnum.SUCCESS.getMessage());

            } else {
                //如果找不到對應的會員，返回一個合適的錯誤消息
                resultDTO.setData(null);
                resultDTO.setSuccess(false);
                resultDTO.setCode(ResultEnum.NOT_FOUND.getCode());
                resultDTO.setMsg("Member not found with id: " + memberId);
            }
            return resultDTO;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setData(null);
            resultDTO.setSuccess(false);
            resultDTO.setCode(ResultEnum.SYSTEM_FAILURE.getCode());
            resultDTO.setMsg(ResultEnum.SYSTEM_FAILURE.getMessage());
            log.error("Error occurred while updating member with ID:{}, exception message:{}", memberId, e.getMessage());
            return resultDTO;
        }
    }

    @Override
    public ResultDTO<MemberDTO> getMemberById(Long memberId) {
        ResultDTO<MemberDTO> resultDTO = new ResultDTO<>();

        try {
            Optional<Members> optionalMember = memberRepository.findById(memberId);
            if (optionalMember.isPresent()) {
                Members member = optionalMember.get();
                MemberDTO memberDTO = this.convertToMemberDTO(member);
                resultDTO.setData(memberDTO);
                resultDTO.setSuccess(true);
                resultDTO.setCode(ResultEnum.SUCCESS.getCode());
                resultDTO.setMsg(ResultEnum.SUCCESS.getMessage());
            } else {
                resultDTO.setData(null);
                resultDTO.setSuccess(false);
                resultDTO.setCode(ResultEnum.NOT_FOUND.getCode());
                resultDTO.setMsg("Member not found with id: " + memberId);
            }
        } catch (Exception e) {
            resultDTO.setData(null);
            resultDTO.setSuccess(false);
            resultDTO.setCode(ResultEnum.SYSTEM_FAILURE.getCode());
            resultDTO.setMsg(ResultEnum.SYSTEM_FAILURE.getMessage());
            log.error("Error occurred while retrieving member with ID: {}, exception message: {}", memberId, e.getMessage());
        }

        return resultDTO;
    }


    @Override
    public ResultDTO<Page<MemberDTO>> getAllMembers(Pageable pageable) {
        ResultDTO<Page<MemberDTO>> resultDTO = new ResultDTO<>();
        try {
            Page<Members> membersPage = memberRepository.findAll(pageable);
            if (membersPage.isEmpty()) {
                resultDTO.setData(Page.empty());
                resultDTO.setSuccess(true);
                resultDTO.setCode(ResultEnum.NO_CONTENT.getCode());
                resultDTO.setMsg("No members found");
            } else {
                Page<MemberDTO> memberDTOPage = membersPage.map(this::convertToMemberDTO);
                resultDTO.setData(memberDTOPage);
                resultDTO.setSuccess(true);
                resultDTO.setCode(ResultEnum.SUCCESS.getCode());
                resultDTO.setMsg(ResultEnum.SUCCESS.getMessage());
            }
        } catch (Exception e) {
            resultDTO.setData(null);
            resultDTO.setSuccess(false);
            resultDTO.setCode(ResultEnum.SYSTEM_FAILURE.getCode());
            resultDTO.setMsg(ResultEnum.SYSTEM_FAILURE.getMessage());
            log.error("Error occurred while retrieving all members, exception message: {}", e.getMessage());
        }
        return resultDTO;
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
}
