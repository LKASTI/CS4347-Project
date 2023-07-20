package com.partytruckservice.JdbcItemRepository;

import com.partytruckservice.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public class JdbcItemRepository implements ItemRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public int count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM item", Integer.class);
    }

    @Override
    public int save(Item item){
        return jdbcTemplate.update(
            "INSERT INTO item (name, manufacturer, stock, price) VALUES(?,?,?,?)",
            item.getName(), item.getManufacturer(), item.getStock(), item.getPrice()
        );
    }

    @Override
    public int updateItemStock(Item item){
        return jdbcTemplate.update(
          "UPDATE item SET stock = ? WHERE itemId = ?",
            item.getStock(), item.getItemID()
        );
    }

    @Override
    public int deleteById(int id){
        return jdbcTemplate.update(
            "DELETE item WHERE itemId = ?",
            id
        );
    }

    @Override
    public List<Item> findAll(){
        return jdbcTemplate.query(
            "SELECT * FROM item",
            (rs, rowNum) -> 
                new Item(
                    rs.getInt("itemId"),
                    rs.getString("name"),
                    rs.getString("manufacturer"),
                    rs.getFloat("price"),
                    rs.getInt("stock")
                )
        );
    }

    @Override
    public List<Item> findByName(String name){
        return jdbcTemplate.queryForList(
            "SELECT * FROM item WHERE name LIKE ?",
            String.class,
            "%" + name + "%"
        );
    }

    @Override
    public String getNameById(int id){
        return jdbcTemplate.query(
            "SELECT name FROM item WHERE id = ?",
            id
        );
    }

}
