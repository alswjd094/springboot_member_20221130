package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Long save(MemberDTO memberDTO) {

//        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
//        memberRepository.save(memberEntity);

       Long savedId = memberRepository.save(MemberEntity.toSaveEntity(memberDTO)).getId();
        return savedId;
    }
    public MemberDTO login(MemberDTO memberDTO){
//email로 DB에서 조회를 하고
//사용자가 입력한 비밀번호와 DB에서 조회한 비밀번호가일치하는지 판단해서 성공, 실패 여부 리턴
// 단. email 조회결과가 없을 때에도 실패
        Optional<MemberEntity> optionalEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(optionalEntity.isPresent()){
            MemberEntity memberEntity = optionalEntity.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
//                MemberDTO memberDTO1 = MemberDTO.toDTO(memberEntity);
//                return memberDTO1;
                return MemberDTO.toDTO(memberEntity);
            }else {
                return null;
            }
         }else {
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity:memberEntityList){
//            MemberDTO memberDTO = MemberDTO.toDTO(memberEntity);
//            memberDTOList.add(memberDTO);
            memberDTOList.add(MemberDTO.toDTO(memberEntity));
    }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
//            MemberEntity memberEntity = optionalMemberEntity.get();
//            MemberDTO memberDTO = MemberDTO.toDTO(memberEntity);
//            return memberDTO;
            return MemberDTO.toDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

    public MemberDTO findByMemberEmail(String loginEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(loginEmail);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toDTO(optionalMemberEntity.get());
        }else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
       MemberEntity updateEntity = MemberEntity.toUpdateEntity(memberDTO);
        memberRepository.save(updateEntity);
        //save method: Entity의 id값이 없으면 insert, id값 있으면 update
        // toSaveEntity에는 id값 X, update용 변환 Entity 필요
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }


    public String emailDuplicateCheck(String memberEmail) {
       Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
       if(optionalMemberEntity.isEmpty()){
           return"ok";
       }else {
           return null;
       }
    }
}
