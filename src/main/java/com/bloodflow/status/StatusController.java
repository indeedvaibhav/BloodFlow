package com.bloodflow.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class StatusController {
	
	@GetMapping("/api/v1/status")
	public StatusResponse getStatus() {
		return new StatusResponse("UP","bloodflow");
	}
	public record StatusResponse(String status, String service) {}
}
