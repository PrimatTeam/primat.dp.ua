package ua.dp.primat.utils.view;

import java.util.List;
import org.apache.wicket.model.LoadableDetachableModel;
import ua.dp.primat.curriculum.data.StudentGroup;

/**
 * LoadableDetachableModel for student groups combo.
 */
public class GroupsLoadableDetachableModel extends LoadableDetachableModel<List<StudentGroup>> {

    private final List<StudentGroup> groups;

    public GroupsLoadableDetachableModel(List<StudentGroup> groups) {
        this.groups = groups;
    }

    @Override
    protected List<StudentGroup> load() {
        return groups;
    }
}