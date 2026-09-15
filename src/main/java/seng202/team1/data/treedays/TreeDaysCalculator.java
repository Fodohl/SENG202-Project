package seng202.team1.data.treedays;

import java.util.List;

import seng202.team1.data.trashlog.*;

public class TreeDaysCalculator {

    public float getTreeDays(TrashLogStore trashLogStore) {
        List<TrashLog> ls = trashLogStore.getAll();
        int totalWeight = 0;
        for (int i = 0; i < ls.size(); i++) {
            totalWeight += ls.get(i).getTrashLogged().getTrashWeight();
        }
        return totalWeight;
    }
}
