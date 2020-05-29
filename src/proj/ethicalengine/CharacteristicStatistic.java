package proj.ethicalengine;

/**
 * @description:each characterstic
 * @author: Fan Jia
 */
public class CharacteristicStatistic {

    private String characteristicName;
    private int totalCase=0;
    private int totalSurvive=0;

    public CharacteristicStatistic(String characteristicName) {
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

    public void survive(boolean isSurvive) {
        if (isSurvive) {
            this.totalCase++;
            this.totalSurvive++;
        } else {
            this.totalCase++;
        }

    }

    public String ratio() {
        return String.format("%.2f",(double)totalSurvive/totalCase);
    }

    @Override
    public String toString() {
        return characteristicName.toLowerCase()+": "+ratio()+"\n";
    }
}
