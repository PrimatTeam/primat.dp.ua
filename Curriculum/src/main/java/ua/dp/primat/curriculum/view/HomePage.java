package ua.dp.primat.curriculum.view;

import java.util.List;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.workload.Workload;
import ua.dp.primat.repositories.WorkloadRepository;
import ua.dp.primat.utils.view.ChoosePanel;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;
    @SpringBean
    private WorkloadRepository workloadRepository;
    private ListView<Workload> workloadsView;
    private List<Workload> workloads;

    public HomePage() {

        final ChoosePanel choosePanel = new ChoosePanel("choosePanel") {

            @Override
            protected void executeAction(StudentGroup studentGroup, Long semester) {
                workloads = workloadRepository.getWorkloadsByGroupAndSemester(studentGroup, semester);
                if (workloadsView != null) {
                    workloadsView.setList(workloads);
                }
            }
        };
        add(choosePanel);

        workloadsView = new WorkloadsListView("disciplineRow", workloads);
        add(workloadsView);
    }

    private static class WorkloadsListView extends ListView<Workload> {

        public WorkloadsListView(String string, List<? extends Workload> list) {
            super(string, list);
        }

        @Override
        protected void populateItem(ListItem<Workload> li) {
            final Workload workload = li.getModelObject();
            li.add(new Label("disciplineName", workload.getDiscipline().getName()));
            li.add(new Label("cathedra", workload.getDiscipline().getCathedra().getName()));
            li.add(new Label("lection", workload.getLectionHours().toString()));
            li.add(new Label("practice", workload.getPracticeHours().toString()));
            li.add(new Label("labs", workload.getLaboratoryHours().toString()));
            li.add(new Label("selfwork", workload.getSelfworkHours().toString()));
            li.add(new Image("course") {

                @Override
                public boolean isVisible() {
                    return workload.getCourseWork();
                }
            });
            li.add(new Label("finalcontrol", workload.getFinalControlType().toString()));
        }
    }
}
