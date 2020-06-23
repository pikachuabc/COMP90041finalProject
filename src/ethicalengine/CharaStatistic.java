package ethicalengine;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @description:
 * @author: Fan Jia
 */
public class CharaStatistic {

    private String characteristicName;
    private int totalCase = 0;
    private int totalSurvive = 0;

    public CharaStatistic(String characteristicName) {
        this.characteristicName = characteristicName;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public void setCharacteristicName(String characteristicName) {
        this.characteristicName = characteristicName;
    }

    public int getTotalCase() {
        return totalCase;
    }

    public void setTotalCase(int totalCase) {
        this.totalCase = totalCase;
    }

    public int getTotalSurvive() {
        return totalSurvive;
    }

    public void setTotalSurvive(int totalSurvive) {
        this.totalSurvive = totalSurvive;
    }

    /**
     * support function for finding a specific characteristic in a list
     *
     * @param name            : characteristic name in string
     * @param charaStatistics : where to find that characteristic
     * @return CharaStatistic
     * @author Fan Jia
     */
    public static CharaStatistic findCharacter(String name,
                                               ArrayList<CharaStatistic> charaStatistics) {
        for (CharaStatistic charaStatistic : charaStatistics) {
            if (charaStatistic.getCharacteristicName().equals(name)) {
                return charaStatistic;
            }
        }
        return null;
    }

    /**
     * record statistic under different situation
     *
     * @param isSurvive : if survive
     * @author Fan Jia
     */
    public void survive(boolean isSurvive) {
        this.totalCase++;
        if (isSurvive) {
            this.totalSurvive++;
        }

    }

    public String ratio() {
        DecimalFormat df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.DOWN);
        if (totalCase == 0) {
            return "0";
        }
        return df.format((double) totalSurvive / totalCase);
    }

    @Override
    public String toString() {

        return characteristicName.toLowerCase() + ": " + ratio() + "\n";
    }
}
