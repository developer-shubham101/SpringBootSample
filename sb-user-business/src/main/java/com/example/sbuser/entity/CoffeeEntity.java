package com.example.sbuser.entity;

import java.util.ArrayList;
import lombok.Data;

@Data
public class CoffeeEntity {

  public String title;
  public String description;
  public ArrayList<String> ingredients;
  public String image;
  public int id;
}
