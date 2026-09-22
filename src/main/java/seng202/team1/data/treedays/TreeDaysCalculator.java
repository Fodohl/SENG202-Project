package seng202.team1.data.treedays;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import seng202.team1.TrashApp;
import seng202.team1.data.sqlite.DAO.DAOInterface;
import seng202.team1.data.trashlog.TrashEmission;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;

/**
 * Calculates the total logged waste weight used by the tree-days feature.
 */
public class TreeDaysCalculator {

    public double getTreeDays(DAOInterface<TrashLog> trashLogStore) {
        List<TrashLog> logs = trashLogStore.getAll();
        List<TrashEmission> emissions = allTrashEmissions();
        

        double runningTotal = 0;
        for (int i = 0; i < logs.size(); i++) {
            double weight = logs.get(i).trashWeight();
            TrashType type = logs.get(i).trashType();
            TrashEmission emission = getEmission(type, emissions);

            runningTotal += ((emission.litteredFactor()-emission.treatedFactor())*(weight/1000));
        }
        return runningTotal*60.33;
    }

    public List<TrashEmission> allTrashEmissions(){
        InputStream input = getClass().getResourceAsStream("/data/trash_emissions.json");
        InputStreamReader reader = new InputStreamReader(input);
        Type listType = new TypeToken<List<TrashEmission>>() {}.getType();
        return TrashApp.GSON.fromJson(reader, listType);
    }
    public TrashEmission getEmission(TrashType type, List<TrashEmission> emissions){

        TrashEmission emission = emissions.stream()
            .filter(item -> item.id().equals(type.name()))
            .findFirst()
            .orElseThrow();
        return emission;
    }

}
