package com.example.xujie.controller;

import com.example.xujie.dto.MemberDTO;
import com.example.xujie.dto.ResultDTO;
import com.example.xujie.entity.mysql.Members;
import com.example.xujie.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 新增會員
     */
    @PostMapping("/addMember")
    public ResultDTO<MemberDTO> addMember(@Valid @RequestBody Members member) {
        return memberService.addMember(member);
    }
    /**
     * 刪除會員
     */
    @DeleteMapping("/DeleteMember/{id}")
    public ResultDTO<MemberDTO> deleteById(@PathVariable("id") Long memberId) {
        return memberService.deleteById(memberId);
    }

    /**
     * 修改會員資訊
     */
    @PatchMapping("/updateMember/{id}")
    public ResultDTO<MemberDTO> updateMember(@PathVariable("id") Long memberId, @Valid @RequestBody Members updatedMember) {
        return memberService.updateMember(memberId, updatedMember);
    }

    /**
     * 查詢單一會員
     */
    @GetMapping("/{id}")
    public ResultDTO<MemberDTO> getMemberById(@PathVariable("id") Long memberId) {
        return memberService.getMemberById(memberId);
    }

    /**
     * 分頁查詢所有會員
     */
    @GetMapping("/page")
    public ResultDTO<Page<MemberDTO>> getAllMembers(Pageable pageable) {
        return memberService.getAllMembers(pageable);
    }
}
