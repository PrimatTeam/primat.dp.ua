/*
 *  
 */

package ua.dp.primat.curriculum.data;

/**
 *
 * @author EniSh
 */
public enum FinalControlType {
    Exam,
    Setoff,
    DifferentiableSetoff,
    Nothing;

    @Override
    public String toString() {
        switch(this) {
            case Exam:
                return "�������";
            case DifferentiableSetoff:
                return "��������������� ����";
            case Setoff:
                return "����";
            case Nothing:
                return "ͳ����";
        }
    }
}
