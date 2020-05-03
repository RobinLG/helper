package com.robin.life.helper.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * picture dao
 * @author Robin
 * @date 2020/5/2
 */
@Repository
public interface ImageDao {

    /**
     * saving picture to databse
     * @param param
     * @return int
     */
    int insertImage(Map<String, Object> param);
}
