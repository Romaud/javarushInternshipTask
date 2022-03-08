package com.game.controller;

import com.game.entity.Player;
import com.game.exxeption_handler.BadRequestException;
import com.game.exxeption_handler.NotFoundException;
import com.game.exxeption_handler.PlayerHepler;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class MyController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("rest/players")
    public List<Player> findAllPlayers(@RequestParam Map<String, String> allParams, Integer limit) {
        List<Player> playerList = playerService.findAllPlayers(allParams, limit);
        return playerList;
    }

    @GetMapping("rest/players/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        Player player = playerService.getPlayer(id);
        if (id < 1) throw new BadRequestException("Invalid ID");
        if (player == null) throw new NotFoundException("There is no player with ID = " + id);
        return player;
    }

    @GetMapping("rest/players/count")
    public Integer getCountPlayers(@RequestParam Map<String, String> map) {
        return findAllPlayers(map, 1).size();
    }

    @PostMapping("rest/players/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player, @PathVariable Long id) {
        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        PlayerHepler playerHepler = new PlayerHepler(player);
        if (playerHepler.checkAllFieldsForNull()) return new ResponseEntity<>(getPlayerById(id), HttpStatus.OK);
        Player newPlayer = getPlayerById(id);

        if (player.getName() != null && playerHepler.checkName()) newPlayer.setName(player.getName());
        if (player.getTitle() != null && playerHepler.checkTitle()) newPlayer.setTitle(player.getTitle());
        if (player.getRace() != null && playerHepler.checkRace()) newPlayer.setRace(player.getRace());
        if (player.getProfession() != null && playerHepler.checkProfession())
            newPlayer.setProfession(player.getProfession());
        if (player.getBirthday() != null && playerHepler.checkBirthday()) newPlayer.setBirthday(player.getBirthday());
        if (player.getExperience() != null && playerHepler.checkExperience())
            newPlayer.setExperience(player.getExperience());
        if (player.getBanned() != null) newPlayer.setBanned(player.getBanned());

        playerService.savePlayer(newPlayer);
        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    @PostMapping("rest/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        PlayerHepler playerHepler = new PlayerHepler(player);
        if (!playerHepler.checkAllFields()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        playerService.savePlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @DeleteMapping("rest/players/{id}")
    public ResponseEntity<HttpStatus> deletePlayer(@PathVariable Long id) {
        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = getPlayerById(id);
        playerService.deletePlayer(player);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
