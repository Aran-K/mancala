package com.mancala.persistence.dao;

import com.mancala.persistence.model.Player;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by aran on 16/07/17.
 */
public interface PlayerRepo extends CrudRepository<Player, String> {
}
