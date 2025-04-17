package in.newdevpoint.bootcamp.entity;

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
