package com.lilybookclub.dto.response.user;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SignUpResponse {
     private String email;
     private String firstname;
     private String lastname;
}
