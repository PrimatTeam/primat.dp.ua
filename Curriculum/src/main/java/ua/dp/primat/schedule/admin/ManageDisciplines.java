package ua.dp.primat.schedule.admin;

import java.util.List;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import ua.dp.primat.domain.workload.Discipline;
import ua.dp.primat.repositories.DisciplineRepository;

/**
 *
 * @author EniSh
 */
public final class ManageDisciplines extends WebPage {
    private static final long serialVersionUID = 1L;
    
    public ManageDisciplines() {
        super ();

        final List<Discipline> disciplines = disciplineRepository.getDisciplines();

        final ListView<Discipline> disciplineView = new DosciplineListView("repeating", disciplines, disciplines);

        add(disciplineView);
    }

    @SpringBean
    private DisciplineRepository disciplineRepository;

    private class DosciplineListView extends ListView<Discipline> {

        private final List<Discipline> disciplines;

        public DosciplineListView(String id, List<? extends Discipline> list, List<Discipline> disciplines) {
            super(id, list);
            this.disciplines = disciplines;
        }

        @Override
        protected void populateItem(ListItem<Discipline> li) {
            final Discipline discipline = li.getModelObject();
            li.add(new Label("disciplineName", discipline.getName()));
            li.add(new Label("disciplineCathedra", discipline.getCathedra().toString()));

            IPageLink page = new IPageLink() {

                public Page getPage() {
                    return new EditDisciplinePage(discipline);
                }

                public Class<? extends Page> getPageIdentity() {
                    return EditDisciplinePage.class;
                }
            };
            if (page != null) {
            final Link editLink = new PageLink("editDiscipline", page);
                editLink.add(new Image("editImage"));
                li.add(editLink);
                final Link deleteLink = new Link("deleteDiscipline") {

                    @Override
                    public void onClick() {
                        disciplineRepository.delete(discipline);
                        disciplines.remove(discipline);
                    }
                };
                deleteLink.add(new Image("deleteImage"));
                li.add(deleteLink);
            }
        }
    }
}

