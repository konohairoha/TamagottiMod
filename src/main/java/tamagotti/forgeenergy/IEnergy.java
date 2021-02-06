package tamagotti.forgeenergy;

import java.util.UUID;

public interface IEnergy {

	void removeCapacity(UUID id, int amount);

    void addCapacity(UUID id, int amount);

    int getCapacity();

    int getStored();

    void setStored(int amount);

    int extract(int amount, Action action);

    int insert(int amount, Action action);
}
