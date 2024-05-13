
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Az adatbázis egy rekordját reprezentálja.
 * @author Szemesi Gábor
 */
public class HighScore {

    private final String name;
    private final int level;
    private final long time;

    public HighScore(String name, int level, long time) {
        this.name = name;
        this.level = level;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        DateFormat obj = new SimpleDateFormat("m:ss");
        Date sol = new Date(time);
        return "name: " + name + ", level: " + level + ", time: " + obj.format(sol) + '\n';
    }
}
