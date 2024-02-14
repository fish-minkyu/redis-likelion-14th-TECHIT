package com.example.redis;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// 인증 외 상황(장바구니 등)에서 세션 정보를 저장하기 위한 Controller
@Slf4j
@RestController
@RequestMapping("/session")
public class SessionController {
  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setSession(
    @RequestParam("key") String key,
    @RequestParam("value") String value,
    // Key - Value로 이뤄져 있음
    HttpSession session
  ) {
    session.setAttribute(key, value);
  }

  @GetMapping
  public String getSession(
    @RequestParam("key") String key,
    HttpSession session
  ) {
    // setAttribute를 Object형으로 저장하였기에 Object로 받아준다.
    Object value = session.getAttribute(key);
    if (value == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    if (!(value instanceof String))
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

    return value.toString();
  }
}
