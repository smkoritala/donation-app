package com.snowbird.donations.security.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DevTokenRequest {

	
	// Class for dev environment only till auth module is integrated.
	
	
	
    private String userId;
    private String name;
    private String email;
    private List<String> roles;
}
