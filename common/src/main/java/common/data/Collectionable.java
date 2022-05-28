package common.data;

/**
 * ��������� ��� �������. =)
 */

public interface Collectionable extends Comparable<Collectionable>, Validateable {

    int getId();

    void setId(int ID);

    Integer getImpactSpeed();

    String getName();

    int compareTo(Collectionable human);

    boolean validate();
}
