package com.example.bootcamp.controller;

import com.example.bootcamp.utility.BigONotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
// @PreAuthorize(RoleConstants.ADMIN_CRUD)
public class AdminController {

  @Autowired BigONotation bigONotation;

  @GetMapping("/get-report")
  // @PreAuthorize(RoleConstants.ADMIN_CRUD)
  public String getActiveProfile() {
    return "Admin Details";
  }

  @GetMapping("/big-o")
  public String testBigONotation() {

    bigONotation.logarithmicTime(20);

    return "testing done";
  }
}
