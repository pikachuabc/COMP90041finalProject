
import java.util.ArrayList;

/**
 * @description:
 * @author: Fan Jia
 */
public  class CharaStatistic {

    private String characteristicName;
    private int totalCase=0;
    private int totalSurvive=0;

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
     * @author Fan Jia
     * @methodName findCharacter
     * @param name :
     * @param charaStatistics :
     * @return ethicalengine.CharaStatistic
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
     * @author Fan Jia
     * @methodName survive
     * @param isSurvive : if survive
     * @return void
     */
    public void survive(boolean isSurvive) {
        this.totalCase++;
        if (isSurvive) {
            this.totalSurvive++;
        }

    }

    public String ratio() {
        return String.format("%.1f",(double)totalSurvive/totalCase);
    }

    @Override
    public String toString() {
        return characteristicName.toLowerCase()+": "+ratio()+"\n";
    }
}
