package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    PlayerRepository playerRepository;

    public List<Player> findAllPlayers(Map<String, String> allParams, Integer limit) {
        //объявление всех переменных
        String name = allParams.get("name");
        String title = allParams.get("title");
        String race = allParams.get("race");
        String profession = allParams.get("profession");
        Long after;
        if (allParams.get("after") != null) after = Long.parseLong(allParams.get("after"));
        else after = null;
        Long before;
        if (allParams.get("before") != null) before = Long.parseLong(allParams.get("before"));
        else before = null;
        Boolean banned;
        if (allParams.get("banned") != null) banned = Boolean.valueOf(allParams.get("banned"));
        else banned = null;
        Integer minExperience;
        if (allParams.get("minExperience") != null) minExperience = Integer.parseInt(allParams.get("minExperience"));
        else minExperience = null;
        Integer maxExperience;
        if (allParams.get("maxExperience") != null) maxExperience = Integer.parseInt(allParams.get("maxExperience"));
        else maxExperience = null;
        Integer minLevel;
        if (allParams.get("minLevel") != null) minLevel = Integer.parseInt(allParams.get("minLevel"));
        else minLevel = null;
        Integer maxLevel;
        if (allParams.get("maxLevel") != null) maxLevel = Integer.parseInt(allParams.get("maxLevel"));
        else maxLevel = null;
        PlayerOrder order;
        if (allParams.get("order") != null) order = PlayerOrder.valueOf(allParams.get("order"));
        else order = PlayerOrder.ID;
        int pageNumber = 0;
        if (allParams.get("pageNumber") != null) pageNumber = Integer.parseInt(allParams.get("pageNumber"));
        int pageSize = 3;
        if (allParams.get("pageSize") != null) pageSize = Integer.parseInt(allParams.get("pageSize"));

        //сортировка и фильтрация списка объектов
        List<Player> allPlayers = playerRepository.findAll();
        List<Player> resultList;
        resultList = allPlayers.stream().sorted(((player1, player2) -> {
                    if (PlayerOrder.LEVEL.equals(order)) {
                        return player1.getLevel().compareTo(player2.getLevel());
                    }
                    if (PlayerOrder.BIRTHDAY.equals(order)) {
                        return player1.getBirthday().compareTo(player2.getBirthday());
                    }

                    if (PlayerOrder.EXPERIENCE.equals(order)) {
                        return player1.getExperience().compareTo(player2.getExperience());
                    }
                    if (PlayerOrder.NAME.equals(order)) {
                        return player1.getName().compareTo(player2.getName());
                    }
                    return player1.getId().compareTo(player2.getId());
                }))
                .filter(p -> name == null || p.getName().contains(name))
                .filter(p -> title == null || p.getTitle().contains(title))
                .filter(p -> race == null || p.getRace().toString().contains(race))
                .filter(p -> profession == null || p.getProfession().toString().contains(profession))
                .filter(p -> after == null || p.getBirthday().getTime() > after)
                .filter(p -> before == null || p.getBirthday().getTime() < before)
                .filter(p -> banned == null || p.getBanned().equals(banned))
                .filter(p -> minExperience == null || p.getExperience() >= minExperience)
                .filter(p -> maxExperience == null || p.getExperience() <= maxExperience)
                .filter(p -> minLevel == null || p.getLevel() >= minLevel)
                .filter(p -> maxLevel == null || p.getLevel() <= maxLevel)
                .skip(pageSize * pageNumber).collect(Collectors.toList());

        allPlayers = resultList.stream().limit(pageSize).collect(Collectors.toList());

        if (limit != null) return resultList;
        else return allPlayers;
    }

    @Override
    public void savePlayer(Player player) {
        int exp = player.getExperience();
        int lvl = (int) (Math.sqrt(2500 + 200 * exp) - 50) / 100;
        int untilNextLevel = 50 * (lvl + 1) * (lvl + 2) - exp;
        player.setLevel(lvl);
        player.setUntilNextLevel(untilNextLevel);

        playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    @Override
    public Player getPlayer(long id) {
        Player player = null;
        Optional<Player> optional = playerRepository.findById(id);
        if (optional.isPresent()) {
            player = optional.get();
        }
        return player;
    }
}
