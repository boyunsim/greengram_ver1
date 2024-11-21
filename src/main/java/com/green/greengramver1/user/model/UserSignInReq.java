package com.green.greengramver1.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(title = "로그인")
public class UserSignInReq {
    @Schema(title = "아이디", example = "asdf", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uid;
    @Schema(title = "비밀번호", example = "asdf", requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;
}
