package com.example.redis;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/session")
public class SessionController {
  @PutMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setSession(
    @RequestParam("key") String key,
    @RequestParam("value") String value,
    HttpSession session
  ) {
    session.setAttribute(key, value);
  }

  @GetMapping
  public String getSession(
    @RequestParam("key") String key,
    HttpSession session
  ) {
    Object value = session.getAttribute(key);
    if (value == null)
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    if (!(value instanceof String))
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

    return value.toString();
  }
}
