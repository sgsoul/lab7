package common.data;

/**
 * Интерфейс для объекта. =)
 */

public interface Collectionable extends Comparable<Collectionable>, Validateable {

    public int getId();

    public void setId(int ID);

    public Integer getImpactSpeed();

    public String getName();

    public int compareTo(Collectionable human);

    public boolean validate();
}
