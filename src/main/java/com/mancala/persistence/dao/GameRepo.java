package com.mancala.persistence.dao;

import com.mancala.persistence.model.Game;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aran on 15/07/17.
 */
public interface GameRepo extends CrudRepository<Game, String> {
}
