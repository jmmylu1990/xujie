package com.example.xujie.service;

import com.example.xujie.dto.MemberDTO;
import com.example.xujie.dto.ResultDTO;
import com.example.xujie.entity.mysql.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface MemberService {
    ResultDTO<MemberDTO> addMember(Members member);
    ResultDTO<MemberDTO> deleteById(Long memberId);
    ResultDTO<MemberDTO>  updateMember(Long memberId, Members updatedMember);
    ResultDTO<MemberDTO> getMemberById(Long memberId);

    ResultDTO<Page<MemberDTO>> getAllMembers(Pageable pageable);
}
