package com.study.springboot.Controller;

import com.study.springboot.Dto.MemberJoinDto;
import com.study.springboot.Dto.MemberLoginDto;
import com.study.springboot.entity.Member;
import com.study.springboot.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class Membercontroller {
    final MemberRepository memberRepository; //생성자 주입
    //
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
    @PostMapping("/loginAction")
    @ResponseBody
    public String loginAction(@Valid MemberLoginDto dto,
                              BindingResult bindingResult,
                              HttpServletRequest request,
                              Model model){
        if ( bindingResult.hasErrors() ) {
            //DTO에 설정한 message값 가져온다.
            String detail = bindingResult.getFieldError().getDefaultMessage();
            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();
            System.out.println(detail + ":" + bindResultCode);
            return "<script>alert('"+ detail +"'); history.back();</script>";
        }
        System.out.println( dto.getUser_id() );
        System.out.println( dto.getUser_pw() );

        //로그인 액션 처리 : 실제는 DB에 쿼리를 던진다.
        List<Member> list = memberRepository.findByUserIdAndUserPW(dto.getUser_id(), dto.getUser_pw());
        HttpStatus status = HttpStatus.OK;
        if (list.isEmpty()){
            status = HttpStatus.NOT_FOUND;
        }else{
            status = HttpStatus.OK;
        }
        if (status == HttpStatus.OK){
            System.out.println("로그인 성공");

            Member entity = list.get(0);
            //로그아웃시까지 로그인한 회원정보(Member테이블)을 가지고 있다.
            request.getSession().setAttribute("memberEntity", entity);

            //세션객체에 로그인성공값 저장(로그아웃시까지 저장됨.)
            request.getSession().setAttribute("isLogin",true);
            request.getSession().setAttribute("user_id",dto.getUser_id());
            request.getSession().setAttribute("user_role", entity.getUser_role());
            return "<script>alert('로그인 성공'); location.href='/';</script>";
        }else{
            return "<script>alert('로그인 실패'); history.back();</script>";
        }

    }
    @GetMapping("/logoutAction")
    @ResponseBody
    public String logoutAction(HttpServletRequest request){
        //세션 종료
        request.getSession().invalidate();

        return "<script>alert('로그아웃 되었습니다.'); location.href='/';</script>";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/joinAction")
    @ResponseBody
    public String joinAction(@Valid MemberJoinDto dto, BindingResult bindingResult) {
        if( bindingResult.hasErrors() ) {
            //DTO에 설정한 message값을 가져온다.
            String detail = bindingResult.getFieldError().getDefaultMessage();
            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();
            System.out.println( detail + ":" + bindResultCode);
            return "<script>alert('"+ detail +"'); history.back();</script>";
        }

        System.out.println( dto.getUser_id() );
        System.out.println( dto.getUser_pw() );

        try {
            Member entity = dto.toUpdateEntity();
            memberRepository.save( entity );
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("회원가입 실패");
            return "<script>alert('회원가입 실패'); histroy.back();</script>";
        }
        System.out.println("회원가입 성공!");
        return "<script>alert('회원가입 성공!'); location.href='/loginForm';</script>";

        //회원가입 DB 액션 수행
        //기존에 같은 아이디의 회원이 있는지 중복체크 해야 됨.
        //select * from member where user_id='';
        //insert into member user_id = '', user_pw = '';
        //지금은 무조건 성공 시킴.
//        HttpStatus status = HttpStatus.OK;
//        if( status == HttpStatus.OK ) {
//            System.out.println("회원가입 성공!");
//            return "<script>alert('회원가입 성공!'); location.href='/loginForm';</script>";
//        }else{
//            return "<script>alert('회원가입 실패'); histroy.back();</script>";
//        }
    }
    @GetMapping
    public String admin( Model model ){
        long listCount = memberRepository.count();
        model.addAttribute("listCount", listCount);

        List<Member> list = memberRepository.findAll();
        model.addAttribute("list", list);

        return "admin";

    }
    @RequestMapping("/viewDto")
    public String viewDTO(@RequestParam("id") int id,
                          Model model) throws Exception{
        Optional<Member> optional = memberRepository.findById(Long.valueOf(id));
        if (!optional.isPresent()){
            throw new Exception("member id is wrong");
        }
        Member entity = optional.get();
        model.addAttribute("member", entity);
        return "modifyForm";
    }
    @RequestMapping("/modifyAction")
    @ResponseBody
    public String modifyAction(MemberJoinDto memberJoinDto)  {
        try {
            Member entity = memberJoinDto.toUpdateEntity();
            memberRepository.save( entity );
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return "<script>alert('회원정보 수정 실패'); history.back();</script>";
        }


        return "<script>alert('회원정보 수정 성공'); location.href='/viewDto?id="+ memberJoinDto.getId()+"';</script>";
    }
    @RequestMapping("/deleteDTO")
    @ResponseBody
    public String deleteDTO(@RequestParam("id") int id)throws Exception {
        Optional<Member> optional = memberRepository.findById(Long.valueOf(id));
        if( !optional.isPresent()) {
            throw new Exception("member id is wrong");
        }
        Member entity = optional.get();
        try {
            memberRepository.delete( entity );
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return "<script>alert('회원정보 삭제 실패'); history.back();</script>";
        }


        return "<script>alert('회원정보 삭제 성공'); location.href='/admin';</script>";
    }

    //응답은 문자열(또는 Json)으로 내려준다. html파일로 응답은 안됨.
    @GetMapping("idDoubleCheckAjax")
    @ResponseBody
    public String idDoubleCheckAjax(@RequestParam("user_id") String user_id) {
        List<Member> list = memberRepository.findByUserId(user_id);
        if( list.size() == 0 ){
            return "0"; //0 중복안됨
        }else{
            return "1"; //1 중복됨
        }


    }
}
