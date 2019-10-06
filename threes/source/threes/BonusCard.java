public class BonusCard{
    int maxCard;

    public BonusCard(int maxCard){
        this.maxCard = maxCard;
    }

    public double bonusProb() {
        int count = 1, tmp = maxCard/8;
        while (tmp > 6) {
            count++;
            tmp = tmp / 2;
        }
        return 1.0/count;
    }
}