package com.example.sbuser.external.services;

import com.example.sbuser.entity.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {

  @GetMapping("/hotels/{hotelId}")
  Hotel getHotel(@PathVariable("hotelId") String hotelId);
}
