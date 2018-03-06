package model;

import dao.ManagerMachine;
import entity.Machine;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author preda
 */
public class MachineDispoModel extends AbstractListModel<Machine>
{
    private ArrayList<Machine> listeMachines;
    
    public MachineDispoModel()
    {
        listeMachines = ManagerMachine.getMachineList();
    }

    @Override
    public int getSize()
    {
        return listeMachines.size();
    }

    @Override
    public Machine getElementAt(int index)
    {
        return listeMachines.get(index);
    }
    
    public void delete(int index)
    {
        listeMachines.remove(index);
        fireIntervalRemoved(this, index, index);
    }
    
    public void add(Machine m)
    {
        listeMachines.add(m);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);
    }
}
