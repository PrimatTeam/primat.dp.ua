/*
 * ����� ���������� ����� ��� WorkloadEntry.
 * ��� ��� ������ �������� ���������� � ������������ �������� � �����������
 * ��������� � ������������� ��������
 */

package ua.dp.primat.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author EniSh
 */
@Embeddable
public class WorkloadEntryPK implements Serializable {
    @Column(name = "workload_id")
    Long workloadId;

    @Column(name = "semester_number")
    Long semesterNumber;

    public WorkloadEntryPK() {
    }
}
