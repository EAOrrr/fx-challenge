package com.fx.api.web;

import com.fx.api.model.AcceptingPayload;
import com.fx.api.service.AcceptingState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AcceptingState acceptingState;

    public AdminController(AcceptingState acceptingState) {
        this.acceptingState = acceptingState;
    }

    @GetMapping("/accepting")
    public AcceptingPayload getAccepting() {
        return new AcceptingPayload(acceptingState.isAccepting());
    }

    @PostMapping("/accepting")
    public AcceptingPayload setAccepting(@RequestBody AcceptingPayload payload) {
        return new AcceptingPayload(acceptingState.set(payload.accepting()));
    }
}