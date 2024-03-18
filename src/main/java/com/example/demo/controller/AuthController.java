package com.example.demo.controller;
import com.example.demo.request.SignUpRequest;
import com.example.demo.response.Resp;
import com.example.demo.service.user.UserService;
import com.example.demo.table.JwtResponse;
import com.example.demo.table.User;
import com.example.demo.service.JwtService;
import com.example.demo.utils.Constants;
import com.example.demo.utils.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Resp> login(@RequestBody User user) {
        Resp resp = new Resp();
        try {
            resp.setData(userService.login(user));
            resp.setStatusCode(Constants.RESP_SUCC);
            resp.setMsg("Thành công");
        } catch (Exception e) {
            resp.setStatusCode(Constants.RESP_FAIL);
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<Resp> register( @RequestBody SignUpRequest user) {
        Resp resp = new Resp();
        try {
            resp.setData(userService.register(user));
            resp.setStatusCode(Constants.RESP_SUCC);
            resp.setMsg("Thành công");
        } catch (Exception e) {
            resp.setStatusCode(Constants.RESP_FAIL);
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/rest-pass")
    public ResponseEntity<Resp> editpass(@RequestBody SignUpRequest user) {
        Resp resp = new Resp();
        try {
            resp.setData(userService.changePassword(user));
            resp.setStatusCode(Constants.RESP_SUCC);
            resp.setMsg("Thành công");
        } catch (Exception e) {
            resp.setStatusCode(Constants.RESP_FAIL);
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping(Path.UPDATE)
    public ResponseEntity<Resp> update(@RequestBody SignUpRequest user) {
        Resp resp = new Resp();
        try {
            resp.setData(userService.update(user));
            resp.setStatusCode(Constants.RESP_SUCC);
            resp.setMsg("Thành công");
        } catch (Exception e) {
            resp.setStatusCode(Constants.RESP_FAIL);
            resp.setMsg(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
