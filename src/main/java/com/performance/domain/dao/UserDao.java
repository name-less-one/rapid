package com.performance.domain.dao;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.performance.domain.entity.UserMaster;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;
    
    final String INSERT_QUERY = "INSERT INTO user_master (last_name, first_name, prefectures, city, blood_type, hobby1, hobby2, hobby3, hobby4, hobby5) VALUES (?,?,?,?,?,?,?,?,?,?);";
    
    final String SEARCH_USER_QUERY = "SELECT id, last_name, first_name, prefectures, city, blood_type, hobby1, hobby2, hobby3, hobby4, hobby5 FROM user_master WHERE last_name <> '試験';";
    
    final String SEARCH_TARGET_QUERY = "SELECT id, last_name, first_name, prefectures, city, blood_type, hobby1, hobby2, hobby3, hobby4, hobby5 FROM user_master WHERE last_name = '試験' AND first_name = '太郎';";
    
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUserMaster (List<Object[]> params) {
        jdbcTemplate.batchUpdate(INSERT_QUERY, params);
    }

    public List<UserMaster> searchUser(UserMaster targetUserMaster) {
        RowMapper<UserMaster> mapper = new BeanPropertyRowMapper<UserMaster>(UserMaster.class);
        return jdbcTemplate.query(SEARCH_USER_QUERY, mapper);
    }
    
    public UserMaster getTargetUser() {
        RowMapper<UserMaster> mapper = new BeanPropertyRowMapper<UserMaster>(UserMaster.class);
        return jdbcTemplate.queryForObject(SEARCH_TARGET_QUERY, mapper);
    }
    
    public int searchCount() {
        String sql = "SELECT COUNT(*) FROM user_master";
        
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void truncateUserInfo() {
        String sql = "TRUNCATE TABLE user_info";
        jdbcTemplate.execute(sql);
    }
    
    public void truncateUserHobby() {
        String sql = "TRUNCATE TABLE user_hobby";
        jdbcTemplate.execute(sql);
    }
    
    public void truncateUserMaster() {
        String sql = "TRUNCATE TABLE user_master";
        jdbcTemplate.execute(sql);
    }
}
