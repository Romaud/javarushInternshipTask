package com.game.exxeption_handler;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.*;

//класс для проверки валидности входных данных
public class PlayerHepler {
    private Player player;

    public PlayerHepler(Player player) {
        this.player = player;
    }

    public boolean checkId() {
        if (player.getId() < 1) return false;
        return true;
    }

    public boolean checkName() {
        if (player.getName().isEmpty() ||
                player.getName().length() < 1 || player.getName().length() > 12) {
            throw new BadRequestException("Invalid name");
        }
        return true;
    }

    public boolean checkTitle() {
        if (player.getTitle().isEmpty() ||
                player.getTitle().length() < 1 || player.getTitle().length() > 30) {
            throw new BadRequestException("Invalid title");
        }
        return true;
    }

    public boolean checkRace() {
        if (player.getRace() != null) {
            boolean result = false;
            for (Race race : Arrays.asList(Race.values())) {
                if (player.getRace() == race) {
                    result = true;
                }
            }
            return result;
        }
        return true;
    }

    public boolean checkProfession() {
        if (player.getProfession() != null) {
            boolean result = false;
            for (Profession profession : Arrays.asList(Profession.values())) {
                if (player.getProfession() == profession) {
                    result = true;
                }
            }
            return result;
        }
        return true;
    }

    public boolean checkBirthday() {
        if (player.getBirthday().getTime() < 0) throw new BadRequestException("Invalid birthday");
        Calendar calendar1 = new GregorianCalendar(2000, Calendar.JANUARY, 01);
        Calendar calendar2 = new GregorianCalendar(3000, Calendar.DECEMBER, 31);
        if (player.getBirthday().before(calendar1.getTime()) || player.getBirthday().after(calendar2.getTime())) {
            throw new BadRequestException("Invalid birthday");
        }
        return true;
    }

    public boolean checkExperience() {
        if (player.getExperience() > 10000000L || player.getExperience() < 0) {
            throw new BadRequestException("Invalid experience");
        }
        return true;
    }

    public boolean checkAllFieldsForNull() {
        return (player.getName() == null) && (player.getTitle() == null) &&
                (player.getBirthday() == null) && (player.getExperience() == null) &&
                (player.getProfession() == null) && (player.getRace() == null);
    }

    public boolean checkAllFields() {
        return !checkAllFieldsForNull() && checkName() && checkTitle() && checkRace()
                && checkProfession() && checkBirthday() && checkExperience();
    }
    
}
