package com.pravo.pravo.domain.member.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "jwt")
@Tag(name = "Member", description = "멤버 관련 api")
public interface MemberApi {

}
