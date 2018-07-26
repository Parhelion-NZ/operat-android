package nz.co.parhelion.operat.model;

import android.arch.lifecycle.ViewModel;

import nz.co.parhelion.operat.OperatApplication;
import nz.co.parhelion.operat.R;

public class OperatForm extends ViewModel {

    public long meshblockId;
    public int numberOfProperties;

    public boolean wholeArea;

    public boolean q1, q2, q3, q4, q5, q6, q7;

    public enum Q8 {
        NONE(R.string.q8_none), ONE_TO_ELEVEN(R.string.q8_one_to_eleven), TWELVE_OR_MORE(R.string.q8_twelve_or_more);

        private int name;

        Q8(int name) {
            this.name = name;
        }

        public String toString() {
            return OperatApplication.getContext().getString(name);
        }
    }

    public Q8 q8;

    public enum Q9 {
        RESIDENTS(R.string.q9_residents),
        NOT_RESIDENTS(R.string.q9_not_residents);

        private int name;

        Q9(int name) {
            this.name = name;
        }
        public String toString() {
            return OperatApplication.getContext().getString(name);
        }
    }
    public Q9 q9;

    public enum Q10 {NO_PAVEMENT(R.string.q10_no_pavement), NOT_CONTINUOUS(R.string.q10_not_continuous), CONTINUOUS(R.string.q10_yes);
        private int name;

        Q10(int name) {
            this.name = name;
        }
        public String toString() {
            return OperatApplication.getContext().getString(name);
        }
    };

    public Q10 q10;

    public enum Q11 {FLAT, MEDIUM, STEEP};

    public Q11 q11;

    public enum Q12 {WELL, MODERATE, POOR};

    public Q12 q12;

    public enum Q13 {RESIDENTIAL, GREEN, INDUSTRIAL};

    public Q13 q13;

    public boolean q14, q15, q16, q17;

    //actual numbers
    public int q18, q19, q20, q21;

    public int q18No, q19No, q19Na, q20Mod, q20Poor, q20Na, q21Mod, q21Poor;

    public void setYesNo(int question, boolean value) {
        switch (question) {
            case 1:
                q1 = value;
                return;
            case 2:
                q2 = value;
                return;
            case 3:
                q3 = value;
                return;
            case 4:
                q4 = value;
                return;
            case 5:
                q5 = value;
                return;
            case 6:
                q6 = value;
                return;
            case 7:
                q7 = value;
                return;
            case 14:
                q14 = value;
                return;
            case 15:
                q15 = value;
                return;
            case 16:
                q16 = value;
                return;
            case 17:
                q17 = value;
                return;

        }
        System.out.println(this);

    }

    public void setInt(int question, int value) {
        switch (question) {
            case 18:
                q18 = value;
                return;
            case 19:
                q19 = value;
                return;
            case 20:
                q20 = value;
                return;
            case 21:
                q21 = value;
                return;

        }
    }

    public void setEnum(Enum option) {
        if (option instanceof Q8) {
            q8 = (Q8)option;
        } else if (option instanceof Q9) {
            q9 = (Q9)option;
        } else if (option instanceof Q10) {
            q10 = (Q10)option;
        } else if (option instanceof Q11) {
            q11 = (Q11)option;
        } else if (option instanceof Q12) {
            q12 = (Q12)option;
        } else if (option instanceof Q13) {
            q13 = (Q13)option;
        }
        System.out.println(this);
    }

    public String toString() {

        return "Q1: "+q1+"; Q2: "+q2+"; Q3: "+q3+"; Q4: "+q4+"; Q5: "+q5+"; Q6: "+q6+"; Q7: "+q7+"; Q8: "+q8+"; Q9: "+q9+"; Q10: "+q10+"; "+
                "Q11: "+q11+"; Q12: "+q12+"; Q13: "+q13+"; Q14: "+q14+"; Q15: "+q15+"; Q16: "+q16+"; Q17: "+q17+"; Q18: "+q18+"; Q19: "+q19+"; ";
    }
}
